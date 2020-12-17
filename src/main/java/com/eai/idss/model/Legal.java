package com.eai.idss.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "legalDataMaster")
public class Legal {

	@JsonFormat(pattern="yyyy-MM-dd")
	private Date visitedDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date issuedOn;
	private String industryName;
	private String complianceStatus;
	private String owner;
	private String scale;
	private String region;
	private String type;
	private String category;
	private String subRegion;
	private double totalLegalActionsCreated;
	
	public double getTotalLegalActionsCreated() {
		return totalLegalActionsCreated;
	}
	public void setTotalLegalActionsCreated(double totalLegalActionsCreated) {
		this.totalLegalActionsCreated = totalLegalActionsCreated;
	}
	public Date getVisitedDate() {
		return visitedDate;
	}
	public void setVisitedDate(Date visitedDate) {
		this.visitedDate = visitedDate;
	}
	public Date getIssuedOn() {
		return issuedOn;
	}
	public void setIssuedOn(Date issuedOn) {
		this.issuedOn = issuedOn;
	}
	
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getComplianceStatus() {
		return complianceStatus;
	}
	public void setComplianceStatus(String complianceStatus) {
		this.complianceStatus = complianceStatus;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
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
	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}

}
