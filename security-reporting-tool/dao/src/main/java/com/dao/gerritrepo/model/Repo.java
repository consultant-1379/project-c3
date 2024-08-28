package com.dao.gerritrepo.model;

import com.dao.gerritrepo.model.securitycerts.SecurityReportTotals;
import com.dao.gerritrepo.model.securitycerts.severity.SeverityData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gerritrepos")
public class Repo {
    @Id
    private String _id;
    private String cxp;
    private String name;
    private String uri;
    private Filters filters;
    private SecurityReportTotals securityReportTotals;

    public Repo(String cxp, String name, String uri, Filters filters, SecurityReportTotals securityReportTotals) {
        this.filters = filters; // made up of CSA/CRA/CNA
        this.name = name; // e.g. ip ne discovery service
        this.uri = uri; // e.g. com.ericsson.oss.services.ipcm.discovery/ip-ne-discovery-service
        this.cxp = cxp; // e.g. CXP 903 2341
        this.securityReportTotals = securityReportTotals;
    }

    public String get_id() {
        return _id;
    }

    public String getCxp() {
        return cxp;
    }

    public void setCxp(String cxp) {
        this.cxp = cxp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public SecurityReportTotals getSecurityReportTotals() {
        return securityReportTotals;
    }

    public void setSecurityReportTotals(SecurityReportTotals securityReportTotals) {
        this.securityReportTotals = securityReportTotals;
    }

}
