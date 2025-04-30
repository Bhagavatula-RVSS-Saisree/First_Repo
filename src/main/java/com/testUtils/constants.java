package com.testUtils;

public class constants extends Utilities{
    public static final String pNameString = "Project 1_" + new Utilities().getDateTime();
    public static final String executionFilePath = ".\\src\\main\\java\\resources\\projectData.txt";
    public static final String executionPropFilePath = ".\\src\\main\\java\\resources\\projectData.properties";
    public static final String screenshotFolderPath = ".\\test-output\\screenshots";
    public static final String tcScreenShotFolder = constants.screenshotFolderPath + "\\" + pNameString;
    public static final String customReportInitialPath = "C:\\Users\\codin\\Automation Projects\\Project1\\test-output\\custom-report.html";
    public static final String customReportFinalPath = "C:\\Users\\codin\\Automation Projects\\Project1\\test-output\\Custom Reports\\";
}
