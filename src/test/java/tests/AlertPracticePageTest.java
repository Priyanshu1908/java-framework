package tests;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

@Test
public class AlertPracticePageTest extends BaseTest {

    @Test
    public void navigateToUrl() {
        driverManager.navigateToUrl("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test(priority = 1)
    public void enablePopup() throws InterruptedException {
        driver.findElement(By.name("enter-name")).sendKeys("Sam");
        driver.findElement(By.id("alertbtn")).click();
        Thread.sleep(2000);
        Alert alert = driver.switchTo().alert();
        System.out.println(alert.getText());
        alert.accept();
        Thread.sleep(2000);
        driver.findElement(By.name("enter-name")).sendKeys("Sam");
        driver.findElement(By.id("confirmbtn")).click();
        System.out.println(alert.getText());
        Thread.sleep(2000);
        alert.dismiss();
    }
}
