package com.testUtils;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import com.google.common.reflect.TypeToken;

import static com.testUtils.ExcelReader.*;
// import freemaker.core.ParseException;

public class Utilities extends baseTest{
    public Utilities() {
        super();
        PageFactory.initElements(driver,this);
    }

    File file;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    XSSFWorkbook wb;
    XSSFSheet sheet;
    XSSFRow row2;
    List<String> cellValues = new ArrayList<>();
    protected String[] cellArray;

    @FindBy(xpath = "(//input[@type='text'])[1]/../following::li/div")
    public List<WebElement> dropDownElements;

    public static Properties prop;
    protected static ExcelReader excelReader;
    private static WebDriverWait wait;

    public static void waitFor(int seconds) {
        try{
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void click(WebElement element, String message) {
        try{
            Thread.sleep(1500);
            if(element.isDisplayed()) {
                scrollToElement(driver, element);
                element.click();
                Reporter.log("Clicked the element: " + message, true);
            } else {
                jsClick(driver, element);
            }
        } catch (Exception e) {
            jsClick(driver, element);
        }
    }

    public void jsClick(WebDriver driver, WebElement element) {
        try{
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    public void enterText(WebElement element, String testValue) {
        try{
            Thread.sleep(1500);
            if(element.isDisplayed()) {
                scrollToElement(driver,element);
                element.clear();
                element.click();
                element.sendKeys(testValue);
                Reporter.log("Entered text " + testValue, true);
            }
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    public void waitForElement(WebDriver driver, WebElement element) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, 60);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
        }
    }

    public void SelectValueFromDropdown(WebElement element, String Answer) throws InterruptedException {
        click(element,"clicked dropdown");
        Thread.sleep(800);
        String DropDownVal = "//div//*[text()[contains(.,'Value')]]";
        WebElement DDVal = driver.findElement(By.xpath(DropDownVal.replaceAll("Value", Answer)));
        click(DDVal, Answer);
    }

    public void waitForElement(WebDriver driver, WebElement element, int time) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
        }
    }

    public void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        waitForElement(driver, element);
        try{
            js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
        } catch(Exception e) {
            js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
        }
    }

    public void isPageReady(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try{
            for(int i = 0; i < 200; i++) {
                Thread.sleep((long) (1 * 1000));
                if("complete".equalsIgnoreCase(js.executeScript("return document.readyState").toString())) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectValueFromDropDown(WebDriver driver, String valueToBeSelected, int... startCount) {
        boolean valueFound = false;
        int sizeOfDropDown = dropDownElements.size();
        String ddValueName = null;
        int i = 1;
        if(startCount.length > 0) {
            i = 2;
        }
        try{
            for (int j = 1; j < sizeOfDropDown; j++) {
                WebElement dropDownElement = driver.findElement(By.xpath("(//input[@type = 'text'])[1]/../following::li[" + j + "]/div"));
                waitForElement(driver, dropDownElement);
                ddValueName = dropDownElement.getText().toString().trim();
                if(ddValueName.equals(valueToBeSelected)) {
                    click(dropDownElement, ddValueName + "is selected");
                    valueFound = true;
                }
                if(valueFound) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(valueFound) {
                Assert.assertTrue(valueFound);
            } else if (!valueFound) {
                Assert.fail(ddValueName + "is present");
            }
        }
    }

    public String getDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime now = LocalDateTime.now();
        String myDate = dtf.format(now).toString();
        return myDate;
    }

    public String addMonths(String dateAsString, int nbMonths) throws ParseException {

        String format = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dateAsObj;
        String newEndDate = null;
        try {
            dateAsObj = sdf.parse(dateAsString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateAsObj);
            cal.add(Calendar.MONTH, nbMonths);
            Date dateAsObjAfterAMonth = cal.getTime();
            newEndDate = sdf.format(dateAsObjAfterAMonth);
            System.out.println(sdf.format(dateAsObjAfterAMonth));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return newEndDate;
    }

    public static void createDefaultFiles() {
        File file = new File(constants.customReportFinalPath);
        boolean result;
        try {
            result = file.createNewFile();
            if (result) {
                System.out.println("file created " + file.getCanonicalPath());
            } else {
                System.out.println("File already exist at location: " + file.getCanonicalPath());
            }
        } catch (Exception e) {
            e.printStackTrace(); // prints exception if any
        }
    }

    public static void createDefaultDir() {
        File screenShotDir = new File(constants.screenshotFolderPath);
        File tcScreenShotDir = new File(constants.tcScreenShotFolder);
        File customReportDir = new File(constants.customReportFinalPath);

        boolean screenShotDirsuccess = screenShotDir.mkdir();
        boolean tcScreenShotDirsuccess = tcScreenShotDir.mkdir();

        boolean customReportDirSuccess = customReportDir.mkdir();


        if (screenShotDirsuccess) {
            System.out.println("Directory created successfully");
        } else {
            System.out.println("Directory already exist at location");
        }
//		if (customReportDirSuccess) {
//			System.out.println("Directory created successfully");
//		} else {
//			System.out.println("Directory already exist at location");
//		}
        if (tcScreenShotDirsuccess) {
            System.out.println("Directory created successfully");
        } else {
            System.out.println("Directory already exist at location");
        }

    }

    public static void writeDataToFile(String path, String text) {
        try {
            FileWriter fWriter = new FileWriter(path);
            fWriter.write(text);
            System.out.println(text);
            fWriter.close();
            System.out.println("File is created successfully with the content.");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public String readFileToString(String path) {
        String recordNumberString = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    recordNumberString = line;
                }
            } finally {
                br.close();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return recordNumberString;
    }

    public static HashMap<String, String> getJsonDataToMap(String jsonPath) {
        String testObject = null;
        HashMap<String, String> dataMap = null;
        try {
            FileReader reader = new FileReader(jsonPath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            testObject = jsonObject.toString();

            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            dataMap = gson.fromJson(testObject, type);
            System.err.println(dataMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataMap;
    }

    public static void takeSnapShot(WebDriver webdriver) {
        try {
            String fileWithPath = constants.tcScreenShotFolder + "\\" + new Utilities().getDateTime() + ".png";
            TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File(fileWithPath);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
        }
    }

    public static void snapshot(WebDriver webdriver) {
        try {
            String fileWithPath = constants.screenshotFolderPath + "\\" + new Utilities().getDateTime() + ".png";
            TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File(fileWithPath);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
        }
    }

    public void writeDataToPropFile(String path, String key, String data, String... propName) {
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        String text = "";
        if (propName.length > 0) {
            text = propName[0].toString();
        }
        try {
            Properties configProperty = new Properties();

            File file = new File(path);
            fileIn = new FileInputStream(file);
            configProperty.load(fileIn);
            configProperty.setProperty(key, data);
            fileOut = new FileOutputStream(file);
            configProperty.store(fileOut, text);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileOut.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void fileMove() {
        Path source = Paths.get(constants.customReportInitialPath);
        Path destination = Paths.get(constants.customReportFinalPath, "custom-report.html"); // Specify destination file name

        try {
            // Ensure destination folder exists
            File destinationDir = new File(constants.customReportFinalPath);
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
            java.nio.file.Files.move(source, destination);
            System.out.println("File moved successfully to: " + destination.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("File move failed.");
        }
    }

    // Method to rename the file
    public static void fileRename(String newFileName) {
        try {
            fileMove(); // First move the file

            Path movedFilePath = Paths.get(constants.customReportFinalPath, "custom-report.html");
            Path renamedFilePath = Paths.get(constants.customReportFinalPath, newFileName + ".html");

            // Rename the file
            File movedFile = movedFilePath.toFile();
            File renamedFile = renamedFilePath.toFile();

            if (movedFile.exists()) {
                boolean flag = movedFile.renameTo(renamedFile);
                if (flag) {
                    System.out.println("File successfully renamed to: " + renamedFilePath);
                } else {
                    System.err.println("File rename operation failed.");
                }
            } else {
                System.err.println("Moved file not found. Rename operation skipped.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void click(WebDriver driver, WebElement element, String message) {

        try {
            Thread.sleep(3000);
            if (element.isDisplayed()) {
                scrollToElement(driver, element);
                element.click();
                Reporter.log("Clicked the element: " + message, true);
            } else {
                jsClick(driver, element);
            }
        } catch (Exception e) {
            try {
                Actions action = new Actions(driver);
                action.moveToElement(element).click().perform();
            } catch (Exception e2) {
                jsClick(driver, element);
            }
        }
    }

    public HashMap<String, String> getTestCaseData(HashMap<String, String> testData, String sheetName, String tcName, String pathName) {
        try {
            testData = new ExcelReader().getData(sheetName, tcName, pathName);
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
        return testData;
    }

    public static WebElement find(WebElement identifier) {
        wait = new WebDriverWait(driver, 60);
        return wait.until(ExpectedConditions.visibilityOf(identifier));
    }

    public static void getDataFromExcel(WebElement identifier,String FILEPATH, String SheetName, int RowNo, int ColNo) throws IOException {
        excelReader = new ExcelReader(FILEPATH);
        String CellData = excelReader.getCellData(SheetName, RowNo, ColNo);
        find(identifier).sendKeys(CellData);
    }

    public void getExcelData(String testDataFilePath, String testDataSheet) throws IOException {
        file = new File(props.getProperty(testDataFilePath));
        inputStream = new FileInputStream(file);
        wb = new XSSFWorkbook(inputStream);
        sheet = wb.getSheet(testDataSheet);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row2 = sheet.getRow(i);
            if (row2 == null) {
                row2 = sheet.createRow(i);  // Create the row if it doesn't exist
            }
            List<String> cellValues = new ArrayList<>();
            for (Cell cell : row2) {
                DataFormatter formatter = new DataFormatter();
                cellValues.add(formatter.formatCellValue(cell));
            }
            cellArray = cellValues.toArray(new String[0]);
        }
    }

    public static String getCellData(String FILEPATH, String SheetName, int rowNum, int colNum) throws IOException {
        excelReader = new ExcelReader(props.getProperty(FILEPATH));
        String CellData = excelReader.getCellData(SheetName, rowNum, colNum);
        return CellData.toString();
    }

    public void writeDataToExcel(String testDataFilePath, String testDataSheet, int rowNum, int colNum, String data) throws IOException {
        file = new File(props.getProperty(testDataFilePath));
        inputStream = new FileInputStream(file);
        wb = new XSSFWorkbook(inputStream);
        sheet = wb.getSheet(testDataSheet);
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        cell.setCellValue(data);
        FileOutputStream outputStream = new FileOutputStream(file);
        wb.write(outputStream);
        wb.close();
        outputStream.close();
        inputStream.close();
    }

    public static void writeAssetDataToExcel(String FILEPATH, String SheetName,int AQ,int colNum,String TempID, String aID) throws IOException {
        XSSFWorkbook workBook;
        XSSFSheet Sheet;
        try (FileInputStream IS = new FileInputStream(new File(prop.getProperty(FILEPATH)))) {
            workBook = new XSSFWorkbook(IS);
            Sheet = workBook.getSheet(SheetName);

            int rowCount = Sheet.getLastRowNum() - Sheet.getFirstRowNum();
            Row row = Sheet.getRow(0);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Row newRow = Sheet.createRow(rowCount + 1);
                Cell cell = newRow.getCell(colNum);
                if (cell == null) {
                    cell = newRow.createCell(colNum);
                }
                cell.setCellValue(TempID + " - " + aID);
            }
        }
        FileOutputStream outputStream = new FileOutputStream(new File(prop.getProperty(FILEPATH)));
        workBook.write(outputStream);
        workBook.close();
        outputStream.close();
    }

    public boolean isElementPresent(WebElement element, int TimeInSec) throws InterruptedException {
        boolean ElementPresent = false;
        Thread.sleep(TimeInSec);
        if(element.isDisplayed()) {
            ElementPresent = true;
        }
        return ElementPresent;
    }

}
