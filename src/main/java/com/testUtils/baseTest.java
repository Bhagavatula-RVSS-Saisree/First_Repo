package com.testUtils;

import java.util.Properties;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class baseTest {

    public static String myProjectName;

    public static WebDriver driver;
    public static Properties props;

    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeSuite(alwaysRun = true, enabled = true)
    public void loadData() {
        myProjectName = constants.pNameString;
        Utilities.createDefaultFiles();
        Utilities.createDefaultDir();

        props = new Properties();
        File propFile = new File(
                "C:\\Users\\codin\\Automation Projects\\Project1\\src\\main\\java\\com\\Config\\Config.properties");
        try {
            FileInputStream fis = new FileInputStream(propFile);
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageFactory.initElements(driver, this);
    }

    @BeforeTest(alwaysRun = true, enabled = true)
    public WebDriver initializingBrowser() throws InterruptedException {

        extent = ReportManager.getReportInstance();
        test = extent.createTest(this.getClass().getSimpleName());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.4);
        int height = (int) (screenSize.height * 0.4);


        ChromeOptions options = new ChromeOptions();
        //	options.addArguments("--window-size=1920,1080");
        //	options.addArguments("--start-maximized");
        options.addArguments("--window-size=" + width + "," + height);
        String browserName = props.getProperty("browserName");
        String browserMode = props.getProperty("isHeadLess");
        if (browserMode.equalsIgnoreCase("true")) {
            options.addArguments("--headless");
        }
        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty(props.getProperty("chromeBrowser"), props.getProperty("driverLocation"));
            driver = new ChromeDriver(options);
        } else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.get(props.getProperty("url"));

        return driver;
    }


    @AfterTest(alwaysRun = true, enabled = true)
    public void tearDown() {
        try {
            Utilities.fileRename(myProjectName);
            //		driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        extent.flush();
    }

}
