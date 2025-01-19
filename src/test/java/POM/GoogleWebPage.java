package POM;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GoogleWebPage {

    public void searchText(WebDriver driver) {

        String searchBoxPath = "//textarea[@name=\"q\"]";
        driver.findElement(new By.ByXPath(searchBoxPath)).sendKeys("Test");
    }
}
