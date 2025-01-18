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
        System.out.println("Before Suite");
        driver = driverManager.launchBrowser("Chrome");
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("Before Method");
    }

    @AfterMethod
    public void afterMethod(){
        System.out.println("After Method");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("After Suite");
        driverManager.closeBrowser();
    }
}
