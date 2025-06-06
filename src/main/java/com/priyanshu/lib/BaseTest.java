package com.priyanshu.lib;

import com.priyanshu.model.TestData;
import com.priyanshu.model.TestType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.Optional;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class BaseTest {

    public WebDriver Driver;
    public static SoftAssert Assert = new SoftAssert();
    HashMap<String, Integer> testCount = new HashMap<>();
    protected Logger logger = Logger.getLogger(BaseTest.class.getName());
    public static final Map<String, TestReport> reportMap = new ConcurrentHashMap<>();

    public TestReport getReport() {
        return reportMap.get(Thread.currentThread().getName());
    }

    public void setReport(TestReport report) {
        reportMap.put(Thread.currentThread().getName(), report);
    }

    public static final Map<String, WebDriver> driverMap = new ConcurrentHashMap<>();

    private String className;
    private static final String FS = File.separator;
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String RESOURCES = USER_DIR + FS + "src" + FS + "test" + FS + "resources" + FS;
    public static final String INPUT_DIR = RESOURCES + "Inputs" + FS;
    public static final String ALM_DIR = INPUT_DIR + "TestManagement" + FS + "ALM" + FS;
    public static final String TARGET = USER_DIR + FS + "target" + FS;
    public static final String OUTPUTS = TARGET + "Outputs" + FS;
    private static final String TEST_RESULTS = OUTPUTS + "TestResult_";
    private static final String ConfigPath = USER_DIR + "/config.ini";
    private static final Properties config = BaseTest.getConfig();
    private static final String InputDataExcelPath = config.getProperty("testDataWorkBookName") + ".xls";
    private static final String TestDataSheetName = config.getProperty("testDataWorkSheetName");
    private final Date startTime = new Date();
    private static String currentTestOutputDirName = null;
    private TestType testType;
    //private static final List<List<String>> excelData = Data.ReadDataFromExcel(TestDataSheetName, INPUT_DIR + InputDataExcelPath, false);
    private PdfReportGenerator pdfReportGenerator;
    private ApiReportGenerator apiReportGenerator;
    private HtmlReportGenerator htmlReportGenerator;
    private XmlReportGenerator xmlReportGenerator;
    private JsonReportGenerator jsonReportGenerator;
    private VideoReportGenerator videoReportGenerator;
    private ITestManagement testManagement = null;
    private static final List<TestReport> reports = new ArrayList<>();
    private final Boolean headless = Boolean.parseBoolean(config.getProperty("headless", "false"));

    //Already defined above
    private static final String InputPath = Utilities.GetFrameworkPath() + "src/test/resources/Inputs/";

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info(("Starting test suite"));
        logger.setUseParentHandlers(false);
        if (currentTestOutputDirName == null) currentTestOutputDirName = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + "_" + Thread.currentThread().getId();

    }

    @Parameters({"none", "none", "none"})
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(@Optional("none") String name, @Optional("none") String description, @Optional("none") String cucumberTestType, ITestResult result) throws Exception {
        testType = !"none".equals(cucumberTestType) ? TestType.valueOf(cucumberTestType) : Utilities.getTestType(getClass());
        className = "none".equals(name) ? this.getClass().getSimpleName() : name;
        var browser = config.getProperty("browser");
        var testData = new TestData();
        testData.Description = description;
        testData.Browser = Utilities.getBrowser(browser);
        testData.ClassName = className;
        testData.Name = className;
        testData.StartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        testData.Os = System.getProperty("os.name");
        testData.ExecutedOn = System.getProperty("os.name");
        testData.TestToolVersion = Utilities.getLibraryVersion("priyanshu");
        testData.SeleniumVersion = Utilities.getLibraryVersion("selenium-java");
        testData.RestSharpVersion = Utilities.getLibraryVersion("rest-assured");
        testData.TestParam = getTestParam(result);
        testData.TestType = testType;
        testData.TestExecutionName = currentTestOutputDirName;
        testData.TestManagementData = TestReport.ReadAlmFile(ALM_DIR, config);

        setReport(new TestReport(testData).SetOutputPath(TEST_RESULTS + currentTestOutputDirName + FS + testData.Name));
        pdfReportGenerator = new PdfReportGenerator();
        htmlReportGenerator = new HtmlReportGenerator();
        apiReportGenerator = new ApiReportGenerator();
        videoReportGenerator = new VideoReportGenerator();
        xmlReportGenerator = new XmlReportGenerator();
        jsonReportGenerator = new JsonReportGenerator();
        pdfReportGenerator.Prepare(getReport());
        htmlReportGenerator.Prepare(getReport());
        xmlReportGenerator.Prepare(getReport());
        jsonReportGenerator.Prepare(getReport());

        if (Arrays.asList(TestType.Api, TestType.WebApi).contains(testType)) apiReportGenerator.Prepare(getReport());
//        if(ShouldBeSkipped(testData, excelData)){
//            getReport().TestData.TestCaseStatus = TestStatus.Ignored;
//            throw new SkipException("Skipping this exception");
//        }
        testData.Os = System.getProperty("os.name");
        testData.ExecutedOn = Utilities.getHostName();

        if (Arrays.asList((TestType.Web), TestType.WebApi).contains(testType)) {
            Driver = DriverManager.GetDriver(Utilities.getBrowser(browser), headless);
            testData.IsHeadless = headless;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() throws Exception {
//        if(getReport().TestData.TestManagementData == null){
//            getReport().TestData.TestManagementData = testManagement.GetData(getReport());
//        }
        if (Arrays.asList(TestType.Web, TestType.WebApi).contains(testType) && Driver != null) Driver.quit();
        logger.info("Test finished");
        if (Arrays.asList(TestType.Api, TestType.WebApi).contains(testType)) apiReportGenerator.RenderAndSave();
        pdfReportGenerator.RenderAndSave();
        htmlReportGenerator.RenderAndSave();
        xmlReportGenerator.RenderAndSave();
        jsonReportGenerator.RenderAndSave();
        videoReportGenerator.RenderAndSave();
        BaseTest.reports.add(getReport());
        if (testManagement != null) {
            var zipName = ZipResults();
        }
        Assert.assertAll();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("Stopping test suite");
        File dir = new File(ALM_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files != null && files.length > 0) {
            Utilities.createFolder(OUTPUTS + FS);
            Utilities.zipUtils(TEST_RESULTS + currentTestOutputDirName,
                    OUTPUTS + FS + getReport().TestData.Name + ".zip");
        }
        Utilities.generateJSONReport(reports, BaseTest.OUTPUTS + "notification.json",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
    }

    public String GetScreenshot() {
        return ((TakesScreenshot) Driver).getScreenshotAs(OutputType.BASE64);
    }

    public String ZipResults() {
        if (getReport().TestData.TestManagementData == null) return "";
        var ZipFilePath = OUTPUTS + FS + getReport().TestData.Name + ".zip";
        Utilities.createFolder(OUTPUTS);
        var SourceDirPath = TEST_RESULTS + currentTestOutputDirName + FS + getReport().TestData.Name;
        Utilities.zipUtils(SourceDirPath, ZipFilePath);
        return ZipFilePath;
    }

    public static Properties getConfig() {
        return Utilities.getConfig(ConfigPath);
    }

    private String getTestParam(ITestResult result) {
        if (result == null) return "";
        if (Arrays.stream(result.getParameters()).findAny().isEmpty()) return "";
        Integer count = 0;
        try {
            count = this.testCount.get(result.getTestName());
            count++;
            this.testCount.put(result.getTestName(), count);
            return "_" + count;
        } catch (Exception e) {
            this.testCount.put(result.getTestName(), 1);
            return "_" + count;
        }
    }

//    public static Boolean ShouldBeSkipped(TestData data, List<List<String>> excelData){
//        try{
//            return excelData.stream().anyMatch(row ->(row.get(excelData.get(0).indexOf("Dataflag")).substring(0,1)
//                    .equalsIgnoreCase("n")&& row.get(excelData.get(0).indexOf("Test Case Name")).equals(data.Name)));
//        } catch (IndexOutOfBoundsException e){
//            return false;
//        }
//    }
}
