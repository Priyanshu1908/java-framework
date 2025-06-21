package com.priyanshu.lib;

import com.priyanshu.model.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.time.Duration;
import java.util.HashMap;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

//    public static WebDriver getDriver() {
//        return driver.get();
//    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static WebDriver GetDriver(Browser browser, boolean isHeadlessExecution) throws Exception {

        WebDriver driver = switch (browser) {
            case Chrome -> {
                var chromeOptions = setChromeOptions(isHeadlessExecution);
                yield new ChromeDriver(chromeOptions);
            }
            case Firefox -> {
                var firefoxOptions = setFirefoxOptions(isHeadlessExecution);
                yield new FirefoxDriver(firefoxOptions);
            }
            case Edge -> {
                var edgeOptions = setEdgeOptions(isHeadlessExecution);
                yield new EdgeDriver(edgeOptions);
            }
            default -> throw new Exception("Browser not defined");
        };
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        setDriver(driver);
        return driver;
    }

    public static ChromeOptions setChromeOptions(boolean isHeadlessExecution) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", setChromiumPreferences());
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--start-maximized");
        if (Boolean.parseBoolean(BaseTest.getConfig().getProperty("enableIncognitoMode")))
            chromeOptions.addArguments("--incognito");
        if (isHeadlessExecution)
            chromeOptions.addArguments("--headless");
        return chromeOptions;
    }

    public static FirefoxOptions setFirefoxOptions(boolean isHeadlessExecution) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        var profile = new FirefoxProfile();
        profile.setPreference("pdffjs.disabled", true);
        profile.setPreference("browser.helperApps.nerverAsk.saveToDisk",
                "application/download, application/octet-stream, text/csv, " +
                        "application/pdf,application/zip,image/png");
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", BaseTest.getConfig().getProperty("defaultDownloadPath"));
        profile.setPreference("browser.download.useDownloadDir", true);
        profile.setPreference("dom.disable_open_during_load", Boolean.
                parseBoolean(BaseTest.getConfig().getProperty("allowPopups")));
        firefoxOptions.setProfile(profile);
//        firefoxOptions.addArguments("--width=1536","--height=864");
        firefoxOptions.addArguments("--kiosk");
        if (Boolean.parseBoolean(BaseTest.getConfig().getProperty("enableIncognitoMode")))
            firefoxOptions.addArguments("--private");
        if (isHeadlessExecution)
            firefoxOptions.addArguments("--headless");
        return firefoxOptions;
    }

    public static EdgeOptions setEdgeOptions(boolean isHeadlessExecution) {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setExperimentalOption("prefs", setChromiumPreferences());
        edgeOptions.addArguments("--no-sandbox");
        edgeOptions.addArguments("--start-maximized");
        if (Boolean.parseBoolean(BaseTest.getConfig().getProperty("enableIncognitoMode")))
            edgeOptions.addArguments("--inprivate");
        if (isHeadlessExecution)
            edgeOptions.addArguments("--headless");
        return edgeOptions;
    }

    public static HashMap<Object, Object> setChromiumPreferences() {
        var chromiumPreferences = new HashMap<>();
        chromiumPreferences.put("download.prompt_for_download", false);
        chromiumPreferences.put("download.default_diredctory", getDefaultDownloadPath());
        if (Boolean.parseBoolean(BaseTest.getConfig().getProperty("allowPopups")))
            chromiumPreferences.put("profile.managed_default_content_settings.popups", 1);
        else
            chromiumPreferences.put("profile.managed_default_content_settings.popup", 2);
        return chromiumPreferences;
    }

    public static String getDefaultDownloadPath() {
        var configPath = BaseTest.getConfig().getProperty("defaultDownloadPath");
        return !configPath.isEmpty() ? configPath : System.getProperty("user.home") + "/Downloads";
    }
}
