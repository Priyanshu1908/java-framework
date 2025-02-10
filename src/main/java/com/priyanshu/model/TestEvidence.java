package com.priyanshu.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestEvidence {

    public Api Api;
    public String Screenshot;
    public String StepName;
    public String Details;
    public String Expected;
    public String Actual;
    public TestType TestType;
    public TestStatus StepStatus;
    public List<String> EmbeddedFiles = new ArrayList<>();
    public Map<String, String> CustomRows = new LinkedHashMap<>();
}
