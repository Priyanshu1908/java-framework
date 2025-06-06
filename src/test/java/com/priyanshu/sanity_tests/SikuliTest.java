package com.priyanshu.sanity_tests;

import com.priyanshu.data.application_example.sikuli.Calculator;
import com.priyanshu.lib.BaseTest;
import com.priyanshu.model.TestEvidence;
import com.priyanshu.model.TestStatus;
import org.sikuli.script.FindFailed;
import org.testng.annotations.Test;

import static com.priyanshu.lib.Utilities.TryAssert;

@Test(groups = "App", enabled = false)
public class SikuliTest extends BaseTest {

    public void calculatorTest() throws FindFailed {
        getReport().TestData.Description = "Run Calculator using Sikuli";
        Calculator calculator = new Calculator();
        calculator.openCalculator();
        var status = TryAssert(() -> Assert.assertTrue(calculator.calculatorOperation()));
        getReport().AddEvidence(new TestEvidence(){
            {
                Expected = "Scenario was executed properly";
                Actual = "Scenario was " + (status.equals(TestStatus.Passed) ? "" : "not") + " executed properly";
                StepStatus = status;
                Details = "Run windows calculator, add 2 + 2, display result and close calculator";
                StepName = "Calculator Test";
                TestType = com.priyanshu.model.TestType.App;
            }
        });
        calculator.closeCalculator();
    }
}
