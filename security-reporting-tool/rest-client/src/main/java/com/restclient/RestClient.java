package com.restclient;

import com.dao.gerritrepo.model.Repo;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;


public interface RestClient {
    boolean login(String username, String password);
    JSONArray getProductMatrixData() throws JSONException;
    JSONArray getSecurityReportDataFromSq(String username, String password, String repoId, String reportType) throws JSONException;
    JSONObject getSeverityData(String username, String password, String repoId, String reportType) throws JSONException;
    List<Repo> getAllSecurityReports(String username, String password, int limit) throws Exception;
    Repo convertJsonToRepo(JSONObject repoData, JSONArray secReport, JSONObject severityReport) throws JSONException;
    void getAndStoreRepoSecurityReports(String username, String password, int limit) throws Exception;
}
