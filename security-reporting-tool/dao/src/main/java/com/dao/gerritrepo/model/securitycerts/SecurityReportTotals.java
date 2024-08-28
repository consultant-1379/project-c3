package com.dao.gerritrepo.model.securitycerts;


import com.dao.gerritrepo.model.securitycerts.owasp.OwaspReportData;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SecurityReportTotals {
    //    @JsonProperty("owaspReportDataList")
    private List<OwaspReportData> owaspReportDataList;

    public SecurityReportTotals(List<OwaspReportData> owaspReportDataList) {
        this.owaspReportDataList = owaspReportDataList;
    }

    public List<OwaspReportData> getOwaspReportDataList() {
        return owaspReportDataList;
    }

    public void setOwaspReportDataList(List<OwaspReportData> owaspReportDataList) {
        this.owaspReportDataList = owaspReportDataList;
    }

    public SecurityReportTotals() {
    }
}
