package tests;

import POM.GoogleWebPage;
import org.testng.annotations.Test;

@Test
public class WebTest {

    public void webTest(){
        GoogleWebPage test = new GoogleWebPage();
        test.navigateToUrl();
        test.searchText();
        test.closeBrowser();
    }
}
