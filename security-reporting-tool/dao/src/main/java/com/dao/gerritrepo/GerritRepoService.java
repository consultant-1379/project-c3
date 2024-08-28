package com.dao.gerritrepo;

import com.dao.gerritrepo.model.Repo;
import com.dao.gerritrepo.model.securitycerts.severity.SeverityData;

import java.util.List;

public interface GerritRepoService {
    List<Repo> getAllGerritRepos();

    List<Repo> getSecurityReportTotalsForRepos();

    Repo insertRepo(Repo repo);

    Repo getRepoByName(String name);

    Repo getRepoById(String id);

    List<String> getAllGerritRepoNames();

    List<Repo> getAllGerritReposWithoutSecurityReportTotals();

    List<SeverityData> getSQSeverityData();

}
