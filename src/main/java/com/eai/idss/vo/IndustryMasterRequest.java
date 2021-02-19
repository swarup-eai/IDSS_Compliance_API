package com.eai.idss.vo;

public class IndustryMasterRequest {

	private String region;
	private String subRegion;
	private String scale;
	private String type;
	private String category;
	private String complianceScore;
	private String pendingCases;
	private String legalActions;
	private String industryName;
	
	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}
	public String getComplianceScore() {
		return complianceScore;
	}
	public void setComplianceScore(String complianceScore) {
		this.complianceScore = complianceScore;
	}
	public String getPendingCases() {
		return pendingCases;
	}
	public void setPendingCases(String pendingCases) {
		this.pendingCases = pendingCases;
	}
	public String getLegalActions() {
		return legalActions;
	}
	public void setLegalActions(String legalActions) {
		this.legalActions = legalActions;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	
}
