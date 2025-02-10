package com.priyanshu.lib;

import com.priyanshu.model.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class DriverManager {

    public static WebDriver GetDriver(Browser browser, boolean isHeadlessExecution) throws Exception {

        WebDriver driver;

        switch (browser) {
            case Chrome:
                driver = new ChromeDriver();
                break;
            case Firefox:
                driver = new FirefoxDriver();
                break;
            case Edge:
                driver = new EdgeDriver();
                break;
            default:
                throw new Exception("Browser not defined");
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        return driver;
    }

    public static void getDefaultDownloadPath(){

    }

}
