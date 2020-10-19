package com.eai.idss.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Consent")
public class Consent {

	@JsonFormat(pattern="yyyy-MM-dd")
	private Date created;
	private String status;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date consentApprovalDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date consentValidityDate;
	private String scale;
	private String industryname;
	private String region;
	private String type;
	private String category;
	private String consentStatus;
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getConsentApprovalDate() {
		return consentApprovalDate;
	}
	public void setConsentApprovalDate(Date consentApprovalDate) {
		this.consentApprovalDate = consentApprovalDate;
	}
	public Date getConsentValidityDate() {
		return consentValidityDate;
	}
	public void setConsentValidityDate(Date consentValidityDate) {
		this.consentValidityDate = consentValidityDate;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getIndustryname() {
		return industryname;
	}
	public void setIndustryname(String industryname) {
		this.industryname = industryname;
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
	public String getConsentStatus() {
		return consentStatus;
	}
	public void setConsentStatus(String consentStatus) {
		this.consentStatus = consentStatus;
	}
	
	
}
