package tests;

import POM.FlightsBookingPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;


@Test
public class HandleStaticDropdownTest extends BaseTest{

    FlightsBookingPage page;
    @Test(priority = 0)
    public void currencyDropdownTest(){

        driverManager.navigateToUrl("https://rahulshettyacademy.com/dropdownsPractise/");

        page = new FlightsBookingPage();
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

    @Test(priority = 1)
    public void passengerDropDownTest(){

        System.out.println("Select passengers count");
        WebElement passengers = page.passengersDropDownElement(driver);
        passengers.click();
        WebElement adultCount = page.addAdultCount(driver);
        for(int i=0;i<2;i++) {
            adultCount.click();
        }
        WebElement doneButton = page.doneButton(driver);
        doneButton.click();
        String updatedCount = passengers.getText();
        System.out.println("New passengers count is: " + updatedCount);
    }
}
