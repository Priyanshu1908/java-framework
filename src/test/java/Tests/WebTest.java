package Tests;

import POM.WebPage;
import org.testng.annotations.Test;

@Test
public class WebTest {

    public void webTest(){
        WebPage test = new WebPage();
        test.navigateToUrl();
        test.searchText();
        test.closeBrowser();
    }
}
