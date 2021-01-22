package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "benchmarkingSourceData")
public class BenchmarkingSourceData {
    private Double productionQty;
    private String productName;
    private String productCategory;
    private String industryType;
    private String industryName;
    private Double categoryCode;
    private String airPollutantsGroup;
    private Double concentration;
    private Double capacityOfEtp;
    private Double capacityOfStp;
    private Double treatedEffluentBod;
    private Double treatedEffluentCod;
    private Double treatedEffluentTds;
    private Double treatedEffluentSs;
    private Double treatedEffluentPh;
    public void setProductionQty(Double productionQty) {
        this.productionQty = productionQty;
    }
    public Double getProductionQty() {
        return productionQty;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getIndustryType() {
        return industryType;
    }
    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }
    public String getIndustryName() {
        return industryName;
    }
    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
    public void setCategoryCode(Double categoryCode) {
        this.categoryCode = categoryCode;
    }
    public Double getCategoryCode() {
        return categoryCode;
    }
    public String getProductCategory() {
        return productCategory;
    }
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public String getAirPollutantsGroup() {
        return airPollutantsGroup;
    }
    public void setAirPollutantsGroup(String airPollutantsGroup) {
        this.airPollutantsGroup = airPollutantsGroup;
    }
    public void setConcentration(Double concentration) {
        this.concentration = concentration;
    }
    public Double getConcentration() {
        return concentration;
    }
    public void setCapacityOfEtp(Double capacityOfEtp) {
        this.capacityOfEtp = capacityOfEtp;
    }
    public Double getCapacityOfEtp() {
        return capacityOfEtp;
    }
    public void setCapacityOfStp(Double capacityOfStp) {
        this.capacityOfStp = capacityOfStp;
    }
    public Double getCapacityOfStp() {
        return capacityOfStp;
    }

    public void setTreatedEffluentBod(Double treatedEffluentBod) {
        this.treatedEffluentBod = treatedEffluentBod;
    }
    public Double getTreatedEffluentBod() {
        return treatedEffluentBod;
    }

    public void setTreatedEffluentCod(Double treatedEffluentCod) {
        this.treatedEffluentCod = treatedEffluentCod;
    }
    public Double getTreatedEffluentCod() {
        return treatedEffluentCod;
    }

    public void setTreatedEffluentTds(Double treatedEffluentTds) {
        this.treatedEffluentTds = treatedEffluentTds;
    }
    public Double getTreatedEffluentTds() {
        return treatedEffluentTds;
    }

    public void setTreatedEffluentSs(Double treatedEffluentSs) {
        this.treatedEffluentSs = treatedEffluentSs;
    }
    public Double getTreatedEffluentSs() {
        return treatedEffluentSs;
    }


    public void setTreatedEffluentPh(Double treatedEffluentPh) {
        this.treatedEffluentPh = treatedEffluentPh;
    }
    public Double getTreatedEffluentPh() {
        return treatedEffluentPh;
    }
}
