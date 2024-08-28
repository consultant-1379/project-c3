package com.restserver;

import com.dao.gerritrepo.model.Filters;
import com.dao.gerritrepo.model.Repo;
import com.dao.gerritrepo.model.securitycerts.SecurityReportTotals;
import com.dao.gerritrepo.model.securitycerts.owasp.OwaspCategory;
import com.dao.gerritrepo.model.securitycerts.owasp.OwaspReportData;
import com.restclient.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RepoRestControllerTests {
    private Filters filters;
    private OwaspReportData mockOwaspReportData;
    private Repo mockRepo;
    private List<Repo> mockRepoList;

    @Autowired
    RestClient restClient;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Value("${ericssonSqLogin}")
    private String ericssonSqLogin;

    @Value("${ericssonSqPassword}")
    private String ericssonSqPassword;

    @Test
    public void contextLoads() {
        assertThat(restClient).isNotNull();
    }

    @BeforeEach
    void setupMockData() {
        filters = new Filters("Mediation Adaptions", "Shared Mediation Components", "Shared Node Mediation");
        mockOwaspReportData = new OwaspReportData(OwaspCategory.A1, 2, 1, 2, 5, null);
        mockRepo = new Repo(
                "CXP 903 1393",
                "Node Discovery Module",
                "com.ericsson.oss.mediation.nodediscovery/node-discovery-module",
                filters,
                new SecurityReportTotals(Collections.singletonList(mockOwaspReportData))
        );
        mockRepoList = new ArrayList<Repo>(Collections.singletonList(mockRepo));
    }

    @Test
    void testRepoInit() {
        assertEquals(OwaspCategory.A1, mockOwaspReportData.getCategory());
        assertEquals("Mediation Adaptions", mockRepo.getFilters().getCSA());
        assertEquals("Node Discovery Module", mockRepo.getName());
        assertEquals("com.ericsson.oss.mediation.nodediscovery/node-discovery-module", mockRepo.getUri());
    }

    @Test
    public void testLoginCallInvalidUsername() {
        String url = "http://localhost:" + port + "/api/v1/login";
        User user = new User("", "");
        ResponseEntity<String> response = restTemplate.postForEntity(url, user, String.class);
        assertThat(response.getStatusCode().getReasonPhrase().equals("Unauthorized")).isTrue();
    }

    @Test
    public void testLoginCallSuccessful() {
        String url = "http://localhost:" + port + "/api/v1/login";
        User user = new User(ericssonSqLogin, ericssonSqPassword);
        ResponseEntity<String> response = restTemplate.postForEntity(url, user, String.class);
        assertThat(response.getStatusCode().getReasonPhrase().equals("OK")).isTrue();
    }

    @Test
    public void testGetAllRepos() {
        String url = "http://localhost:" + port + "/api/v1/repos";
        List<Repo> response = restTemplate.getForObject(url, List.class);
        assertThat(response.size()).isGreaterThan(0);
    }
}
