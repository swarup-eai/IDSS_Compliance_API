package com.eai.idss.vo;

public class IndustryMasterDetailResponseVo {
    private String industryName;
    private String region;
    private String category;
    private String scale;
    private String type;
    private int cscore;
    private int pendingLegalAction;

    public String getIndustryName() {
        return industryName;
    }
    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getScale() {
        return scale;
    }
    public void setScale(String scale) {
        this.scale = scale;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getCscore() {
        return cscore;
    }
    public void setCscore(int cscore) {
        this.cscore = cscore;
    }
    public int getPendingLegalAction() {
        return pendingLegalAction;
    }
    public void setPendingLegalAction(int pendingLegalAction) {
        this.pendingLegalAction = pendingLegalAction;
    }
}
