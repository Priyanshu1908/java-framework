package tests;

import POM.GoogleWebPage;
import org.testng.annotations.Test;

@Test(groups = "Web")
public class GoogleWebTest extends BaseTest{

    @Test
    public void webTest() throws InterruptedException {
        GoogleWebPage test = new GoogleWebPage();
        driverManager.navigateToUrl("https://www.google.com/");
        test.searchText(driver);
        Thread.sleep(1000);
    }
}
