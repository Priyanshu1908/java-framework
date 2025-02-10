package com.priyanshu.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyanshu.model.TestData;
import com.priyanshu.model.TestType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class JsonReportGenerator implements IReportGenerator {

    private static final String JsonPath = "/JSON_Reports";
    private TestReport _testReport;
    private static final String Sep = File.separator;

    @Override
    public IReportGenerator Prepare(TestReport testReport) throws Exception {
        _testReport = testReport;
        Utilities.createFolder(_testReport.GetOutputPath());
        return null;
    }

    @Override
    public void RenderAndSave() throws IOException {
        Map<String, Object> testDataParameters = new LinkedHashMap<>();

        testDataParameters.put("AutomationFramework", "Torchbearer Java Framework (V" + _testReport.TestData.TestToolVersion + ")");
        testDataParameters.put("TechnologyUsed", "Selenium " + _testReport.TestData.SeleniumVersion + " (UI), "
                + "RestAssured " + _testReport.TestData.RestSharpVersion + " (API)");
        testDataParameters.put("Browser", IsWebTest(_testReport.TestData) ? _testReport.TestData.Browser.toString() : "-");
        testDataParameters.put("URL", _testReport.TestData.Url != null ? _testReport.TestData.Url : "-");
        testDataParameters.put("OperatingSystem", _testReport.TestData.Os);
        testDataParameters.put("TestCaseName", _testReport.TestData.Name);
        testDataParameters.put("TestCaseDescription", _testReport.TestData.Description);
        testDataParameters.put("TimeZone", TimeZone.getDefault().getDisplayName());
        testDataParameters.put("StartTime", _testReport.TestData.StartTime);
        testDataParameters.put("EndTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        testDataParameters.put("ExecutedBy", System.getProperty("user.name"));
        testDataParameters.put("ExecutedOn", _testReport.TestData.ExecutedOn != null ? _testReport.TestData.ExecutedOn : "");
        testDataParameters.put("OverallStatus", _testReport.TestData.TestCaseStatus.toString().toUpperCase());


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter jsonFile = new FileWriter(GetPath(_testReport))) {
            gson.toJson(testDataParameters, jsonFile);
        } catch (Exception e) {
            System.err.println("Error while creating JSON file: " + e.getMessage());
        }
    }

    private Boolean IsWebTest(TestData testData) {
        return testData.TestType.equals(TestType.Web) || testData.TestType.equals(TestType.WebApi);
    }

    public static String GetPath(TestReport report) throws Exception {
        var outputPath = report.GetOutputPath() + JsonPath;
        var fileName = report.TestData.ClassName + report.TestData.TestParam + ".json";
        return outputPath + Sep + fileName;
    }


}
