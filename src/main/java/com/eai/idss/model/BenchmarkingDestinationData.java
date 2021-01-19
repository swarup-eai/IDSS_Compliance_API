package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "benchmarkingDestinationData")
public class BenchmarkingDestinationData {
    private Double categoryCode;
    private String industryType;
    private Double avgQty;
    private Double avgNox;
    private Double avgSo2;
    private Double avgPm;
    private Double avgBod;
    private Double avgCod;
    private Double avgTds;
    private Double avgTss;
    private Double avgPh;
    private Double avgEtp;
    private Double avgStp;

    public void setCategoryCode(Double categoryCode) {
        this.categoryCode = categoryCode;
    }
    public Double getCategoryCode() {
        return categoryCode;
    }
    public String getIndustryType() {
        return industryType;
    }
    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public void setAvgQty(Double avgQty) {
        this.avgQty = avgQty;
    }
    public Double getAvgQty() {
        return avgQty;
    }

    public void setAvgNox(Double avgNox) {
        this.avgNox = avgNox;
    }
    public Double getAvgNox() {
        return avgNox;
    }

    public void setAvgSo2(Double avgSo2) {
        this.avgSo2 = avgSo2;
    }
    public Double getAvgSo2() {
        return avgSo2;
    }

    public void setAvgPm(Double avgPm) {
        this.avgPm = avgPm;
    }
    public Double getAvgPm() {
        return avgPm;
    }

    public void setAvgBod(Double avgBod) {
        this.avgBod = avgBod;
    }
    public Double getAvgBod() {
        return avgBod;
    }

    public void setAvgCod(Double avgCod) {
        this.avgCod = avgCod;
    }
    public Double getAvgCod() {
        return avgCod;
    }

    public void setAvgTds(Double avgTds) {
        this.avgTds = avgTds;
    }
    public Double getAvgTds() {
        return avgTds;
    }

    public void setAvgTss(Double avgTss) {
        this.avgTss = avgTss;
    }
    public Double getAvgTss() {
        return avgTss;
    }

    public void setAvgPh(Double avgPh) {
        this.avgPh = avgPh;
    }
    public Double getAvgPh() {
        return avgPh;
    }

    public void setAvgEtp(Double avgEtp) {
        this.avgEtp = avgEtp;
    }
    public Double getAvgEtp() {
        return avgEtp;
    }

    public void setAvgStp(Double avgStp) {
        this.avgStp = avgStp;
    }
    public Double getAvgStp() {
        return avgStp;
    }

}
