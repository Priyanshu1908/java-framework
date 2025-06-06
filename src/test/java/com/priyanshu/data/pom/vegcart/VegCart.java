package com.priyanshu.data.pom.vegcart;

import org.openqa.selenium.WebDriver;

public class VegCart {

    private final WebDriver driver;

    public final String Url = "https://rahulshettyacademy.com/seleniumPractise/#/";

    public VegCart(WebDriver driver){
        this.driver = driver;
        driver.navigate().to(Url);
    }
}
