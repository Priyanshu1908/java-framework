package com.priyanshu.lib;

import io.cucumber.java.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CucumberHook {
    private static BaseTest baseTest;

    @BeforeAll
    public static void beforeAll(){}

    @AfterAll
    public static void afterAll() throws IOException {
        baseTest.afterSuite();
    }

    @Before
    public void before(@NotNull Scenario scenario) throws Exception {
        baseTest = new BaseTest();
        baseTest.beforeSuite();
        baseTest.beforeMethod(Utilities.getName(scenario),scenario.getName(),Utilities.getTestType(scenario).toString(),null);
    }

    @After
    public void after(Scenario scenario) throws Exception {
        baseTest.afterMethod();
    }
}
