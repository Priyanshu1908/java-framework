import org.testng.annotations.*;

import java.util.logging.Logger;

public class BaseTest {

    Logger logger;

    @BeforeSuite
    public void beforeSuite(){
        logger.info("Starting test suite");
    }

    @BeforeMethod
    public void beforeMethod(){

    }

    @AfterMethod
    public void afterMethod(){

    }

    @AfterSuite
    public void afterSuite(){
        logger.info("Stopping test suite");
    }
}
