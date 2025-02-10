package com.priyanshu.lib;

import com.priyanshu.model.TestManagementData;

public interface ITestManagement {

    void UpdateTestRun(TestReport report);
    void AddTestRunAttachment(TestReport report, String zipName);
    void AddDefectAttachment(TestReport report);
    TestManagementData GetData(TestReport report);
    String RaiseDefect(TestReport report);
}
