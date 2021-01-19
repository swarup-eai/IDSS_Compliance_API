package com.eai.idss.vo;

public class SkuDetailVo {
    private Double productQuantity;
    private Double benchmarkValues;
    private String productCategory;

    public void setProductQuantity(Double productQuantity) {
        this.productQuantity = productQuantity;
    }
    public Double getProductQuantity() {
        return productQuantity;
    }
    public void setBenchmarkValues(Double benchmarkValues) {
        this.benchmarkValues = benchmarkValues;
    }
    public Double getBenchmarkValues() {
        return benchmarkValues;
    }
    public String getProductCategory() {
        return productCategory;
    }
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
