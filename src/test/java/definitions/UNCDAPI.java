package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import pages.OSPC.OSPCClaimOverview;
import utils.ConfigReader;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UNCDAPI {


	private OSPCClaimOverview OSPCClaimOverview;
	private String endpoint;
	private String requestBody;
	private Response response;
	private String Filepath = "src/test/resources/config/UNCD_API.properties"; // Path to your properties file

	ConfigReader prop= Hooks.getConfigReader();
	List<Map<String, String>> data = Hooks.data;
	public UNCDAPI() throws Exception {

	}

	@Given("the UNCD API endpoint is configured with {string}")
	public void theUNCDAPIEndpointIsConfiguredWith(String Endpoint) {
		endpoint = prop.getProperty("baseUrl")+ Endpoint;
	}

	@When("a UNCD POST request is sent")
	public void aUNCDPOSTRequestIsSent() {
		// Disable SSL validation
		RestAssured.useRelaxedHTTPSValidation();
		String bearerToken = prop.getProperty("BearerToken");
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Authorization", "Bearer " + bearerToken)
				.header("Content-Type", "application/json")
				.body(requestBody)
				.post(endpoint);
	}

	@Then("the UNCD response status code should be {int}")
	public void theUNCDResponseStatusCodeShouldBe(int statusCode) {
		// Log the response details for debugging
		//response.then().log().all(); // Logs the response details
		response.prettyPrint(); // Prints the response body
		assertThat(response.getStatusCode(), is(statusCode));
	}

	@Then("Extract the file id from the response for UNCD")
	public void extractTheFileIdFromTheResponseforUNCD() throws Exception {

		String fileId = response.jsonPath().getString("name");
		Assert.assertNotNull("File ID should not be null", fileId);
		prop.setProperty("FileId", fileId, Filepath); // Store the fileId in config.properties
		System.out.println("Extracted File ID: " + fileId);
	}

	@And("the UNCD request body is:")
	public void theUNCDRequestBodyIs(String body) {
		String claimNumber = prop.getProperty("ClaimNumber");
		String FileId = prop.getProperty("FileId");
		String LoginId = prop.getProperty("userLoginID");
		String LastName = prop.getProperty("LastName");
		String dateOfBirth = prop.getProperty("dateOfBirth");
		// Fetch ClaimNumber from config.properties
		requestBody = body.replace("{{ClaimNumber}}", claimNumber).replace("{{FileId}}", FileId).replace("{{userLoginID}}", LoginId)
				.replace( "{{LastName}}", LastName).replace("{{dateOfBirth}}", dateOfBirth); // Replace placeholder with actual value
	}


	@When("a POST request is sent with form data for UNCD")
	public void aPOSTRequestIsSentWithFormDataForUNCD() {
		String bearerToken = prop.getProperty("BearerToken");
		File file = new File(prop.getProperty("Filepath")); // Path to the file
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Authorization", "Bearer " + bearerToken)
				.header("Content-Type", "multipart/form-data")
				.multiPart("file", file) // Attach the file
				.formParam("claimId", prop.getProperty("ClaimNumber")) // Add claimId as form data
				.formParam("lastName", prop.getProperty("LastName")) // Add lastName as form data
				.formParam("dateOfBirth", prop.getProperty("dateOfBirth")) // Add dateOfBirth as form data
				.post(endpoint);

	}


	@When("a POST request is sent with form data for UNCD Unnumbered")
	public void aPOSTRequestIsSentWithFormDataForUNCDD() {
		String bearerToken = prop.getProperty("BearerToken");
		File file = new File(prop.getProperty("Filepath")); // Path to the file
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Authorization", "Bearer " + bearerToken)
				.header("Content-Type", "multipart/form-data")
				.multiPart("file", file) // Attach the file
				.post(endpoint);

	}

	@Given("the UNCD Token endpoint is configured with {string}")
	public void theUNCDTokenEndpointIsConfiguredWith(String arg0) {

		endpoint = prop.getProperty("tokenBaseUrl") + arg0;

	}

	@When("the token request body has set up")
	public void theTokenRequestBodyHasSetUp() {

		requestBody =
				"""
						client_id=05feb79d-a0b7-4096-9f58-5e8ce03ba7b3&redirect_uri=https%3A%2F%2Fppd.documentupload.wsib.ca%2Fauth-callback&scope=openid%20offline_access%20openid%20offline_access%20https%3A%2F%2Fwsibb2cppd.onmicrosoft.com%2Fuduapi%2FUDU_API_ACCESS%20profile&code=eyJraWQiOiJjcGltY29yZV8wOTI1MjAxNSIsInZlciI6IjEuMCIsInppcCI6IkRlZmxhdGUiLCJzZXIiOiIxLjAifQ..DpDVqT4LT1s9831R.ffqH9Ze2x-JJaOgwxJQjT6ijkg-HrV3dUdMhvI3pZs1m5Ho-HDlZC7kZOSjuYDP-Hw40Gacp1047fsckvmuMexrY-Znn4iry12GRm8IXgY1qDuEnAzXuNfoiiGYdtJL8BhOzr_DULMn6xbRlUGvZTxHnZdxxTSWEiBDPnMbXXNI8UCbZKiCUk43V_OSwG7qON1_AK40IXRbAEsxhDfWi4N7gYL2VxPQKTOJH2bimF619jZ-PYoPrKoZP4gKmnMIIcfw9LfNyr2vKhRe84fmQYBv7t6f4XBlt0WprfnwHoMPvAxM--TOYl-jnqEtme0MBXQ7keMaQ3L4zpbUK40CP72EqOsBtMZ8R1tdOCNrtwD6RBLCueP6OFUDiW1Z0JX1L-MJY_t1HUIoetiPEi1VjWVPoMvXoHmgC9nhVA9vTGs_iIDEHjCaJ14jtHSGyMPmEeayABpFSVLjyy_z_Akv87e7aJ5CZg0aK4Q1tDbuyfHxOUNZ9kXmzAVyPza-MWlMLABdhg-Z9vgLyZI8T6z13eq6sUz2ch_s9MGLR8T70HszkyQeHv99TU1GMHewfb7oUbNUA9rNB8ZVzQS1vwW_1tcMGt-gBSKpylkzjYvUoJh1i-VVzZdqrBHKrmzUJ84NyH5Uffaws2rI8ofEWKLq9vFBN73lkSCJuG74JaF-EcpleTSlahUeUskjmKdD4ULpGmFJEhrDQ4jYBYIkhDEioxru04TLRqg4pMohIZFHUJqcDtiqOf7pNEslFTBSz9OynywBBSsLd02NH1j3FSjXU2qqAnoFrl0_xokZS2JBh-Vrd3UB95Qla--SfCPrhc9ZBqaiT5ns59gbuy6oe3F22tFHokrsNyfTOVQOhXw.irXwfNz8B60GU3-vaDVExw&x-client-SKU=msal.js.browser&x-client-VER=2.39.0&x-ms-lib-capability=retry-after, h429&x-client-current-telemetry=5|865,0,,,|@azure/msal-react,1.5.13&x-client-last-telemetry=5|0|||0,0&code_verifier=B6TWMTb5T-kTTcKTYX_pI13K_inK4A3ag-LDfrfpmf4&grant_type=authorization_code&client_info=1&client-request-id=b442c497-c11f-4f5f-9203-db26cda8c6af&X-AnchorMailbox=Oid%3A1185294c-3a89-4ef5-978a-dc36ca4688a2-b2c_1_udu_signinup%4073ffa90f-3528-4be6-bbd7-e35b29788a57
						 """;


	}

	@Then("Capture the token value from response")
	public void captureTheTokenValueFromResponse() throws Exception {

		String BearerToken = response.jsonPath().getString("access_token");
		Assert.assertNotNull("BearerToken should not be null", BearerToken);
		prop.setProperty("BearerToken", BearerToken, Filepath); // Store the fileId in config.properties
		System.out.println("BearerToken : " + BearerToken);

	}

	@And("a UNCD Token POST request is sent")
	public void aUNCDTokenPOSTRequestIsSent() {
		RestAssured.useRelaxedHTTPSValidation();
		String bearerToken = prop.getProperty("BearerToken");
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
				.body(requestBody)
				.post(endpoint);

	}
}
