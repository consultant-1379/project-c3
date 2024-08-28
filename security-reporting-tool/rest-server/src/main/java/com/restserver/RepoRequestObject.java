package com.restserver;

import com.dao.gerritrepo.model.Filters;
import com.dao.gerritrepo.model.Repo;
import com.dao.gerritrepo.model.securitycerts.SecurityReportTotals;

public class RepoRequestObject extends Repo {
    public RepoRequestObject(String cxp, String name, String uri, Filters filters, SecurityReportTotals securityReportTotals) {
        super(cxp, name, uri, filters, securityReportTotals);
    }

    @Override
    public String get_id() {
        return super.get_id();
    }

    @Override
    public String getCxp() {
        return super.getCxp();
    }

    @Override
    public void setCxp(String cxp) {
        super.setCxp(cxp);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public Filters getFilters() {
        return super.getFilters();
    }

    @Override
    public void setFilters(Filters filters) {
        super.setFilters(filters);
    }

    @Override
    public String getUri() {
        return super.getUri();
    }

    @Override
    public void setUri(String uri) {
        super.setUri(uri);
    }

    @Override
    public SecurityReportTotals getSecurityReportTotals() {
        return super.getSecurityReportTotals();
    }

    @Override
    public void setSecurityReportTotals(SecurityReportTotals securityReportTotals) {
        super.setSecurityReportTotals(securityReportTotals);
    }
}
