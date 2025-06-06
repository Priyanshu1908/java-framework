package com.priyanshu.sanity_tests;

import com.priyanshu.data.pom.google.GoogleWebPage;
import com.priyanshu.lib.BaseTest;
import com.priyanshu.model.TestEvidence;
import com.priyanshu.model.TestStatus;
import org.testng.annotations.Test;

import static com.priyanshu.lib.Utilities.TryAssert;

@Test(groups = "Web")
public class GoogleWebTest extends BaseTest {

    public void webTest() {
        getReport().TestData.Description = "Verify Web Test";
        GoogleWebPage test = new GoogleWebPage(Driver);
        getReport().TestData.Url = test.Url;
        test.Search("test");

        var status = TryAssert(() -> Assert.assertTrue(Driver.getCurrentUrl().contains("google")));
        getReport().AddEvidence(new TestEvidence() {{
            Expected = "Verify Web Test";
            Actual = "Url did" + (status == TestStatus.Passed ? " " : " not ") + "contains String";
            StepStatus = status;
            Details = "Validate web test";
            Screenshot = GetScreenshot();
            StepName = "Test web";
            TestType = com.priyanshu.model.TestType.Web;
        }});
    }
}
