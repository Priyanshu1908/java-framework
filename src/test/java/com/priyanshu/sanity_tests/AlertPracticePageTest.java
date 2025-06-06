package com.priyanshu.sanity_tests;

import com.priyanshu.lib.BaseTest;
import com.priyanshu.model.TestEvidence;
import com.priyanshu.model.TestStatus;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.priyanshu.lib.Utilities.TryAssert;

@Test(groups = "Web")
public class AlertPracticePageTest extends BaseTest {

    public final String Url = "https://rahulshettyacademy.com/AutomationPractice/";
    @Test()
    public void enablePopup() throws InterruptedException {
        WebDriver driver = Driver;
        getReport().TestData.Description = "Verify Alert";
        getReport().TestData.Url = Url;
        driver.navigate().to(Url);
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

        var status = TryAssert(() -> Assert.assertTrue(Driver.getTitle().contains("Practice")));
        getReport().AddEvidence(new TestEvidence() {{
            Expected = "Verify Alert";
            Actual = "Title did" + (status == TestStatus.Passed ? " " : " not ") + "contains String";
            StepStatus = status;
            Details = "Validate Alert";
            Screenshot = GetScreenshot();
            StepName = "Test web";
            TestType = com.priyanshu.model.TestType.Web;
        }});
    }
}
