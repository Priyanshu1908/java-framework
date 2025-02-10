package com.priyanshu.lib;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.priyanshu.model.TestStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TimeZone;

public class HtmlReportGenerator implements IReportGenerator {

    private static final String HtmlPath = "/HTML_Reports";
    private TestReport _testReport;
    private static final String Sep = File.separator;

    public IReportGenerator Prepare(TestReport testReport) throws Exception {
        _testReport = testReport;
        Utilities.createFolder(_testReport.GetOutputPath() + HtmlPath);
        return this;
    }

    public void RenderAndSave() throws Exception {
        var outputPath = _testReport.GetOutputPath() + HtmlPath;
        var extentReports = new ExtentReports();
        ExtentSparkReporter html = new ExtentSparkReporter(outputPath + Sep + "index.html");
        extentReports.attachReporter(html);
        extentReports.setSystemInfo("Executed By", System.getProperty("user.name"));
        extentReports.setSystemInfo("Time Zone", TimeZone.getDefault().getDisplayName());
        extentReports.setSystemInfo("Executed on", Utilities.getHostName());
        extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
        var extentTest = extentReports.createTest(_testReport.TestData.ClassName);
        for (Map.Entry<String, String> entry : _testReport.TestData.CustomRows.entrySet()) {
            extentReports.setSystemInfo(entry.getKey(), entry.getValue());
        }

        for (var evidence : _testReport.TestData.TestEvidences) {
            StringBuilder customRowData = new StringBuilder();
            for (Map.Entry<String, String> entry : evidence.CustomRows.entrySet()) {
                customRowData.append(entry.getKey()).append(": ").append(entry.getValue()).append("<br>");
            }
            extentTest.log(TestStatus2AvantStatus(evidence.StepStatus),
                    "Expected Result: " + evidence.Expected + "<br>" +
                            "Actual Result: " + evidence.Actual + "<br>" + customRowData);

        }
        extentReports.flush();
        try {
            Files.deleteIfExists(Path.of(GetPath(_testReport)));
            Files.move(Path.of(outputPath + Sep + "index.html"), Path.of(GetPath(_testReport)));
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    private static Status TestStatus2AvantStatus(TestStatus status) throws Exception {
        var AvantStatus = Status.FAIL;
        switch (status) {
            case Passed:
                AvantStatus = Status.PASS;
                break;
            case Ignored:
                AvantStatus = Status.SKIP;
                break;
            default:
                throw new Exception("Invalid Status");
        }
        return AvantStatus;
    }

    public static String GetPath(TestReport report) throws Exception {
        var outputPath = report.GetOutputPath() + HtmlPath;
        var fileName = report.TestData.ClassName + report.TestData.TestParam + ".html";
        return outputPath + Sep + fileName;
    }

}
