package tests;

import dev.failsafe.Timeout;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Test(groups = "Web")
public class AlertPracticePageTest extends BaseTest {

    @Test
    public void navigateToUrl() {
        driverManager.navigateToUrl("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test(priority = 1)
    public void enablePopup() throws InterruptedException {
        driver.findElement(By.name("enter-name")).sendKeys("Sam");
        driver.findElement(By.id("alertbtn")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        Thread.sleep(2000);
        Alert alert = driver.switchTo().alert();
        System.out.println(alert.getText());
        alert.accept();
        Thread.sleep(2000);
        driver.findElement(By.name("enter-name")).sendKeys("Sam");
        driver.findElement(By.id("confirmbtn")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        System.out.println(alert.getText());
        Thread.sleep(2000);
        alert.dismiss();
    }
}
