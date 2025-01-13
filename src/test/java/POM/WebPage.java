package POM;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebPage {

    WebDriver driver;

    String url = "https://www.google.com/";

    public void navigateToUrl() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
    }

    public void searchText(){

        String searchBoxPath = "//textarea[@name=\"q\"]";
        driver.findElement(new By.ByXPath(searchBoxPath)).sendKeys("Test");

    }

    public void closeBrowser(){
        driver.close();
    }
}
