package com.priyanshu.sanity_tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;

public class AutoItTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.get("https://easyupload.io/");
        driver.findElement(By.xpath("//button[text()='click here or drop files to upload or transfer']")).click();
        ProcessBuilder processBuilder = new ProcessBuilder( "C:\\Users\\priya\\IdeaProjects\\Java-Framework\\src\\test\\java\\com\\priyanshu\\data\\application_example\\autoit\\FileUpload.exe");
        Process process = processBuilder.start();
        process.waitFor();
    }
}
