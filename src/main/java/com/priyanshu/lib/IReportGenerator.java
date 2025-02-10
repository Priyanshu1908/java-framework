package com.priyanshu.lib;

public interface IReportGenerator {

    IReportGenerator Prepare(TestReport testReport) throws Exception;

    void RenderAndSave() throws Exception;
}
