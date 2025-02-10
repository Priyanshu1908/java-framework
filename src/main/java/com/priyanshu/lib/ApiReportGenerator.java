package com.priyanshu.lib;

import org.apache.commons.io.output.WriterOutputStream;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ApiReportGenerator implements IReportGenerator{

    private static final String ApiPath = "/API_Reports";
    private TestReport _testReport;
    private static final String Sep = File.separator;

    @Override
    public IReportGenerator Prepare(TestReport testReport) throws Exception {
        _testReport= testReport;
        Utilities.createFolder(_testReport.GetOutputPath() + ApiPath);
        return this;
    }

    @Override
    public void RenderAndSave() throws Exception {
        PrintStream sw;
        sw = new PrintStream(new WriterOutputStream(new FileWriter(GetPath(_testReport)),
                StandardCharsets.UTF_8), true);

        for (var evidence : _testReport.TestData.TestEvidences) {
            if(evidence.Api == null) continue;
            sw.println("============================================================");
            sw.println("Request Details");
            sw.println("============================================================");
            sw.println("Name:               " + evidence.Api.Name);
            sw.println("Step Name:          " + evidence.StepName);
            sw.println("Method:             " + evidence.Api.RequestMethod);
            sw.println("Client:             " + evidence.Api.Client);
            sw.println();
            sw.println("============================================================");
            sw.println("Response Details");
            sw.println("============================================================");
            sw.println("Uri:                " + evidence.Api.RequestUrl);
            sw.println("StatusCode:         " + evidence.Api.ResponseCode);
            sw.println("StatusDescription:  " + evidence.Api.ResponseCode);
            sw.println("Headers:            " + evidence.Api.ResponseHeaders);
            sw.println("Content:            " + evidence.Api.ResponseContent);
        }
    }

    public static String GetPath(TestReport report) throws Exception {
        var outputPath = report.GetOutputPath() + ApiPath;
        var fileName = report.TestData.ClassName + report.TestData.TestParam + ".txt";
        return outputPath + Sep + fileName;
    }
}
