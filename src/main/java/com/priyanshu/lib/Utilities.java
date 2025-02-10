package com.priyanshu.lib;

import com.google.common.collect.Iterables;
import com.priyanshu.model.Browser;
import com.priyanshu.model.TestStatus;
import com.priyanshu.model.TestType;
import io.cucumber.java.Scenario;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.priyanshu.lib.BaseTest.Assert;

public class Utilities {

    public static void createFolder(String folderPath){
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException ignored) {
        }
    }
    @SneakyThrows
    public static Properties getConfig(String propertiesFilename) {
        var config = new Properties();
        config.load(new FileInputStream(propertiesFilename));
        return mergeProperties(config, System.getProperties());
    }

    public static Properties mergeProperties(Properties configFileProperties, Properties commandLineProperties) {
        Properties mergedProperties = new Properties();
        var e = dummyMerge(configFileProperties, commandLineProperties).keys();
        while (e.hasMoreElements()) { var key = e.nextElement().toString();
            var configFilePropertiesValue = (String) configFileProperties.get(key);
            var commandLinePropertiesValue = (String) commandLineProperties.get(key);
            if (!StringUtils.isBlank(commandLinePropertiesValue)) {
                mergedProperties.put(key, commandLinePropertiesValue); }
            else if (!StringUtils.isBlank(configFilePropertiesValue)) {
                mergedProperties.put(key, configFilePropertiesValue); }
            else { mergedProperties.put(key, ""); } }
        return mergedProperties;
    }

    private static Properties dummyMerge(Properties... properties){
        return Stream.of(properties)
                .collect(Properties::new, Map::putAll, Map::putAll);
    }

    public static String getHostName(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown host";
        }
    }

    public static TestType getTestType(Class<?> cl){
        var t = cl.getAnnotation(Test.class);
        for(var group : t.groups()){
            for(var en : TestType.values()){
                if(en.toString().equals(group)) return en;
            }
        }
        return TestType.Unit;
    }

    public static TestType getTestType(Scenario scenario){
        for(var group : scenario.getSourceTagNames()){
            for(var en : TestType.values()){
                if(en.toString().equals(group.substring(1))) return en;
            }
        }
        return TestType.Unit;
    }

    public static String getName(Scenario scenario){
        return Iterables.getLast(scenario.getSourceTagNames()).replace("@","");
    }

    public static Browser getBrowser(String browser){
        try{
            return Browser.valueOf(StringUtils.capitalize(browser.toLowerCase()));
        } catch (IllegalArgumentException | NullPointerException e){
            return Browser.None;
        }
    }

    public static String getLibraryVersion(String name){
        if("priyanshu".equals(name)){
            try{
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                var doc = factory.newDocumentBuilder().parse(BaseTest.USER_DIR + File.separator + "pom.xml");
                return doc.getElementsByTagName("version").item(0).getTextContent();
            } catch (IOException | SAXException | ParserConfigurationException e){
                return "Not found";
            }
        }
        return SystemUtils.JAVA_CLASS_PATH.split(name + "_")[1].split(".jar")[0];
    }

    public static String GetFrameworkPath(){
        return System.getProperty("user.dir");
    }

    public static void zipUtils(String sourceDirPath, String zipFilePath) {
        FileUtils.deleteQuietly(new File(zipFilePath));
        Utilities.createFolderForFile(zipFilePath);
        try {
            Path p = Files.createFile(Paths.get(zipFilePath));
            try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
                Path pp = Paths.get(sourceDirPath);
                try (Stream<Path> walk = Files.walk(pp)) {
                    walk.filter(path -> !Files.isDirectory(path))
                            .forEach(path -> {
                                ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                                try {
                                    zs.putNextEntry(zipEntry);
                                    Files.copy(path, zs);
                                    zs.closeEntry();
                                } catch (IOException ignored) {
                                }
                            });
                }
            } catch (IOException ignored) {
            }
        } catch (IOException ignored) {
        }
    }

    public static void createFolderForFile(String filePath){
        File file = new File(filePath);
        var folderPath = file.getParent();
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException ignored) {

        }
    }

    public static TestStatus TryAssert(Utilities.Callback action){
        try {
            action.call();
            try {
                Assert.assertAll();
            } catch (AssertionError e){
                return TestStatus.Failed;
            }
            return TestStatus.Passed;
        } catch (Exception e){
            Assert.fail(e.getMessage());
            return TestStatus.Failed;
        }
    }

    public interface Callback{
        void call() throws SQLException;
    }
}
