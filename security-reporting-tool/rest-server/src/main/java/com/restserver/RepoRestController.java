package com.restserver;

import com.dao.gerritrepo.GerritRepoService;
import com.dao.gerritrepo.model.Repo;
import com.restclient.ExcelToJSONConverter;
import com.restclient.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class RepoRestController {
    @Autowired
    private RestClient restClient;

    @Autowired
    private GerritRepoService gerritService;

    @GetMapping(value = "/repos/securityreports", produces = {"application/json"})
    public List<Repo> getAllSecurityReports() {
        return gerritService.getSecurityReportTotalsForRepos();
    }

    @GetMapping(value = "/repos/nosecurityreports", produces = {"application/json"})
    public List<Repo> getAllGerritReposWithoutSecurityReportTotals() {
        return gerritService.getAllGerritReposWithoutSecurityReportTotals();
    }

    /**
     * Gets a list of all repos and their security report data, complete with the CSA/CRA/CNA
     * they correspond to as well as Line Managers responsible for them
     *
     * @return list of repos with filters and security report data
     */
    @GetMapping(value = "/repos", produces = {"application/json"})
    public List<Repo> getAllGerritRepos() {
        return gerritService.getAllGerritRepos();
    }

    @PostMapping(value = "/repo", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Repo> insertRepo(@RequestBody RepoRequestObject repo) {
        return new ResponseEntity<>(gerritService.insertRepo(repo), HttpStatus.CREATED);
    }

    @GetMapping(value = "/repo/{id}", produces = {"application/json"})
    public ResponseEntity<Repo> getRepoById(@PathVariable String id) {
        return ResponseEntity.ok(gerritService.getRepoById(id));
    }

    @GetMapping(value = "/repo", produces = {"application/json"})
    public ResponseEntity<Repo> getRepoByName(@RequestParam String name) {
        return ResponseEntity.ok(gerritService.getRepoByName(name));
    }

    @GetMapping(value = "/repos/name", produces = {"application/json"})
    public ResponseEntity<List<String>> getAllGeritReposWithName() {
        return ResponseEntity.ok(gerritService.getAllGerritRepoNames());
    }


    @PostMapping(value = "/login", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> login(@RequestBody User user) {
        boolean result = restClient.login(user.getUsername(), user.getPassword());
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).build(); // 401 Unauthorized = invalid authentication
    }

    @PostMapping(value = "/index", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> doIndexProcess(@RequestBody User user, @RequestParam int limit) {
        try {
            restClient.getAndStoreRepoSecurityReports(user.getUsername(), user.getPassword(), limit);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build(); // Indicate a server error in case we get an exception
        }
    }

//    @GetMapping(value = "/repos/test", produces = {"application/json"})
//    public Repo test() throws JSONException {
//        ExcelToJSONConverter exl2JSON = new ExcelToJSONConverter("role-matrix.xlsx");
//        JSONObject repos = exl2JSON.convertRoleMatrixToJSON();
//        Repo repo = null;
//        try {
//            JSONArray pmd = repos.getJSONArray("Product Matrix Data");
//            String repoId = "com.ericsson.oss.presentation.server.fm.alarmcontroldisplayservice/alarmcontroldisplayservice";
//            int index = exl2JSON.findIndexOfRepo(repoId);
//            if(index == -1) return null;
//            JSONObject repoData = pmd.getJSONObject(index);
//            JSONArray secReportData = restClient.getSecurityReportDataFromSq("", "", repoId, "owaspTop10");
//            JSONObject severityReport = restClient.getSeverityData("", "", repoId, "owaspTop10");
//            if (secReportData != null) {
//                repo = restClient.convertJsonToRepo(repoData, secReportData, severityReport);
//            }
//        } catch (JSONException e) {
//            System.out.println(e.getMessage());
//        }
//        return repo;
//    }
}
