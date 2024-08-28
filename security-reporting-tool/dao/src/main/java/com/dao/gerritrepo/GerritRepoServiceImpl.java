package com.dao.gerritrepo;

import com.dao.gerritrepo.model.Repo;
import com.dao.gerritrepo.model.securitycerts.severity.SeverityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ComponentScan(basePackages = {"com.dao"}, basePackageClasses = {MongoConfiguration.class})
public class GerritRepoServiceImpl implements GerritRepoService {
    @Autowired
    private GerritRepoDAO dao;

    public List<Repo> getAllGerritRepos() {
        return dao.getAllGerritRepos();
    }

    @Override
    public List<Repo> getSecurityReportTotalsForRepos() {
        return dao.getSecurityReportTotalsForRepos();
    }

    @Override
    public Repo insertRepo(Repo repo) {
        if (repo != null) {
            dao.insertRepo(repo);
        } else {
            return null;
        }
        return getRepoByName(repo.getName());
    }

    @Override
    public Repo getRepoByName(String name) {
        return dao.getRepoByName(name);
    }

    @Override
    public Repo getRepoById(String id) {
        return dao.getRepoById(id);
    }

    @Override
    public List<String> getAllGerritRepoNames() {
        return dao.getAllGerritRepoNames();
    }

    @Override
    public List<Repo> getAllGerritReposWithoutSecurityReportTotals() {
        return dao.getAllGerritReposWithoutSecurityReportTotals();
    }

    @Override
    public List<SeverityData> getSQSeverityData() {
        return dao.getSQSeverityData();
    }

}
