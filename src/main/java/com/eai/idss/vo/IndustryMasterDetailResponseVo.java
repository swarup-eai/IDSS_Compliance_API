package com.eai.idss.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Date;

public class IndustryMasterDetailResponseVo {
    private String industryName;
    private String region;
    private String category;
    private String scale;
    private String type;
    private int cscore;
    private int pendingLegalAction;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date commissioningDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date consentValidityDate;
    private int totalLegalActions;
    private int legalActionsPending;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate lastVisited;

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

    public Date getConsentValidityDate() {
        return consentValidityDate;
    }
    public void setConsentValidityDate(Date consentValidityDate) {
        this.consentValidityDate = consentValidityDate;
    }
    public int getTotalLegalActions() {
        return totalLegalActions;
    }
    public void setTotalLegalActions(int totalLegalActions) {
        this.totalLegalActions = totalLegalActions;
    }
    public int getLegalActionsPending() {
        return legalActionsPending;
    }
    public void setLegalActionsPending(int legalActionsPending) {
        this.legalActionsPending = legalActionsPending;
    }
    public LocalDate getLastVisited() {
        return lastVisited;
    }
    public void setLastVisited(LocalDate lastVisited) {
        this.lastVisited = lastVisited;
    }
    public Date getCommissioningDate() {
        return commissioningDate;
    }
    public void setCommissioningDate(Date commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

}
