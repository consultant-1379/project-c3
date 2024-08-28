package com.dao.gerritrepo.model;

public class Filters {
    private String CSA, CRA, CNA;

    public Filters(String CSA, String CRA, String CNA) {
        this.CSA = CSA;
        this.CRA = CRA;
        this.CNA = CNA;
    }

    public String getCSA() {
        return CSA;
    }

    public void setCSA(String CSA) {
        this.CSA = CSA;
    }

    public String getCRA() {
        return CRA;
    }

    public void setCRA(String CRA) {
        this.CRA = CRA;
    }

    public String getCNA() {
        return CNA;
    }

    public void setCNA(String CNA) {
        this.CNA = CNA;
    }
}
