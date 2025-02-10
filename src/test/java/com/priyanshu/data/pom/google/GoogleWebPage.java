package com.priyanshu.data.pom.google;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GoogleWebPage {

    private final WebDriver driver;
    public final String Url = "https://www.google.com";
    public WebElement Query(){
        return this.driver.findElement(By.name("q"));
    }

    public GoogleWebPage(WebDriver driver) {
        this.driver = driver;
        driver.navigate().to(this.Url);
    }

    public void Search(String query){
        Query().sendKeys(query);
    }
}
