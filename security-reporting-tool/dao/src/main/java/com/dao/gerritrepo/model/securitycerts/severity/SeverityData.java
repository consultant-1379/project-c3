package com.dao.gerritrepo.model.securitycerts.severity;

public class SeverityData {
    private SeverityLevel severityLevel;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public SeverityLevel getSeverity() {
        return severityLevel;
    }

    public void setSeverity(SeverityLevel severityLevel) {
        this.severityLevel = severityLevel;
    }

    public SeverityData(int count, SeverityLevel severityLevel) {
        this.severityLevel = severityLevel;
        this.count = count;
    }
}
