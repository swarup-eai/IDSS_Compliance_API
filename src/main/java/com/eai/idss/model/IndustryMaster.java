package com.eai.idss.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "industryMaster")
public class IndustryMaster {

	@Id
	private String _id;
	private long industryId;
	private String region;
	private String subRegion;
	private String industryName;
	private String category;
	private String scale;
	private String type;
	private int cscore;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date commissioningDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date consentValidityDate;
	private int totalLegalActions;
	private int legalActionsPending;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate lastVisited;

	private String latitudeDegree;
	private String longitudeDegree;

	public long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}
	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
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

	public Date getCommissioningDate() {
		return commissioningDate;
	}
	public void setCommissioningDate(Date commissioningDate) {
		this.commissioningDate = commissioningDate;
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

	public String  getLatitudeDegree() {
		return latitudeDegree;
	}
	public void setLatitudeDegree(String  latitudeDegree) {
		this.latitudeDegree = latitudeDegree;
	}
	public String  getLongitudeDegree() {
		return longitudeDegree;
	}
	public void setLongitudeDegree(String  longitudeDegree) {
		this.longitudeDegree = longitudeDegree;
	}
	
}
