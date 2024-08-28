package com.restclient;

import com.dao.gerritrepo.GerritRepoService;
import com.dao.gerritrepo.model.Filters;
import com.dao.gerritrepo.model.Repo;
import com.dao.gerritrepo.model.securitycerts.SecurityReportTotals;
import com.dao.gerritrepo.model.securitycerts.owasp.OwaspCategory;
import com.dao.gerritrepo.model.securitycerts.owasp.OwaspReportData;
import com.dao.gerritrepo.model.securitycerts.severity.SeverityData;
import com.dao.gerritrepo.model.securitycerts.severity.SeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Component
public class RestClientImpl implements RestClient {
    @Value("${ericssonSqUrl1}")
    private String ericssonSqUrl1;

    @Autowired
    public GerritRepoService repoService;

    @Value("${sonarqubeBaseAddress}")
    private String baseAddress;


    /**
     * @param username case-sensitive valid Ericsson SQ
     * @param password case-sensitive valid Ericsson password
     * @return true if credentials match a current record and access to the SQ instance is allowed, false otherwise
     */
    @Override
    public boolean login(String username, String password) {
        // Username and password should be validated on the REST API layer
        RestTemplate template = new RestTemplate();
        String url = ericssonSqUrl1 + "/authentication/login?login=" + username + "&password=" + password;
        try {
            template.exchange(url, HttpMethod.POST, null, Void.class);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public JSONObject getSeverityData(String username, String password, String repoId, String reportType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();

        String fixedRepoId = repoId.replaceAll("/", ":");

        String url = ericssonSqUrl1 + "/issues/search?componentKeys=" + fixedRepoId + "&tags=owasp-a1,owasp-a2,owasp-a3,owasp-a4,owasp-a5,owasp-a6,owasp-a7,owasp-a8,owasp-a19,owasp-a10&facets=" + reportType + ",severities";

        JSONObject severityData;
        // Init category data containers
        JSONObject owaspCategories = null;

        // Using tag, set target JSONObj
        try {
            owaspCategories = initOWASPCategoryJson();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            String responseBody = response.getBody();
            severityData = new JSONObject(responseBody);

            // If total components is null, there's no data to read
            if (severityData.getJSONArray("issues").length() == 0) {
                System.out.println("SeverityData has no issues, returning default");
                return owaspCategories;
            }

            // Iterate through each issue fetched from queries and assign to different OWASP category
            JSONArray issues = severityData.getJSONArray("issues");
            for (int i = 0; i < issues.length(); i++) {
                JSONObject issue = issues.getJSONObject(i);
                JSONArray tags = issue.getJSONArray("tags");

                // Parse out owasp tag
                String tag = "";
                for (int j = 0; j < tags.length(); j++) {
                    if (tags.getString(j).startsWith("owasp-a")) {
                        tag = tags.getString(j);
                    }
                }
                if (tag.equals("")) {
                    continue;
                }

                String catID = tag.split("-")[1];
                String issueType = "";
                try {
                    issueType = issue.getString("severity");
                } catch (JSONException e) {
                    continue;
                }

                int oldval;
                switch (issueType) {
                    case "MAJOR":
                        oldval = owaspCategories.getJSONObject(catID).getInt("MAJOR");
                        owaspCategories.getJSONObject(catID).put("MAJOR", oldval + 1);
                        break;
                    case "MINOR":
                        oldval = owaspCategories.getJSONObject(catID).getInt("MINOR");
                        owaspCategories.getJSONObject(catID).put("MINOR", oldval + 1);
                        break;
                    case "BLOCKER":
                        oldval = owaspCategories.getJSONObject(catID).getInt("BLOCKER");
                        owaspCategories.getJSONObject(catID).put("BLOCKER", oldval + 1);
                        break;
                    case "INFO":
                        oldval = owaspCategories.getJSONObject(catID).getInt("INFO");
                        owaspCategories.getJSONObject(catID).put("INFO", oldval + 1);
                        break;
                    case "CRITICAL":
                        oldval = owaspCategories.getJSONObject(catID).getInt("CRITICAL");
                        owaspCategories.getJSONObject(catID).put("CRITICAL", oldval + 1);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR: " + e.getMessage());
            return initOWASPCategoryJson();
        }

        return owaspCategories;
    }

    private JSONObject initOWASPCategoryJson() {

        JSONObject owaspCategories = new JSONObject();
        try {
            owaspCategories.put("a1", new JSONObject());
            owaspCategories.put("a2", new JSONObject());
            owaspCategories.put("a3", new JSONObject());
            owaspCategories.put("a4", new JSONObject());
            owaspCategories.put("a5", new JSONObject());
            owaspCategories.put("a6", new JSONObject());
            owaspCategories.put("a7", new JSONObject());
            owaspCategories.put("a8", new JSONObject());
            owaspCategories.put("a9", new JSONObject());
            owaspCategories.put("a10", new JSONObject());

            owaspCategories.getJSONObject("a1").put("MAJOR", 0);
            owaspCategories.getJSONObject("a1").put("MINOR", 0);
            owaspCategories.getJSONObject("a1").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a1").put("INFO", 0);
            owaspCategories.getJSONObject("a1").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a2").put("MAJOR", 0);
            owaspCategories.getJSONObject("a2").put("MINOR", 0);
            owaspCategories.getJSONObject("a2").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a2").put("INFO", 0);
            owaspCategories.getJSONObject("a2").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a3").put("MAJOR", 0);
            owaspCategories.getJSONObject("a3").put("MINOR", 0);
            owaspCategories.getJSONObject("a3").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a3").put("INFO", 0);
            owaspCategories.getJSONObject("a3").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a4").put("MAJOR", 0);
            owaspCategories.getJSONObject("a4").put("MINOR", 0);
            owaspCategories.getJSONObject("a4").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a4").put("INFO", 0);
            owaspCategories.getJSONObject("a4").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a5").put("MAJOR", 0);
            owaspCategories.getJSONObject("a5").put("MINOR", 0);
            owaspCategories.getJSONObject("a5").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a5").put("INFO", 0);
            owaspCategories.getJSONObject("a5").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a6").put("MAJOR", 0);
            owaspCategories.getJSONObject("a6").put("MINOR", 0);
            owaspCategories.getJSONObject("a6").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a6").put("INFO", 0);
            owaspCategories.getJSONObject("a6").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a7").put("MAJOR", 0);
            owaspCategories.getJSONObject("a7").put("MINOR", 0);
            owaspCategories.getJSONObject("a7").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a7").put("INFO", 0);
            owaspCategories.getJSONObject("a7").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a8").put("MAJOR", 0);
            owaspCategories.getJSONObject("a8").put("MINOR", 0);
            owaspCategories.getJSONObject("a8").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a8").put("INFO", 0);
            owaspCategories.getJSONObject("a8").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a9").put("MAJOR", 0);
            owaspCategories.getJSONObject("a9").put("MINOR", 0);
            owaspCategories.getJSONObject("a9").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a9").put("INFO", 0);
            owaspCategories.getJSONObject("a9").put("CRITICAL", 0);

            owaspCategories.getJSONObject("a10").put("MAJOR", 0);
            owaspCategories.getJSONObject("a10").put("MINOR", 0);
            owaspCategories.getJSONObject("a10").put("BLOCKER", 0);
            owaspCategories.getJSONObject("a10").put("INFO", 0);
            owaspCategories.getJSONObject("a10").put("CRITICAL", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return owaspCategories;
    }

    @Override
    public JSONArray getSecurityReportDataFromSq(String username, String password, String repoId, String reportType) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        String fixedRepoId = repoId.replaceAll("/", ":");

        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseAddress)
                .queryParam("project", fixedRepoId)
                .queryParam("standard", reportType)
                .queryParam("includeDistribution", false); // Don't need CWE distribution for now

        JSONObject secReportDataObj;
        JSONArray secReportDataArr = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, String.class);
            String responseBody = response.getBody();
            secReportDataObj = new JSONObject(responseBody);
            secReportDataArr = secReportDataObj.getJSONArray("categories");
        } catch (HttpClientErrorException e) {
            System.out.println("Failed to get security report data from SonarQube: " + e.getMessage());
            return null;
        }
        return secReportDataArr;
    }

    @Override
    public JSONArray getProductMatrixData() throws JSONException {
        // For each repo read from the role-matrix, get ALL the security report data
        ExcelToJSONConverter exl2JSON = new ExcelToJSONConverter("role-matrix.xlsx");
        JSONObject repos = exl2JSON.convertRoleMatrixToJSON();

        if (repos.toString().equals("{}")) {
            throw new IllegalArgumentException("Failed to initialize repo list.");
        }

        List<Repo> repoList = new LinkedList<>();

        JSONArray pmd = repos.getJSONArray("Product Matrix Data");

        return pmd;
    }

    @Override
    public List<Repo> getAllSecurityReports(String username, String password, int limit) {
        String reportType = "owaspTop10"; // TODO: Remove hardcoded code when adding other security certs
        // Get list of repos from Product Role Matrix
        List<Repo> repoList = new LinkedList<>();
        JSONArray pmd = null;
        try {
            pmd = getProductMatrixData();
        } catch (JSONException e) {
            System.out.println("Failed to parse PRM and get repo data.\n" + e.getMessage());
        }

        if (pmd != null) {
            if (limit == -1) {
                limit = pmd.length();
            }

            int count = limit; // Need to decouple counter from desired number of repos

            for (int i = 1; i <= count && i <= pmd.length() - 1; i++) { // Making sure not to go over the limit of repos that we get from the PRM
                try {
                    // Get individual repo & report data and create Repo object from it
                    JSONObject repoData = pmd.getJSONObject(i);
                    String repoId = repoData.getString("RPM");
                    JSONArray secReportData = getSecurityReportDataFromSq(username, password, repoId, reportType);
                    if (Objects.nonNull(secReportData)) {
                        JSONObject severityData = getSeverityData(username, password, repoId, reportType);
                        Repo repo = convertJsonToRepo(repoData, secReportData, severityData);
                        repoList.add(repo);
                    } else {
                        // Incrementing counter here so as to keep the total returned number of repos the same
                        // I.e. if 1 repo fails to get, get another until limit is met
                        System.out.println("Security Report Data is null: " + (repoList.size() + 1) + "/" + limit + ". Getting another...");
                        //count += 1;
                        System.out.println("i: " + i + ", count: " + ++count);
                    }
                } catch (JSONException e) {
                    System.out.println(e.getMessage());
                    return null;
                } finally {
                    // Wait for 1 seconds before calling the next call -> so I don't get blocked by sonarqube api
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt(); // Re-interrupt
                    }
                }
            }
        }

        System.out.println("Got list of " + repoList.size() + " repos.");
        return repoList;
    }

    @Override
    public Repo convertJsonToRepo(JSONObject repoData, JSONArray secReport, JSONObject severityData) throws JSONException {
        // Params from repoData
        String csa = repoData.getString("CSA");
        String cra = repoData.getString("CRA");
        String cna = repoData.getString("CNA");
        String name = repoData.getString("CXP");
        String uri = repoData.getString("RPM");
        String cxp = repoData.getString("Number");

        // Params from secReport
        List<OwaspReportData> owaspReportDataList = new ArrayList<>();
        for (int i = 0; i < secReport.length(); i++) {
            JSONObject temp = secReport.getJSONObject(i);
            int vulnerabilities = temp.getInt("vulnerabilities");
            int activeRules = temp.getInt("activeRules");
            int totalRules = temp.getInt("totalRules");
            int vulnerabilityRating = 0;
            if (temp.has("vulnerabilityRating")) {
                vulnerabilityRating = temp.getInt("vulnerabilityRating");
            }

            // get corresponding obj from severityData and marry that into Repo obj
            JSONObject severities = severityData.getJSONObject("a" + (i + 1));
            SeverityData blockers = new SeverityData(severities.getInt("BLOCKER"), SeverityLevel.BLOCKER);
            SeverityData criticals = new SeverityData(severities.getInt("CRITICAL"), SeverityLevel.CRITICAL);
            SeverityData majors = new SeverityData(severities.getInt("MAJOR"), SeverityLevel.MAJOR);
            SeverityData minors = new SeverityData(severities.getInt("MINOR"), SeverityLevel.MINOR);
            SeverityData infos = new SeverityData(severities.getInt("INFO"), SeverityLevel.INFO);
            SeverityData[] sevArr = {blockers, criticals, majors, minors, infos,};

            OwaspReportData owaspReport = new OwaspReportData(OwaspCategory.values()[i], vulnerabilities, vulnerabilityRating, activeRules, totalRules, sevArr);
            owaspReportDataList.add(owaspReport);
        }

        SecurityReportTotals securityReportTotals = new SecurityReportTotals(owaspReportDataList);

        Filters filter = new Filters(csa, cra, cna);
        Repo repo = new Repo(cxp, name, uri, filter, securityReportTotals);

        return repo;
    }

    @Override
    public void getAndStoreRepoSecurityReports(String username, String password, int limit) {
        if (!login(username, password)) {
            throw new IllegalArgumentException("Permission to Index DB denied.");
        }

        List<Repo> repos = getAllSecurityReports(username, password, limit);
        // for each repo, store in DB via dao layer
        for (Repo repo : repos) {
            // This can't find the bean and I have 0 clue why
            repoService.insertRepo(repo);
        }
    }
}
