package com.dao.gerritrepo;

import com.dao.gerritrepo.model.Filters;
import com.dao.gerritrepo.model.Repo;
import com.dao.gerritrepo.model.securitycerts.SecurityReportTotals;
import com.dao.gerritrepo.model.securitycerts.owasp.OwaspCategory;
import com.dao.gerritrepo.model.securitycerts.owasp.OwaspReportData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = {MongoConfiguration.class})
class GerritRepoServiceTest {
    @Autowired
    private GerritRepoService service;

    private Repo mockRepo;

    @BeforeEach
    public void init() {
        OwaspReportData mockOwaspReportData = new OwaspReportData(OwaspCategory.A1, 2, 1, 2, 5, null);
        Filters filters = new Filters("Mediation Adaptions", "Shared Mediation Components", "Shared Node Mediation");
        mockRepo = new Repo(
                "TEST CXP: CXP 903 1393",
                "TEST NAME: Node Discovery Module",
                "TEST URI: com.ericsson.oss.mediation.nodediscovery/node-discovery-module",
                filters,
                new SecurityReportTotals(Collections.singletonList(mockOwaspReportData))
        );
    }

    @Test
    void contextLoads() {
        assertNotNull(service);
    }

    @Test
    void testThatServiceReturnsGerritRepos() {
        List<Repo> gerritRepos = service.getAllGerritRepos();
        assertThat(gerritRepos.size(), not(gerritRepos.isEmpty()));
        assertThat(gerritRepos.size(), greaterThan(0));
    }

    @Test
    void testThatServiceInsertsNewGerritRepo() {
        service.insertRepo(mockRepo);
        assertNotNull(service.getRepoByName(mockRepo.getName()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TEST NAME: Node Discovery Module"})
    void testThatServiceReturnsGetRepoByName(String name) {
        final Repo repo = service.getRepoByName(name);
        assertNotNull(repo);
    }

    @Test
    void testThatServiceReturnsGetRepoById() {
        final Repo repo = service.insertRepo(mockRepo);
        assertNotNull(service.getRepoById(repo.get_id()));
    }

    @Test
    void testThatServiceReturnsGerritReposWithTheirNamesOnly() {
        final List<String> repos = service.getAllGerritRepoNames();
        assertThat(repos.size(), not(repos.isEmpty()));
        assertThat(repos.size(), greaterThan(0));
    }

    @Test
    void testThatServiceReturnsGerritReposWithSecurityReportTotals() {
        final List<Repo> repos = service.getSecurityReportTotalsForRepos();
        assertThat(repos.size(), not(repos.isEmpty()));
        assertThat(repos.size(), greaterThan(0));
    }

    @Test
    void testThatServiceReturnsGerritReposWithoutSecurityReportTotals() {
        final List<Repo> repos = service.getAllGerritReposWithoutSecurityReportTotals();
        assertThat(repos.size(), not(repos.isEmpty()));
        assertThat(repos.size(), greaterThan(0));
    }
}
