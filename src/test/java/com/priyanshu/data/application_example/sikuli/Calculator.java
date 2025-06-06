package com.priyanshu.data.application_example.sikuli;

import com.priyanshu.lib.BaseTest;
import lombok.SneakyThrows;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import java.io.File;

public class Calculator extends BaseTest {

    private static final String FS = File.separator;
    String path = INPUT_DIR + "calc.sikuli" + FS;
    Screen screen = new Screen();

    public void openCalculator() throws FindFailed {
        screen.click(path + "CalculatorApp.png");
    }

    @SneakyThrows
    public boolean calculatorOperation(){
        try {
            screen.click(path + "Number2.png");
            screen.click(path + "Addition.png");
            screen.click(path + "Number2.png");
            screen.click(path + "Equals.png");
            Thread.sleep(3000);
            return true;
        } catch (FindFailed | InterruptedException e) {
            return false;
        }
    }

    @SneakyThrows
    public void closeCalculator(){
        screen.click(path + "Close.png");
    }
}
