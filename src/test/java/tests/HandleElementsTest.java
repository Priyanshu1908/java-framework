package tests;

import POM.FlightsBookingPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = "Web")
public class HandleElementsTest extends BaseTest {

    FlightsBookingPage page = new FlightsBookingPage();

    @Test()
    public void navigateToUrl() {
        driverManager.navigateToUrl("https://rahulshettyacademy.com/dropdownsPractise/");
    }

    @Test(priority = 1)
    public void currencyDropdownTest() {

        WebElement currencyDropdown = page.currencyDropDownElement(driver);
        Select dropdown = new Select(currencyDropdown);
        dropdown.selectByIndex(3);
        String firstSelectedValue = dropdown.getFirstSelectedOption().getText();
        System.out.println("First selected value of currency is: " + firstSelectedValue);
        dropdown.selectByValue("AED");
        String secondSelectedValue = dropdown.getFirstSelectedOption().getText();
        System.out.println("Second selected value of currency is: " + secondSelectedValue);
        dropdown.selectByVisibleText("INR");
        String thirdSelectedValue = dropdown.getFirstSelectedOption().getText();
        System.out.println("Third selected value of currency is: " + thirdSelectedValue);
    }

    @Test(priority = 2)
    public void passengerDropDownTest() {

        System.out.println("Select passengers count");
        WebElement passengers = page.passengersDropDownElement(driver);
        passengers.click();
        WebElement adultCount = page.addAdultCount(driver);
        for (int i = 0; i < 2; i++) {
            adultCount.click();
        }
        WebElement doneButton = page.doneButton(driver);
        doneButton.click();
        String updatedCount = passengers.getText();
        System.out.println("New passengers count is: " + updatedCount);
    }

    @Test(priority = 3)
    public void selectCities() {

        WebElement fromCity = page.fromCity(driver);
        fromCity.click();
        driver.findElement(By.xpath("//a[contains(@text,'DEL')]")).click();
        System.out.println("From City: " + fromCity.getDomAttribute("value"));
        driver.findElement(By.xpath("(//a[contains(@text,'BLR')])[2]")).click();
        WebElement toCity = page.toCity(driver);
        System.out.println("To City: " + toCity.getDomAttribute("value"));
    }

    @Test(priority = 4)
    public void selectFromDate() {

        WebElement fromDate = driver.findElement(By.cssSelector("a[class*='ui-state-active']"));
        fromDate.click();
    }

    @Test(priority = 5)
    public void selectCountry() {

        WebElement countryValue = page.typeToSelectCountry(driver);
        countryValue.sendKeys("Ind");
        List<WebElement> values = driver.findElements(By.cssSelector("li[class='ui-menu-item'] a"));
        for (WebElement value : values) {
            if (value.getText().equalsIgnoreCase("India")) {
                Assert.assertEquals(value.getText(), "India");
                value.click();
                break;
            }
        }
        System.out.println("Selected Country");
    }

    @Test(priority = 6)
    public void selectCheckboxes() {
        WebElement seniorCitizenCheckbox = driver.findElement(By.cssSelector("input[id*='SeniorCitizenDiscount']"));
        System.out.println("Checkbox selected: " + seniorCitizenCheckbox.isSelected());
        Assert.assertFalse(seniorCitizenCheckbox.isSelected());
        seniorCitizenCheckbox.click();
        System.out.println("Checkbox selected: " + seniorCitizenCheckbox.isSelected());
        Assert.assertTrue(seniorCitizenCheckbox.isSelected());

        List<WebElement> allCheckboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        System.out.println("Checkboxes count is: " + allCheckboxes.size());
        Assert.assertEquals(allCheckboxes.size(), 6);
    }
}
