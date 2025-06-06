package com.priyanshu.sanity_tests;

import com.priyanshu.data.pom.vegcart.VegCart;
import com.priyanshu.lib.BaseTest;
import com.priyanshu.model.TestEvidence;
import com.priyanshu.model.TestStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.priyanshu.lib.Utilities.TryAssert;

@Test(groups = "Web")
public class GreenKartCartTest extends BaseTest {

    @Test
    public void addVeggies() {

        getReport().TestData.Description = "Verify UI Element test";
        VegCart vegCart = new VegCart(Driver);
        getReport().TestData.Url = vegCart.Url;

        String[] veggies = {"Brocolli", "Cucumber", "Tomato"};
        List<String> vegToAdd = Arrays.asList(veggies);
        List<WebElement> vegOptions = Driver.findElements(By.className("product-name"));
        int count = 0;

        for (int i = 0; i < vegOptions.size(); i++) {

            String vegName = vegOptions.get(i).getText();
            String[] vegs = vegName.split("-");
            String veg = vegs[0].trim();

            if (vegToAdd.contains(veg)) {

                count++;
                Driver.findElements(By.xpath("//button[text()='ADD TO CART']")).get(i).click();

                if (count == vegToAdd.size()) {
                    break;
                }
            }
        }
        String totalItems =  Driver.findElement(By.xpath("//td[text()='Items']  //following-sibling::td //strong")).getText();
        Assert.assertEquals(Integer.parseInt(totalItems),vegToAdd.size());

        var status = TryAssert(() -> Assert.assertTrue(Driver.getTitle().contains("GreenKart")));
        getReport().AddEvidence(new TestEvidence() {{
            Expected = "Verify GreenKart site";
            Actual = "Title did" + (status == TestStatus.Passed ? " " : " not ") + "contains String";
            StepStatus = status;
            Details = "Validate GreenKart site";
            Screenshot = GetScreenshot();
            StepName = "Test web";
            TestType = com.priyanshu.model.TestType.Web;
        }});
    }

}
