package com.priyanshu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TestData {

    public String StartTime;
    public String ClassName;
    public String Description;
    public String Url;
    public String Name;
    public String TestToolVersion;
    public String SeleniumVersion;
    public String RestSharpVersion;
    public Browser Browser;
    public String TestParam;
    public boolean IsHeadless;
    public TestStatus TestCaseStatus;
    public List<TestEvidence> TestEvidences = new ArrayList<>();
    public TestManagementData TestManagementData;
    public String Os;
    public String ExecutedOn;
    public TestType TestType;
    public String Actual;
    public String AlmDefectId;
    public String TestExecutionName;
    public HashMap<String, String> CustomRows = new LinkedHashMap<>();
}
