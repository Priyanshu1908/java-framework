package com.priyanshu.sanity_tests;

import com.priyanshu.data.api.PayLoad;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JiraApiTest {

    static String ID;

    @Test()
    public static void createIssue() {

        RestAssured.baseURI = "https://rathoresam101.atlassian.net";
        String response = given().header("Content-Type", "application/json")
                .header("Authorization", "Basic cmF0aG9yZXNhbTEwMUBnbWFpbC5jb206QVRBVFQzeEZmR0YwNEFYOGNUcWxiTXZzbFRPTmI4NW5sUDRTNzFqLU1tdjhiejJhV2RUcVBXeVA0QS1xdy1qQmxSTk1xemdJT01UaTZUaTM4cnR0V3p2OFc5Yl8zalFFMUU3NWw3MURrZGpfVnhraG54TU9rY3VsUVcweUF4akRUaFN6VHRMc3dhTkNEYkQwLTlBT2c5bXVaRFJENm9SRE5fZUZVRmdadFFKdXJhS1dyWVcxOEQ4PUUwQkQ2MjY3")
                .body(PayLoad.createIssue())
                .when().post("/rest/api/3/issue")
                .then().log().all().assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        ID = js.get("id");
    }

    @Test(priority = 1)
    public static void addAttachment() {

        File fs = new File("C:/Users/priya/OneDrive/Pictures/Screenshots/Screenshot 2024-06-10 152254.png");

        given().pathParam("key", ID).header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic cmF0aG9yZXNhbTEwMUBnbWFpbC5jb206QVRBVFQzeEZmR0YwNEFYOGNUcWxiTXZzbFRPTmI4NW5sUDRTNzFqLU1tdjhiejJhV2RUcVBXeVA0QS1xdy1qQmxSTk1xemdJT01UaTZUaTM4cnR0V3p2OFc5Yl8zalFFMUU3NWw3MURrZGpfVnhraG54TU9rY3VsUVcweUF4akRUaFN6VHRMc3dhTkNEYkQwLTlBT2c5bXVaRFJENm9SRE5fZUZVRmdadFFKdXJhS1dyWVcxOEQ4PUUwQkQ2MjY3")
                .multiPart("file", fs)
                .when().post("/rest/api/3/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200);
    }
}
