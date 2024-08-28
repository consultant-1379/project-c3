package com.restclient;

import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ExcelToJSONConverter {
    private final String excelFileName;

    public ExcelToJSONConverter(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    //resource: https://www.baeldung.com/java-microsoft-excel
    public JSONObject convertRoleMatrixToJSON() {
        JSONObject result = new JSONObject();
        Workbook wb = null;
        ClassLoader classLoader = getClass().getClassLoader();
        URL excelURL = classLoader.getResource(this.excelFileName);
        if (excelURL == null) {
            return null;
        }
        try (FileInputStream file = new FileInputStream(excelURL.getFile())) {
            wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);

            Iterator<Row> rowIter = sheet.rowIterator();
            Row currRow = rowIter.next(); // set first row -> we're intending to read data from current row's cells

            // Init column names for injection into jsonObject
            List<String> columnNames = getColumnNames(currRow);

            // Loop through all rows in the array, squashing down data into individual JSON objects containing repos
            JSONArray repos = new JSONArray();
            Map<Integer, String> rowData = new HashMap<>();
            Row CSA, CRA, CNA, CXP;
            while (rowIter.hasNext()) {
                // Check CSA
                currRow = rowIter.next();
                if (!currRow.getCell(0).getStringCellValue().equals("")) {
                    CSA = currRow;
                    rowData = updateTempRowData(rowData, CSA);
                    continue;
                }

                // Check CRA
                if (!currRow.getCell(1).getStringCellValue().equals("")) {
                    CRA = currRow;
                    rowData = updateTempRowData(rowData, CRA);
                    continue;
                }


                // Check CNA
                if (!currRow.getCell(2).getStringCellValue().equals("")) {
                    CNA = currRow;
                    rowData = updateTempRowData(rowData, CNA);
                    continue;
                }

                // Check CXP -> Loop
                if (!currRow.getCell(3).getStringCellValue().equals("") && rowIter.hasNext()) {
                    CXP = currRow;

                    // Squash down values of the found rows into one JSON object
                    rowData = updateTempRowData(rowData, CXP);
                    JSONObject repo = new JSONObject();
                    Iterator<Map.Entry<Integer, String>> it = rowData.entrySet().iterator();
                    int ind = 0;
                    while (it.hasNext() && ind != 24) {
                        ind++;
                        Map.Entry pair = it.next();
                        repo.put(columnNames.get((Integer) pair.getKey()), pair.getValue());
                    }

                    // Add to array
                    repos.put(repo);

                }
            }
            result.put("Product Matrix Data", repos);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR: Product Matrix Data could not be found.");
            return null;
        } catch (IOException | JSONException e) {
            System.out.println(e.getMessage());
            System.out.println("Something went wrong, stopping...");
            return null;
            // TODO - Log error to external log here for security reasons
        } finally {
            try {
                Objects.requireNonNull(wb).close();
            } catch (IOException e) {
                System.out.println("Something went wrong, stopping...");
                // TODO - Log error to external log here for security reasons
            }
        }

        return result;
    }

    private List<String> getColumnNames(Row startingRow) {
        List<String> columnNames = new LinkedList<>();
        for (Iterator<Cell> cellIter = startingRow.cellIterator(); cellIter.hasNext(); ) {
            Cell currCell = cellIter.next();
            columnNames.add(currCell.getStringCellValue());
        }
        return columnNames;
    }

    private Map<Integer, String> updateTempRowData(Map<Integer, String> tempStore, Row row) {
        for (Iterator<Cell> cellIter = row.cellIterator(); cellIter.hasNext(); ) {
            Cell c = cellIter.next();
            boolean newValEmpty = c.getStringCellValue().equals("");
            int index = c.getColumnIndex();
            if (newValEmpty && !tempStore.containsKey(index)) {
                tempStore.put(index, "");
            } else if (!newValEmpty) {
                tempStore.put(index, c.getStringCellValue());
            }
        }
        return tempStore;
    }

    public int getTotalReposInFile() {
        int total = 0;
        Workbook wb = null;
        ClassLoader classLoader = getClass().getClassLoader();
        URL excelURL = classLoader.getResource(this.excelFileName);
        if (excelURL == null) {
            return total; // is 0
        }
        try {
            FileInputStream file = new FileInputStream(excelURL.getFile());
            wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);

            Iterator<Row> rowIter = sheet.rowIterator();
            Row currRow;
            rowIter.next();

            while (rowIter.hasNext()) {
                currRow = rowIter.next();
                if (!currRow.getCell(3).getStringCellValue().equals("")) {
                    total++;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Product Matrix Data could not be found.");
        } catch (IOException e) {
            System.out.println("Something went wrong, stopping...");
            // TODO - Log error to external log here for security reasons
        } finally {
            try {
                Objects.requireNonNull(wb).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public int findIndexOfRepo(String repoName) {
        int count = 0;
        Workbook wb = null;
        ClassLoader classLoader = getClass().getClassLoader();
        URL excelURL = classLoader.getResource(this.excelFileName);
        if (excelURL == null) {
            return -1;
        }
        try {
            FileInputStream file = new FileInputStream(excelURL.getFile());
            wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);

            Iterator<Row> rowIter = sheet.rowIterator();
            Row currRow;
            rowIter.next();

            while (rowIter.hasNext()) {
                currRow = rowIter.next();
                if (currRow.getCell(16).getStringCellValue().equals(repoName)) {
                    return count;
                }
                count++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Product Matrix Data could not be found.");
        } catch (IOException e) {
            System.out.println("Something went wrong, stopping...");
            // TODO - Log error to external log here for security reasons
        } finally {
            try {
                Objects.requireNonNull(wb).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

}
