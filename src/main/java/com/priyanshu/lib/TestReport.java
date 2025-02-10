package com.priyanshu.lib;

import com.priyanshu.model.TestData;
import com.priyanshu.model.TestEvidence;
import com.priyanshu.model.TestManagementData;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

public class TestReport {
    private String _outputPath;
    public TestData TestData;

    public TestReport(TestData data) {
        TestData = data;
    }

    public TestReport SetOutputPath(String path) {
        _outputPath = path;
        return this;
    }

    public String GetOutputPath() throws Exception {
        if (StringUtils.isBlank(_outputPath))
            throw new Exception("Report output path is not set");
        return _outputPath;
    }

    public static TestManagementData ReadAlmFile(String almInputPath, Properties config){
        File dir = new File(almInputPath);
        FileFilter fileFilter = new WildcardFileFilter("*.txt");
        File[] files = dir.listFiles(fileFilter);
        if(files == null || files.length ==0) return null;
        try{
            List<String> allLines = Files.readAllLines(files[0].toPath());
            var almData = new TestManagementData();
            almData.Server = "https://alm/qcbin/";
            almData.TestCaseId = allLines.get(0);
            almData.TestCaseName = allLines.get(1);
            almData.TestCaseVersion = allLines.get(2);
            almData.TestSetId = allLines.get(3);
            almData.TestSetName = allLines.get(4);
            almData.CurrentRunId = allLines.get(5);
            almData.CurrentRunName = allLines.get(6);
            almData.ProjectName = allLines.get(7);
            almData.TestCaseDescription = allLines.get(8);

            return almData;
        } catch (Exception e){
            return null;
        }
    }

    public TestReport AddEvidence(TestEvidence evidence){
        TestData.TestCaseStatus = evidence.StepStatus;
        TestData.Actual = evidence.Actual;
        TestData.TestEvidences.add(evidence);
        return this;
    }
}

