package lib;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {

    WebDriver driver;

    public WebDriver launchBrowser(String browserName) {

        switch (browserName) {
            case "Chrome":
                driver = new ChromeDriver();
                break;
            case "Firefox":
                driver = new FirefoxDriver();
                break;
            case "Edge":
                driver = new EdgeDriver();
                break;
            default:
                new Exception("Browser not defined");
                break;
        }
        return driver;
    }

    public void navigateToUrl(String Url) {

        driver.manage().window().maximize();
        driver.get(Url);
    }

    public void closeBrowser() {

        driver.quit();
    }
}
