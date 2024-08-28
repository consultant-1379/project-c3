package com.dao.gerritrepo.model.securitycerts.owasp;

import com.dao.gerritrepo.model.securitycerts.severity.SeverityData;

public class OwaspReportData {
    private OwaspCategory category;
    private int vulnerabilities, vulnerabilityRating, activeRules, totalRules;
    private SeverityData[] severityData;

    public OwaspReportData(OwaspCategory category, int vulnerabilities, int vulnerabilityRating, int activeRules, int totalRules, SeverityData[] severityData) {
        this.category = category;
        this.vulnerabilities = vulnerabilities;
        this.vulnerabilityRating = vulnerabilityRating;
        this.activeRules = activeRules;
        this.totalRules = totalRules;
        this.severityData = severityData;
    }

    public OwaspCategory getCategory() {
        return category;
    }

    public int getVulnerabilities() {
        return vulnerabilities;
    }

    public int getVulnerabilityRating() {
        return vulnerabilityRating;
    }

    public int getActiveRules() {
        return activeRules;
    }

    public int getTotalRules() {
        return totalRules;
    }

    public SeverityData[] getSeverities() {
        return severityData;
    }
}
