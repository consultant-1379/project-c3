package com.dao.gerritrepo;

import com.dao.gerritrepo.model.Repo;
import com.dao.gerritrepo.model.securitycerts.severity.SeverityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GerritRepoDAO {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Repo> getAllGerritRepos() {
        return mongoTemplate.findAll(Repo.class);
    }

    public List<Repo> getSecurityReportTotalsForRepos() {
        List<Repo> repos = getAllGerritRepos();
        return repos.stream().map(repo -> {
            repo.setFilters(null);
            return repo;
        }).collect(Collectors.toList());
    }

    public Repo insertRepo(Repo repo) {
        return mongoTemplate.save(repo);
    }

    public Repo getRepoByName(String name) {
        Query byName = new Query();
        byName.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.findOne(byName, Repo.class);
    }

    public Repo getRepoById(String id) {
        return mongoTemplate.findById(id, Repo.class);
    }

    public List<String> getAllGerritRepoNames() {
        List<Repo> allGerritRepos = getAllGerritRepos();
        return allGerritRepos.stream().map(Repo::getName).collect(Collectors.toList());
    }

    public List<Repo> getAllGerritReposWithoutSecurityReportTotals() {
        List<Repo> repos = getAllGerritRepos();

        return repos.stream().map(repo -> {
            repo.setSecurityReportTotals(null);
            return repo;
        }).collect(Collectors.toList());
    }

    public List<SeverityData> getSQSeverityData() {
        return null;
    }
}
