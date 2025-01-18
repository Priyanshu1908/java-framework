package tests;

import lib.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    WebDriver driver;
    DriverManager driverManager = new DriverManager();

    @BeforeSuite
    public void beforeSuite(){
        driver = driverManager.launchBrowser("Chrome");
    }

    @BeforeMethod
    public void beforeMethod(){}

    @AfterMethod
    public void afterMethod(){}

    @AfterSuite
    public void afterSuite(){
        driverManager.closeBrowser();
    }
}
