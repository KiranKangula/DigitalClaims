package testRunner;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTest {
    private static int attempt = 1;

    @Test
    public void testRetry() {
        System.out.println("Test attempt: " + attempt);
        Assert.assertTrue(attempt++ > 2, "Failing test to trigger retry");
    }
}
