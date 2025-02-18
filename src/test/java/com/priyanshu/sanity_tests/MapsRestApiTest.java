package com.priyanshu.sanity_tests;

import com.priyanshu.data.pom.api.PayLoad;
import com.priyanshu.lib.ApiReportGenerator;
import com.priyanshu.lib.BaseTest;
import com.priyanshu.model.Api;
import com.priyanshu.model.TestEvidence;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.priyanshu.lib.Utilities.TryAssert;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Test(groups = "Api")
public class MapsRestApiTest extends BaseTest {

    private static final String Url = "https://rahulshettyacademy.com";
    public static String placeID;
    public static String newAddress = "New Cross Street, USA";

    public void restTest() throws Exception {

        RestAssured.baseURI = Url;

        //Create a place
        String response = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(PayLoad.addPlace()).when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Content-Type", "application/json;charset=UTF-8").extract().response().asString();

        JsonPath js = new JsonPath(response);
        placeID = js.getString("place_id");

        System.out.println(placeID);

        //Update a place
        String resp = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(PayLoad.updatePlace()).when().put("/maps/api/place/update/json")
                .then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"))
                .extract().response().asString();

        //Retrieve a place
        String res = given().queryParam("key", "qaclick123").queryParam("place_id", placeID).header("Content-Type", "application/json")
                .when().get("/maps/api/place/get/json").then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsp = new JsonPath(res);
        String address = jsp.getString("address");

        System.out.println(address);

        var status = TryAssert(() -> Assert.assertEquals(address, newAddress));

        //Delete a place
        Response res1 = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(PayLoad.deletePlace()).when().delete("/maps/api/place/delete/json");

        getReport().AddEvidence(new TestEvidence() {{
            Expected = "Verify Rest Test";
            Actual = "Status code was " + res1.statusCode();
            StepStatus = status;
            Details = "Validate rest test";
            EmbeddedFiles = List.of(ApiReportGenerator.GetPath(getReport()));
            StepName = "Check response code";
            TestType = com.priyanshu.model.TestType.Api;
            Api = new Api() {
                {
                    Name = "Rest Api test";
                    ResponseCode = Integer.toString(res1.statusCode());
                    RequestUrl = Url;
                    Client = "RestAssured";
                    ResponseContent = res1.toString();
                    ResponseHeaders = res1.getHeaders().asList().stream().map(h -> h.getName() + ":" + h.getValue())
                            .collect(Collectors.toList());
                    RequestMethod = Method.GET.toString();
                }
            };
        }});
    }
}
