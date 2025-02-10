//package com.priyanshu.sanity_tests;
//
//import com.priyanshu.lib.BaseTest;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Test(groups = "Web")
//public class AddItemsToCart extends BaseTest {
//
//    public void addVeggies() {
//
//        driverManager.navigateToUrl("https://rahulshettyacademy.com/seleniumPractise/#/");
//
//        String[] veggies = {"Brocolli", "Cucumber", "Tomato"};
//        List<String> vegToAdd = Arrays.asList(veggies);
//        List<WebElement> vegOptions = driver.findElements(By.className("product-name"));
//        int count = 0;
//
//        for (int i = 0; i < vegOptions.size(); i++) {
//
//            String vegName = vegOptions.get(i).getText();
//            String[] vegs = vegName.split("-");
//            String veg = vegs[0].trim();
//
//            if (vegToAdd.contains(veg)) {
//
//                count++;
//                driver.findElements(By.xpath("//button[text()='ADD TO CART']")).get(i).click();
//
//                if (count == vegToAdd.size()) {
//                    break;
//                }
//            }
//        }
//        String totalItems =  driver.findElement(By.xpath("//td[text()='Items']  //following-sibling::td //strong")).getText();
//        Assert.assertEquals(Integer.parseInt(totalItems),vegToAdd.size());
//    }
//
//}
