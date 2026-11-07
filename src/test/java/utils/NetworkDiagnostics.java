package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriver;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Scenario-scoped diagnostics for local Chrome/Edge.
 *
 * Public API remains compatible with the uploaded Hooks.java:
 * enabled(config), start(driver, config, baseUrl), collect(), summary(), stop().
 * Background work never calls WebDriver, Cucumber, Hooks or ExtentReports.
 *
 * Only HTTP >= 400 and non-cancelled browser network failures are reported.
 * Successful responses, including HTTP 200 JSON-RPC error envelopes, are NOT
 * reported in this failed-network-only mode. Intentional browser cancellations
 * and net::ERR_ABORTED without an HTTP error are excluded.
 *
 * For each failed request, retains HTTP method/status, payload's JSON-RPC method,
 * and the returned text/JSON response rather than a scalar field selection.
 * No request is replayed; request params, cookies and auth headers are not exported.
 * Secret masking is best-effort and is NOT comprehensive PII removal.
 * Body/queue/record limits are explicit in the report. Unavailable data is not
 * invented. Read bodies after loadingFinished on the same CDP session/requestId.
 *
 * Source prepared from the last uploaded utility, with the URI correction applied.
 * Not compiled or executed here; validate in the project before full-suite use.
 */
public final class NetworkDiagnostics {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final ThreadLocal<Capture> CURRENT = new ThreadLocal<>();
    private static final int MAX_TRACKED = 300;
    private static final int MAX_FAILURES = 50;
    private static final int MAX_PENDING_READS = 32;
    private static final int MAX_PAYLOAD_CHARS = 262_144;
    private static final Pattern JWT = Pattern.compile(
            "[A-Za-z0-9_-]{15,}\\.[A-Za-z0-9_-]{15,}\\.[A-Za-z0-9_-]{10,}");
    private static final Pattern BEARER = Pattern.compile("(?i)Bearer\\s+[^\\s\"'<>]+");
    private static final Pattern NAMED_SECRET = Pattern.compile(
            "(?i)((?:password|passwd|client[_-]?secret|access[_-]?token|refresh[_-]?token|"
                    + "id[_-]?token|api[_-]?key|authorization)\\s*[:=]\\s*)[^\\s,;\"'<>]+");

    private NetworkDiagnostics() { }

    public static boolean enabled(ConfigReader config) {
        return "true".equalsIgnoreCase(value(config, "network.capture.enabled", "false"));
    }

    /** Call immediately after driver creation, BEFORE navigation. */
    public static void start(WebDriver driver, ConfigReader config, String baseUrl) {
        stop();
        if (!enabled(config)) return;
        Capture c = new Capture(config, baseUrl);
        CURRENT.set(c);
        try {
            if (!(driver instanceof ChromiumDriver) || !(driver instanceof HasCapabilities)) {
                throw new IllegalStateException("Local Chromium driver required");
            }
            Map<String, Object> caps = ((HasCapabilities) driver).getCapabilities().asMap();
            Object options = caps.get("goog:chromeOptions");
            if (!(options instanceof Map)) options = caps.get("ms:edgeOptions");
            if (!(options instanceof Map)) throw new IllegalStateException("Debugger address absent");
            Object address = ((Map<?, ?>) options).get("debuggerAddress");
            if (address == null) throw new IllegalStateException("Debugger address absent");

            // Construct a real loopback HTTP discovery URI, not a placeholder prefix.
            URI debugger = new URI("http", address.toString().trim(), null, null, null);
            if (!loopback(debugger.getHost()) || debugger.getPort() < 1
                    || debugger.getUserInfo() != null
                    || !Optional.ofNullable(debugger.getRawPath()).orElse("").isEmpty()
                    || debugger.getRawQuery() != null || debugger.getRawFragment() != null) {
                throw new IllegalStateException("Only a loopback debugger address is supported");
            }
            c.connect(debugger);
            System.out.println("[NETWORK] Live capture active: failed requests only; "
                    + "HTTP status, payload method and response body. Body capture "
                    + (c.includeBody ? "enabled." : "DISABLED (responseBody=false)."));
        } catch (Exception e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            c.close();
            c.startFailure = "Live capture unavailable (" + rootType(e) + "). "
                    + "No API-health conclusion can be drawn. Validate capture startup first.";
            System.err.println("[NETWORK] " + c.startFailure);
        }
    }

    /** Wait briefly for already-queued events/body reads; no WebDriver calls. */
    public static void collect() {
        Capture c = CURRENT.get();
        if (c == null || c.closed) return;
        try {
            c.worker.submit(() -> { }).get(2, TimeUnit.SECONDS);
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            while (c.pendingReads.get() > 0 && System.nanoTime() < deadline) Thread.sleep(25);
            c.worker.submit(() -> { }).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            c.checkpointProblems.incrementAndGet();
        }
    }

    public static String summary() {
        Capture c = CURRENT.get();
        if (c == null) return "Network diagnostics were not started for this scenario.";
        if (!c.startFailure.isEmpty()) return "FAILED API REQUESTS\n" + c.startFailure;
        if (c.closed) return "FAILED API REQUESTS\nCapture stopped; attach evidence before stop().";
        try {
            return c.worker.submit(c::render).get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "FAILED API REQUESTS\nSnapshot interrupted; evidence incomplete.";
        } catch (Exception e) {
            return "FAILED API REQUESTS\nSnapshot unavailable (" + rootType(e)
                    + "); this is a capture problem, not proof of no API errors.";
        }
    }

    /** Call on the scenario thread before driver.quit(). */
    public static void stop() {
        Capture c = CURRENT.get();
        CURRENT.remove();
        if (c != null) c.close();
    }

    private static final class Capture {
        final boolean includeBody;
        final int maxBodyBytes;
        final int maxFrameChars;
        final Set<String> hosts = new TreeSet<>();
        final ThreadPoolExecutor worker;
        final AtomicInteger nextId = new AtomicInteger();
        final AtomicInteger pendingReads = new AtomicInteger();
        final AtomicLong droppedEvents = new AtomicLong();
        final AtomicLong checkpointProblems = new AtomicLong();
        final ConcurrentMap<Integer, CompletableFuture<JsonNode>> commands = new ConcurrentHashMap<>();
        // Non-volatile request state and collections below are owned by worker only.
        final Map<String, CompletableFuture<JsonNode>> targetReady = new HashMap<>();
        final Set<String> sessions = new HashSet<>();
        final Map<String, Request> tracked = new LinkedHashMap<>();
        final Deque<Request> failures = new ArrayDeque<>();
        final Set<String> warnings = new LinkedHashSet<>();
        long responses, outsideScope, excludedCancellations;
        long bodyUnavailable, bodyLimited, trackingEvictions, failureEvictions, targetFailures;
        volatile WebSocket socket;
        volatile boolean closed, connectionLost;
        volatile String startFailure = "";

        Capture(ConfigReader config, String baseUrl) {
            includeBody = "true".equalsIgnoreCase(value(config, "network.capture.responseBody", "false"));
            maxBodyBytes = integerSetting(config, "network.capture.maxResponseBytes",
                    1_048_576, 16_384, 8_388_608);
            // JSON escaping can expand a CDP message substantially.
            maxFrameChars = Math.max(4_194_304, maxBodyBytes * 6 + 1_048_576);
            try {
                String host = URI.create(baseUrl).getHost();
                if (host != null) hosts.add(host.toLowerCase(Locale.ROOT));
            } catch (RuntimeException ignored) { }
            for (String host : value(config, "network.capture.allowedHosts", "").split(",")) {
                if (!host.trim().isEmpty()) hosts.add(host.trim().toLowerCase(Locale.ROOT));
            }
            worker = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1024), runnable -> {
                Thread thread = new Thread(runnable, "browser-api-capture-" + UUID.randomUUID());
                thread.setDaemon(true);
                return thread;
            }, new ThreadPoolExecutor.AbortPolicy());
        }

        void connect(URI debugger) throws Exception {
            HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3))
                    .proxy(new ProxySelector() {
                        @Override public List<Proxy> select(URI uri) { return List.of(Proxy.NO_PROXY); }
                        @Override public void connectFailed(URI uri, SocketAddress address,
                                                            java.io.IOException e) { }
                    }).build();
            HttpRequest request = HttpRequest.newBuilder(debugger.resolve("/json/version"))
                    .timeout(Duration.ofSeconds(4)).GET().build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || response.body().length() > 65_536) {
                throw new IllegalStateException("Debugger discovery unavailable");
            }
            URI ws = URI.create(JSON.readTree(response.body()).path("webSocketDebuggerUrl").asText());
            if (!"ws".equals(ws.getScheme()) || !loopback(ws.getHost())
                    || ws.getPort() != debugger.getPort() || ws.getUserInfo() != null) {
                throw new IllegalStateException("Unexpected debugger endpoint");
            }
            socket = http.newWebSocketBuilder().connectTimeout(Duration.ofSeconds(4))
                    .buildAsync(ws, new SocketListener(this)).get(5, TimeUnit.SECONDS);
            command("Target.getTargets", Map.of(), "")
                    .thenComposeAsync(result -> {
                        List<CompletableFuture<JsonNode>> initial = new ArrayList<>();
                        for (JsonNode info : result.path("targetInfos")) {
                            if ("page".equals(info.path("type").asText())) {
                                initial.add(attach(info.path("targetId").asText()));
                            }
                        }
                        if (initial.isEmpty()) {
                            throw new CompletionException(new IllegalStateException("No page target"));
                        }
                        return CompletableFuture.allOf(initial.toArray(new CompletableFuture<?>[0]));
                    }, worker).get(10, TimeUnit.SECONDS);
            command("Target.setDiscoverTargets", Map.of("discover", true), "").get(6, TimeUnit.SECONDS);
        }

        CompletableFuture<JsonNode> attach(String target) {
            CompletableFuture<JsonNode> existing = targetReady.get(target);
            if (existing != null) return existing;
            CompletableFuture<JsonNode> ready = command("Target.attachToTarget",
                    Map.of("targetId", target, "flatten", true), "")
                    .thenComposeAsync(result -> {
                        String session = result.path("sessionId").asText();
                        if (session.isEmpty()) {
                            throw new CompletionException(new IllegalStateException("No target session"));
                        }
                        sessions.add(session);
                        return command("Network.enable", Map.of(
                                "maxTotalBufferSize", 33_554_432,
                                "maxResourceBufferSize", maxBodyBytes,
                                "maxPostDataSize", MAX_PAYLOAD_CHARS), session);
                    }, worker);
            targetReady.put(target, ready);
            ready.whenCompleteAsync((result, error) -> {
                if (error != null) {
                    targetFailures++;
                    warn("A page target could not be enabled (" + rootType(error) + ").");
                }
            }, worker);
            return ready;
        }

        CompletableFuture<JsonNode> command(String method, Map<String, ?> params, String session) {
            CompletableFuture<JsonNode> future = new CompletableFuture<>();
            if (closed || connectionLost || commands.size() >= 100) {
                future.completeExceptionally(new IllegalStateException("CDP channel unavailable or busy"));
                return future;
            }
            int id = nextId.incrementAndGet();
            commands.put(id, future);
            future.orTimeout(5, TimeUnit.SECONDS).whenComplete((v, e) -> commands.remove(id, future));
            try {
                worker.execute(() -> {
                    if (closed || socket == null) {
                        future.completeExceptionally(new IllegalStateException("CDP channel closed"));
                        return;
                    }
                    try {
                        ObjectNode message = JSON.createObjectNode();
                        message.put("id", id);
                        message.put("method", method);
                        message.set("params", JSON.valueToTree(params));
                        if (!session.isEmpty()) message.put("sessionId", session);
                        socket.sendText(message.toString(), true).get(2, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        if (e instanceof InterruptedException) Thread.currentThread().interrupt();
                        future.completeExceptionally(e);
                    }
                });
            } catch (RejectedExecutionException e) {
                droppedEvents.incrementAndGet();
                future.completeExceptionally(e);
            }
            return future;
        }

        void receive(String text) {
            enqueue(() -> {
                try {
                    JsonNode event = JSON.readTree(text);
                    if (event.has("id")) {
                        CompletableFuture<JsonNode> pending = commands.remove(event.path("id").asInt());
                        if (pending != null) {
                            if (event.has("error")) {
                                pending.completeExceptionally(new IllegalStateException("CDP command rejected"));
                            } else {
                                pending.complete(event.path("result"));
                            }
                        }
                        return;
                    }
                    String method = event.path("method").asText();
                    JsonNode p = event.path("params");
                    if ("Target.targetCreated".equals(method)) {
                        JsonNode info = p.path("targetInfo");
                        if ("page".equals(info.path("type").asText())) attach(info.path("targetId").asText());
                    } else if ("Target.targetDestroyed".equals(method)) {
                        targetReady.remove(p.path("targetId").asText());
                    } else if ("Target.detachedFromTarget".equals(method)) {
                        String session = p.path("sessionId").asText();
                        sessions.remove(session);
                        tracked.keySet().removeIf(key -> key.startsWith(session + "|"));
                    } else if (method.startsWith("Network.")) {
                        String session = event.path("sessionId").asText();
                        if (sessions.contains(session)) network(session, method, p);
                    }
                } catch (Exception e) {
                    warn("A protocol event could not be processed (" + rootType(e) + ").");
                }
            });
        }

        void network(String session, String method, JsonNode p) {
            String id = p.path("requestId").asText();
            if (id.isEmpty()) return;
            String key = session + "|" + id;
            long now = System.currentTimeMillis();
            if ("Network.requestWillBeSent".equals(method)) {
                tracked.remove(key); // Redirect destinations are tracked independently.
                JsonNode request = p.path("request");
                if (accepted(request.path("url").asText(), p.path("type").asText())) {
                    room();
                    Request r = new Request(session, id, safeMethod(request.path("method").asText()),
                            endpoint(request.path("url").asText()), now);
                    r.hasPostData = request.path("hasPostData").asBoolean() || request.has("postData");
                    r.payloadMethod = payloadMethods(request.path("postData").asText(""));
                    // Deliberately do not retain/export raw postData or params.
                    tracked.put(key, r);
                }
            } else if ("Network.responseReceived".equals(method)) {
                JsonNode response = p.path("response");
                if (!accepted(response.path("url").asText(), p.path("type").asText())) {
                    outsideScope++;
                    return;
                }
                responses++;
                Request r = tracked.get(key);
                if (r == null) {
                    room();
                    r = new Request(session, id, "UNKNOWN", endpoint(response.path("url").asText()), now);
                    tracked.put(key, r);
                }
                r.status = response.path("status").asInt();
                r.statusText = response.path("statusText").asText("");
                r.mime = response.path("mimeType").asText("");
                r.correlation = traceHeaders(response.path("headers"));
                if (r.status >= 400) {
                    remember(r);
                    inspectPayloadMethod(r);
                }
            } else if ("Network.loadingFinished".equals(method)) {
                Request r = tracked.remove(key);
                // No successful bodies are retrieved or logged.
                if (r != null && r.status >= 400) inspectBody(r);
            } else if ("Network.loadingFailed".equals(method)) {
                Request r = tracked.remove(key);
                if (r == null) return;
                String reason = p.path("errorText").asText("");
                if (!shouldReport(r.status, true, p.path("canceled").asBoolean(), reason)) {
                    excludedCancellations++;
                    return;
                }
                r.networkError = reason.matches("net::[A-Z0-9_]+")
                        ? reason : "Browser request failed (no detailed reason returned)";
                r.bodyNote = "Unavailable: browser reported loadingFailed; no complete response body received.";
                remember(r);
                inspectPayloadMethod(r);
            }
        }

        void inspectPayloadMethod(Request r) {
            if (!r.payloadMethod.isEmpty() || !r.hasPostData || r.payloadRequested) return;
            r.payloadRequested = true;
            if (pendingReads.get() >= MAX_PENDING_READS) {
                r.payloadNote = "Unavailable: capture concurrency limit reached.";
                return;
            }
            pendingReads.incrementAndGet();
            command("Network.getRequestPostData", Map.of("requestId", r.id), r.session)
                    .whenCompleteAsync((result, error) -> {
                        try {
                            if (error != null) {
                                r.payloadNote = "Unavailable: browser did not retain request payload ("
                                        + rootType(error) + ").";
                            } else {
                                r.payloadMethod = payloadMethods(result.path("postData").asText(""));
                                r.payloadNote = r.payloadMethod.isEmpty()
                                        ? "No readable top-level JSON method was available." : "";
                            }
                        } finally {
                            pendingReads.decrementAndGet();
                        }
                    }, worker);
        }

        void inspectBody(Request r) {
            if (!includeBody) {
                r.bodyNote = "Disabled: set network.capture.responseBody=true to capture failed response bodies.";
                return;
            }
            if ("HEAD".equals(r.method)) {
                r.bodyNote = "No response body expected for HEAD.";
                return;
            }
            if (pendingReads.get() >= MAX_PENDING_READS) {
                bodyLimited++;
                r.bodyNote = "Unavailable: simultaneous capture-read limit reached.";
                return;
            }
            pendingReads.incrementAndGet();
            // Do not reject by MIME or encoded transfer size: error bodies may be
            // HTML/plain text, missing MIME, or compressed. Inspect returned content.
            command("Network.getResponseBody", Map.of("requestId", r.id), r.session)
                    .whenCompleteAsync((result, error) -> {
                        try {
                            if (error != null) {
                                bodyUnavailable++;
                                r.bodyNote = "Unavailable: response-body retrieval failed (" + rootType(error)
                                        + "). Browser buffer, target closure or protocol support may be involved."
                                        + " No request was resent.";
                                return;
                            }
                            String body = result.path("body").asText("");
                            byte[] bytes = result.path("base64Encoded").asBoolean()
                                    ? Base64.getDecoder().decode(body) : body.getBytes(StandardCharsets.UTF_8);
                            r.responseBytes = bytes.length;
                            if (bytes.length > maxBodyBytes) {
                                bodyLimited++;
                                r.bodyNote = "Omitted: returned body is " + bytes.length
                                        + " bytes; configured limit is " + maxBodyBytes
                                        + " bytes. This is NOT a complete response.";
                                return;
                            }
                            if (!textual(bytes, r.mime)) {
                                r.bodyNote = "Omitted: returned body appears binary (" + bytes.length
                                        + " bytes). This is NOT a displayed complete response.";
                                bodyLimited++;
                                return;
                            }
                            String decoded = new String(bytes, StandardCharsets.UTF_8);
                            r.body = responseText(decoded);
                            r.bodyReady = true;
                            r.bodyNote = bytes.length == 0 ? "Complete empty response received."
                                    : "Complete returned text/JSON body below, with best-effort secret masking.";
                        } catch (Exception e) {
                            bodyUnavailable++;
                            r.bodyNote = "Unavailable: could not decode/format response (" + rootType(e) + ").";
                        } finally {
                            pendingReads.decrementAndGet();
                        }
                    }, worker);
        }

        boolean accepted(String url, String type) {
            if (!Set.of("Fetch", "XHR", "Document").contains(type)) return false;
            try {
                URI uri = URI.create(url);
                return Set.of("http", "https").contains(uri.getScheme()) && uri.getHost() != null
                        && hosts.contains(uri.getHost().toLowerCase(Locale.ROOT));
            } catch (RuntimeException e) { return false; }
        }

        void room() {
            if (tracked.size() >= MAX_TRACKED) {
                tracked.remove(tracked.keySet().iterator().next());
                trackingEvictions++;
            }
        }

        void remember(Request r) {
            if (r.remembered) return;
            r.remembered = true;
            if (failures.size() >= MAX_FAILURES) {
                failures.removeFirst();
                failureEvictions++;
            }
            failures.addLast(r);
        }

        void warn(String warning) { if (warnings.size() < 8) warnings.add(warning); }

        void enqueue(Runnable task) {
            if (closed) return;
            try { worker.execute(task); }
            catch (RejectedExecutionException e) { droppedEvents.incrementAndGet(); }
        }

        String render() {
            StringBuilder out = new StringBuilder("FAILED API REQUESTS\nCapture: ")
                    .append(connectionLost ? "DISCONNECTED; retained evidence only." : "live CDP active.")
                    .append("\nFilter: HTTP >= 400 or non-cancelled network failure.")
                    .append("\nExcluded: successful responses, HTTP 200 business errors, and cancellations without HTTP errors.")
                    .append("\nAllowed hosts: ").append(String.join(", ", hosts))
                    .append("\nResponse bodies: ").append(includeBody ? "enabled" : "DISABLED")
                    .append("; per-body limit: ").append(maxBodyBytes).append(" bytes.")
                    .append("\nPayload params, cookies and authentication headers are not exported.")
                    .append("\nFull response content may contain personal data; secret masking is best-effort only.\n");
            int index = 0;
            for (Request r : failures) {
                out.append("\n========== FAILED REQUEST ").append(++index).append(" ==========\n")
                        .append("Observed (UTC): ").append(Instant.ofEpochMilli(r.atMillis))
                        .append("\nRequest URL: ").append(r.endpoint)
                        .append("\nHTTP method: ").append(r.method)
                        .append("\nHTTP status: ").append(statusLabel(r.status, r.statusText))
                        .append("\nPayload method: ").append(!r.payloadMethod.isEmpty() ? r.payloadMethod
                                : !r.payloadNote.isEmpty() ? r.payloadNote
                                : r.payloadRequested ? "Retrieval pending."
                                : "Not present in the captured JSON payload.")
                        .append("\nBrowser request ID: ").append(safeRequestId(r.id));
                if (!r.networkError.isEmpty()) out.append("\nNetwork error: ").append(r.networkError);
                if (!r.correlation.isEmpty()) out.append("\nTrace headers: ").append(r.correlation);
                out.append("\nContent type: ").append(cleanLine(r.mime))
                        .append("\nResponse availability: ").append(r.bodyNote);
                if (r.bodyReady) {
                    out.append("\nReturned body bytes: ").append(r.responseBytes)
                            .append("\n--- RESPONSE BODY ---\n")
                            .append(r.body.isEmpty() ? "[empty body]" : r.body)
                            .append("\n--- END RESPONSE BODY ---");
                }
                out.append('\n');
            }
            if (failures.isEmpty()) {
                out.append("\nNo matching failed requests captured. This does NOT prove the backend is healthy.\n");
            }
            out.append("\nCapture health (counts only; successful requests are not listed):")
                    .append("\nPage sessions: ").append(sessions.size())
                    .append("; in-scope responses: ").append(responses)
                    .append("; out-of-scope responses: ").append(outsideScope)
                    .append("; excluded cancellations: ").append(excludedCancellations)
                    .append("\nBodies unavailable: ").append(bodyUnavailable)
                    .append("; bodies omitted by limits/type: ").append(bodyLimited)
                    .append("; reads pending: ").append(pendingReads.get())
                    .append("\nTracking evictions: ").append(trackingEvictions)
                    .append("; failure evictions: ").append(failureEvictions)
                    .append("; dropped events: ").append(droppedEvents.get())
                    .append("; checkpoint problems: ").append(checkpointProblems.get())
                    .append("; target attach failures: ").append(targetFailures).append('\n');
            for (String warning : warnings) out.append("Capture warning: ").append(warning).append('\n');
            return out.append("\nScope: allowlisted Fetch/XHR/Document traffic from attached page targets. Latest ")
                    .append(MAX_FAILURES).append(" failures retained for the scenario; no time-window cutoff.")
                    .append(" New tabs are best-effort; workers/out-of-process frames may be missed.")
                    .append(" No request replay, no direct access to server logs.")
                    .append(" Browser failure events are a practical filter, not an exact copy of DevTools icons.\n")
                    .toString();
        }

        void close() {
            closed = true;
            WebSocket ws = socket;
            if (ws != null) ws.abort();
            for (CompletableFuture<JsonNode> pending : commands.values()) pending.cancel(false);
            commands.clear();
            worker.shutdownNow();
        }
    }

    private static final class SocketListener implements WebSocket.Listener {
        final Capture capture;
        final StringBuilder fragments = new StringBuilder();
        SocketListener(Capture capture) { this.capture = capture; }
        @Override public void onOpen(WebSocket socket) { socket.request(1); }
        @Override public CompletionStage<?> onText(WebSocket socket, CharSequence data, boolean last) {
            if ((long) fragments.length() + data.length() > capture.maxFrameChars) {
                capture.connectionLost = true;
                capture.enqueue(() -> capture.warn("Protocol message exceeded the safety limit; channel stopped."));
                fragments.setLength(0);
                socket.abort();
            } else {
                fragments.append(data);
                if (last) {
                    capture.receive(fragments.toString());
                    fragments.setLength(0);
                }
                socket.request(1);
            }
            return CompletableFuture.completedFuture(null);
        }
        @Override public CompletionStage<?> onClose(WebSocket socket, int status, String reason) {
            capture.connectionLost = true;
            return CompletableFuture.completedFuture(null);
        }
        @Override public void onError(WebSocket socket, Throwable error) {
            capture.connectionLost = true;
            capture.enqueue(() -> capture.warn("CDP connection failed (" + rootType(error) + ")."));
        }
    }

    private static boolean shouldReport(int status, boolean loadingFailed, boolean canceled, String reason) {
        return status >= 400 || (loadingFailed && !canceled && !"net::ERR_ABORTED".equals(reason));
    }

    /** Extract only method, never params. Supports individual and batch JSON-RPC payloads. */
    private static String payloadMethods(String postData) {
        if (postData == null || postData.isBlank() || postData.length() > MAX_PAYLOAD_CHARS) return "";
        try {
            JsonNode root = JSON.readTree(postData);
            if (root == null) return "";
            StringJoiner methods = new StringJoiner("\n");
            if (root.isArray()) {
                for (int i = 0; i < root.size(); i++) {
                    String method = payloadMethod(root.get(i));
                    if (!method.isEmpty()) methods.add("batch[" + i + "].method = " + method);
                }
            } else {
                return payloadMethod(root);
            }
            return methods.toString();
        } catch (Exception ignored) { return ""; }
    }

    private static String payloadMethod(JsonNode node) {
        JsonNode method = node.path("method");
        if (!method.isTextual()) return "";
        String text = method.asText();
        return text.matches("[A-Za-z0-9_.:/\\-]{1,256}") ? text : "[nonstandard method omitted]";
    }

    /** Keep the response structure/content, not selected fields and not an 800-char excerpt. */
    private static String responseText(String body) {
        if (body.isEmpty()) return "";
        try {
            JsonNode tree = JSON.readTree(body);
            if (tree != null) return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(maskJson(tree));
        } catch (Exception ignored) {
            // HTML/plain text and non-JSON responses are also useful error evidence.
        }
        return maskText(body);
    }

    private static JsonNode maskJson(JsonNode node) {
        if (node.isObject()) {
            ObjectNode copy = JSON.createObjectNode();
            node.fields().forEachRemaining(field -> copy.set(field.getKey(), sensitiveKey(field.getKey())
                    ? JSON.getNodeFactory().textNode("[REDACTED]") : maskJson(field.getValue())));
            return copy;
        }
        if (node.isArray()) {
            ArrayNode copy = JSON.createArrayNode();
            for (JsonNode item : node) copy.add(maskJson(item));
            return copy;
        }
        return node.isTextual() ? JSON.getNodeFactory().textNode(maskText(node.asText())) : node;
    }

    private static boolean sensitiveKey(String key) {
        String normalized = key.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
        return Set.of("password", "passwd", "secret", "clientsecret", "accesstoken", "refreshtoken",
                "idtoken", "token", "authorization", "proxyauthorization", "cookie", "setcookie",
                "apikey", "xapikey", "credentials").contains(normalized);
    }

    private static String maskText(String text) {
        String safe = JWT.matcher(text).replaceAll("[REDACTED_TOKEN]");
        safe = BEARER.matcher(safe).replaceAll("Bearer [REDACTED]");
        return NAMED_SECRET.matcher(safe).replaceAll("$1[REDACTED]");
    }

    private static boolean textual(byte[] bytes, String mime) {
        if (mime.toLowerCase(Locale.ROOT).startsWith("image/")
                || mime.toLowerCase(Locale.ROOT).startsWith("audio/")
                || mime.toLowerCase(Locale.ROOT).startsWith("video/")
                || "application/pdf".equalsIgnoreCase(mime)) return false;
        for (byte b : bytes) {
            int n = b & 0xff;
            if (n == 0 || (n < 9) || (n > 13 && n < 32)) return false;
        }
        return true;
    }

    private static String traceHeaders(JsonNode headers) {
        StringJoiner out = new StringJoiner("; ");
        headers.fields().forEachRemaining(field -> {
            String key = field.getKey().toLowerCase(Locale.ROOT);
            String text = field.getValue().asText();
            if (Set.of("x-correlation-id", "x-request-id", "request-id", "traceparent", "request-context").contains(key)
                    && text.matches("[A-Za-z0-9_.:/;=\\-]{1,300}")) out.add(key + "=" + text);
        });
        return out.toString();
    }

    private static String endpoint(String url) {
        try {
            URI uri = URI.create(url);
            // Omit userinfo, query strings and fragments, which may contain credentials.
            return uri.getScheme() + "://" + uri.getHost()
                    + (uri.getPort() >= 0 ? ":" + uri.getPort() : "")
                    + Optional.ofNullable(uri.getRawPath()).orElse("");
        } catch (RuntimeException e) { return "[endpoint unavailable]"; }
    }

    private static String statusLabel(int status, String statusText) {
        if (status == 0) return "No HTTP response";
        String label = cleanLine(statusText);
        // HTTP/2 may have no statusText: supply a familiar label for common codes.
        if (label.isEmpty()) {
            switch (status) {
                case 400: label = "Bad Request"; break;
                case 401: label = "Unauthorized"; break;
                case 403: label = "Forbidden"; break;
                case 404: label = "Not Found"; break;
                case 405: label = "Method Not Allowed"; break;
                case 408: label = "Request Timeout"; break;
                case 409: label = "Conflict"; break;
                case 413: label = "Content Too Large"; break;
                case 422: label = "Unprocessable Content"; break;
                case 429: label = "Too Many Requests"; break;
                case 500: label = "Internal Server Error"; break;
                case 502: label = "Bad Gateway"; break;
                case 503: label = "Service Unavailable"; break;
                case 504: label = "Gateway Timeout"; break;
                default: label = "";
            }
        }
        return status + (label.isEmpty() ? "" : " " + label);
    }

    private static String cleanLine(String text) { return text.replace('\r', ' ').replace('\n', ' '); }
    private static String safeRequestId(String id) {
        return id.matches("[A-Za-z0-9_.:-]{1,120}") ? id : "[unavailable]";
    }
    private static boolean loopback(String host) {
        return host != null && Set.of("localhost", "127.0.0.1", "[::1]", "::1")
                .contains(host.toLowerCase(Locale.ROOT));
    }
    private static String rootType(Throwable error) {
        Throwable current = error;
        for (int i = 0; i < 8 && current.getCause() != null; i++) current = current.getCause();
        return current.getClass().getSimpleName();
    }
    private static String safeMethod(String method) {
        return method.matches("[A-Z]{1,12}") ? method : "UNKNOWN";
    }
    private static String value(ConfigReader config, String key, String fallback) {
        String text = config == null ? null : config.getProperty(key);
        return text == null || text.trim().isEmpty() ? fallback : text.trim();
    }
    private static int integerSetting(ConfigReader config, String key, int fallback, int min, int max) {
        try {
            int n = Integer.parseInt(value(config, key, String.valueOf(fallback)));
            return n >= min && n <= max ? n : fallback;
        } catch (NumberFormatException e) { return fallback; }
    }

    private static final class Request {
        final String session, id, method, endpoint;
        final long atMillis;
        int status, responseBytes;
        boolean remembered, hasPostData, payloadRequested, bodyReady;
        String statusText = "", mime = "", correlation = "", networkError = "";
        String payloadMethod = "", payloadNote = "", body = "";
        String bodyNote = "Response not completed or body retrieval pending.";
        Request(String session, String id, String method, String endpoint, long atMillis) {
            this.session = session; this.id = id; this.method = method;
            this.endpoint = endpoint; this.atMillis = atMillis;
        }
    }

    /**
     * Optional offline checks: run utils.NetworkDiagnostics.main() on the test
     * classpath. No browser, application access or configuration changes needed.
     * These check formatting/filter helpers, NOT live browser capture.
     */
    public static void main(String[] args) throws Exception {
        require(shouldReport(500, false, false, ""), "HTTP 500 must be included");
        require(shouldReport(400, false, false, ""), "HTTP 400 must be included");
        require(!shouldReport(200, false, false, ""), "HTTP 200 must be excluded");
        require(!shouldReport(302, false, false, ""), "Redirect must be excluded");
        require(!shouldReport(0, true, true, "net::ERR_ABORTED"), "Cancellation must be excluded");
        require(!shouldReport(0, true, false, "net::ERR_ABORTED"), "Aborted request must be excluded");
        require(shouldReport(0, true, false, "net::ERR_CONNECTION_REFUSED"), "Network failure must be included");
        require(shouldReport(500, true, true, "net::ERR_ABORTED"), "HTTP error must not be hidden by cancellation");
        String payload = "{\"method\":\"searchPoliciesForEform7_WSIB\",\"params\":[{\"password\":\"private\"}]}";
        require("searchPoliciesForEform7_WSIB".equals(payloadMethods(payload)), "Payload method extraction");
        require(!payloadMethods(payload).contains("private"), "Payload params must not be exported");
        String message = "x".repeat(1500) + "_END_OF_MESSAGE";
        ObjectNode response = JSON.createObjectNode();
        response.put("id", "df3b183b-27a8-4244-9e8a-c423cac1d033_10");
        response.put("jsonrpc", "2.0");
        response.putObject("error").put("code", -32603).put("message", message);
        response.put("access_token", "do-not-publish");
        String rendered = responseText(response.toString());
        JsonNode roundTrip = JSON.readTree(rendered);
        require(message.equals(roundTrip.path("error").path("message").asText()), "Long response must not be clipped");
        require(roundTrip.path("error").path("code").asInt() == -32603, "Error code must be preserved");
        require(response.path("id").asText().equals(roundTrip.path("id").asText()), "Response ID must be preserved");
        require(!rendered.contains("do-not-publish"), "Known secret must be redacted");
        require("500 Internal Server Error".equals(statusLabel(500, "")), "HTTP status label");
        require("<html>Error details</html>".equals(responseText("<html>Error details</html>")), "HTML error body");
        require(!textual(new byte[]{0, 1, 2}, "application/octet-stream"), "Binary response must not be rendered");
        System.out.println("[NETWORK SELF-CHECK] Helper checks passed. Live browser capture is NOT tested by this check.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException("Self-check failed: " + message);
    }
}
