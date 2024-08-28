package com.restclient;

import com.dao.gerritrepo.MongoConfiguration;
import com.dao.gerritrepo.model.Repo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {RestTemplate.class, RestClientConfiguration.class, MongoConfiguration.class})
class RestClientImplTest {
    private final String OWASP_TOP_10 = "owaspTop10";

    @Autowired
    RestClient restClient;

    private TestRestTemplate restTemplate;

    @Value("${ericssonSqLogin}")
    private String ericssonSqLogin;

    @Value("${ericssonSqPassword}")
    private String ericssonSqPassword;

    @BeforeEach()
    void setup() {
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void contextLoads() {
        assertThat(restClient).isNotNull();
    }

    @Test
    void testGetSecurityReportDataOWASP() {
        String repoId = "com.ericsson.oss.mediation.netconf.handlers/netconf-session-inbound-config-handler";

        try {
            JSONArray result = restClient.getSecurityReportDataFromSq(ericssonSqLogin, ericssonSqPassword, repoId, OWASP_TOP_10);
            JSONObject jo = result.getJSONObject(0);

            assertTrue(jo.has("vulnerabilities"));

        } catch (JSONException e) {
            Assertions.fail();
        }

    }

    @Test
    void testGetSecurityReportDataInvalidReportType() {
        String repoId = "invalidRepoId";

        try {
            JSONArray result = restClient.getSecurityReportDataFromSq(ericssonSqLogin, ericssonSqPassword, repoId, OWASP_TOP_10);
            assertThat(result).isNull();

        } catch (JSONException e) {
            Assertions.fail();
        }

    }

    @Test
    void testGetSecurityReportDataInvalidRepoIdOWASP() {
        String repoId = "broken link here";

        try {
            JSONArray result = restClient.getSecurityReportDataFromSq(ericssonSqLogin, ericssonSqPassword, repoId, OWASP_TOP_10);

            // returns error message
            assertThat(result).isNull();

        } catch (JSONException e) {
            Assertions.fail();
        }

    }

    @Test
    void testGetAllSecurityReports() {
        try {
            List<Repo> result = restClient.getAllSecurityReports(ericssonSqLogin, ericssonSqPassword, 2);
            assertFalse(result.isEmpty());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testConvertJsonToRepo() {
        ExcelToJSONConverter exl2JSON = new ExcelToJSONConverter("role-matrix.xlsx");
        JSONObject repos = exl2JSON.convertRoleMatrixToJSON();

        try {
            JSONArray pmd = repos.getJSONArray("Product Matrix Data");
            JSONObject repoData = pmd.getJSONObject(3);
            JSONArray secReportData = restClient.getSecurityReportDataFromSq(ericssonSqLogin, ericssonSqPassword, "com.ericsson.oss.mediation.cm.events/sync-node-event", OWASP_TOP_10);
            JSONObject severityReport = restClient.getSeverityData(ericssonSqLogin, ericssonSqPassword, "com.ericsson.oss.mediation.cm.events/sync-node-event", OWASP_TOP_10);

            if (secReportData != null) {
                Repo repo = restClient.convertJsonToRepo(repoData, secReportData, severityReport);
                assertThat(repo).isNotNull();
            }
        } catch (JSONException e) {
            Assertions.fail();
        }

    }

    @Test
    void testCreateRepoWithSeverityReportData() throws JSONException {
        ExcelToJSONConverter exl2JSON = new ExcelToJSONConverter("role-matrix.xlsx");
        JSONObject repos = exl2JSON.convertRoleMatrixToJSON();
        Repo repo = null;
        try {
            JSONArray pmd = repos.getJSONArray("Product Matrix Data");
            String repoId = "com.ericsson.oss.presentation.server.fm.alarmcontroldisplayservice/alarmcontroldisplayservice";
            int index = exl2JSON.findIndexOfRepo(repoId);
            if (index == -1) Assertions.fail();
            JSONObject repoData = pmd.getJSONObject(index);
            JSONArray secReportData = restClient.getSecurityReportDataFromSq(ericssonSqLogin, ericssonSqPassword, repoId, OWASP_TOP_10);
            JSONObject severityReport = restClient.getSeverityData(ericssonSqLogin, ericssonSqPassword, repoId, OWASP_TOP_10);
            if (secReportData != null) {
                repo = restClient.convertJsonToRepo(repoData, secReportData, severityReport);
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        assertThat(repo).isNotNull();
    }

    @Test
    void testGetSeverityData() {
        String repoId = "com.ericsson.oss.presentation.server.fm.alarmcontroldisplayservice/alarmcontroldisplayservice";
        try {
            JSONObject severityReport = restClient.getSeverityData(ericssonSqLogin, ericssonSqPassword, repoId, OWASP_TOP_10);
            assertThat(severityReport).isNotNull();
        } catch (JSONException e) {
            Assertions.fail();
        }
    }

    @Test
    void testGetSeverityDataInvalid() {
        String repoId = "Broken Link!!!";
        try {
            JSONObject severityReport = restClient.getSeverityData(ericssonSqLogin, ericssonSqPassword, repoId, OWASP_TOP_10);
            assertThat(severityReport).isNotNull();
        } catch (JSONException e) {
            Assertions.fail();
        }
    }

    @Test
    void testGetAndStoreRepoSecurityReports() {
        //check if the DAO contains the added repo
        try {
            // TODO: Should mock DAO here instead of making persistent changes to current database
            restClient.getAndStoreRepoSecurityReports(ericssonSqLogin, ericssonSqPassword, 2);
            RestClientImpl impl = (RestClientImpl) restClient;
            assertNotEquals(0, impl.repoService.getAllGerritRepos().size());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testLoginFail() {
        boolean result = restClient.login("", "");
        assertFalse(result);
    }

    @Test
    void testLoginSuccessful() {
        boolean result = restClient.login(ericssonSqLogin, ericssonSqPassword);
        assertTrue(result);
    }
}
