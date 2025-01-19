package POM;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightsBookingPage {

    public WebElement currencyDropDownElement(WebDriver driver) {

        return driver.findElement(By.xpath("//div[@class=\"currency-dropdown\"]//select"));
    }

    public WebElement passengersDropDownElement(WebDriver driver) {

        return driver.findElement(By.id("divpaxinfo"));
    }

    public WebElement addAdultCount(WebDriver driver) {

        return driver.findElement(By.id("hrefIncAdt"));
    }

    public WebElement doneButton(WebDriver driver) {

        return driver.findElement(By.id("btnclosepaxoption"));
    }

    public WebElement fromCity(WebDriver driver) {
        return driver.findElement(By.cssSelector("#ctl00_mainContent_ddl_originStation1_CTXT"));
    }

    public WebElement toCity(WebDriver driver) {
        return driver.findElement(By.cssSelector("#ctl00_mainContent_ddl_destinationStation1_CTXT"));
    }

    public WebElement typeToSelectCountry(WebDriver driver) {

        return driver.findElement(By.id("autosuggest"));
    }

}
