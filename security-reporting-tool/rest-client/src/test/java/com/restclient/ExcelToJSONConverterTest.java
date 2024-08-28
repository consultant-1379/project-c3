package com.restclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelToJSONConverterTest {
    private ExcelToJSONConverter exl2JSON;

    @BeforeEach
    void setup() {
        exl2JSON = new ExcelToJSONConverter("role-matrix.xlsx");
    }

    @Test
    void testConvertToJSONRealFile() {
        JSONObject repos = exl2JSON.convertRoleMatrixToJSON();
        assertTrue(Objects.nonNull(repos));
    }

    @Test
    void testBadFilePath() {
        // Should return empty JSONObject if fails / throws FileNotFoundException
        ExcelToJSONConverter exl = new ExcelToJSONConverter("fkldsf");
        JSONObject repos = exl.convertRoleMatrixToJSON();

        assertTrue(repos == null);

    }

    @Test
    void testNotExcelFile() {
        // Should return empty JSONObject if fails / throws FileNotFoundException
        ExcelToJSONConverter exl = new ExcelToJSONConverter("src/test/resources/notAnExcel.txt");
        JSONObject repos = exl.convertRoleMatrixToJSON();

        assertTrue(repos == null);
    }

    @Test
    void testGetNumOfRepos() {
        int totalRepos = exl2JSON.getTotalReposInFile();

        // 3123 is the number of repos seen in the provided role-matrix excel file
        assertEquals(3123, totalRepos);
    }

    @Test
    void testGetNumOfReposNotExcel() {
        ExcelToJSONConverter exl = new ExcelToJSONConverter("src/test/resources/notAnExcel.txt");
        int totalRepos = exl.getTotalReposInFile();

        assertEquals(0, totalRepos);
    }

    @Test
    void testGetNumOfReposBadFilePath() {
        ExcelToJSONConverter exl = new ExcelToJSONConverter("fdsfdfsf");
        int totalRepos = exl.getTotalReposInFile();

        assertEquals(0, totalRepos);
    }

    @Test
    void testVerifyNumberOfRepos() throws JSONException {
        // Test that ALL the repos in the Excel file are added into the final JSON
        int totalRepos = exl2JSON.getTotalReposInFile();
        JSONObject result = exl2JSON.convertRoleMatrixToJSON();

        JSONArray data = (JSONArray) result.get("Product Matrix Data");
        assertEquals(totalRepos, data.length());
    }

    @Test
    void testFindIndexOfRepo() {
        String repoName = "com.ericsson.oss.mediation.autoprovisioning/ap-mediation-model";
        // Use line number as seen in the excel file
        assertEquals(8, exl2JSON.findIndexOfRepo(repoName));
    }

    @Test
    void testFindIndexOfRepoInvalid() {
        String repoName = "fffffl";
        assertEquals(-1, exl2JSON.findIndexOfRepo(repoName));
    }
}