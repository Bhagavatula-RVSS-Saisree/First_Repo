package com.testUtils;

import java.io.*;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.testng.Assert;

public class ExcelReader extends Utilities {
    public ExcelReader() {
        super();
    }

    private static Workbook workbook;

    public ExcelReader(String FILEPATH) throws IOException {
        FileInputStream fis = new FileInputStream(new File(prop.getProperty(FILEPATH)));
        workbook = new XSSFWorkbook(fis);
    }

    public static String getCellData(String sheetName, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        return cell.toString();
    }

    public int getRowCount(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        return sheet.getLastRowNum();
    }

    public int getColumnCount(String sheetName, int rowNum) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        return row.getLastCellNum();
    }

    public void close() throws IOException {
        workbook.close();
    }

    public void writeData(String sheetName, int rowNum, int colNum, String data) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        if(row == null) {
            row = sheet.createRow(rowNum);
        }
        Cell cell = row.getCell(colNum);
        if(cell == null) {
            cell = row.createCell(colNum);
        }
        cell.setCellValue(data);
    }

    public void saveAndClose(String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } finally {
            if(workbook != null) {
                workbook.close();
            }
        }
    }

    public HashMap<String, String> getData(String sheetName, String tcName, String pathName) {
        File file = new File(System.getProperty("user.dir") + props.getProperty(pathName));
        HashMap<String, String> testcaseData = new HashMap<>();
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                if(sheet.getRow(j).getCell(0).getStringCellValue().equalsIgnoreCase(tcName)) {
                    for(int k = 0; k < sheet.getRow(j).getPhysicalNumberOfCells(); k++) {
                        String key = sheet.getRow(0).getCell(k).getStringCellValue().trim();
                        String value = "";
                        try{
                            if(null != sheet.getRow(j).getCell(k).getStringCellValue()) {
                                value = sheet.getRow(j).getCell(k).getStringCellValue().trim();
                            }
                        } catch (Exception e) {
                        }
                        testcaseData.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
        return testcaseData;
    }

}
