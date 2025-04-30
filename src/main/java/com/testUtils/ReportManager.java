package com.testUtils;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {

    private static ExtentReports extent;

    public static ExtentReports getReportInstance() {
        if(extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/SparkReport_" + new Utilities().getDateTime() + ".html");
            sparkReporter.config().setDocumentTitle("Project 1 Automation Test Summary");
            sparkReporter.config().setReportName("Automation Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }
}
