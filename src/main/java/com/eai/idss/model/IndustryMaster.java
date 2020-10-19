package com.eai.idss.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "industryMaster")
public class IndustryMaster {

	@Id
	private String _id;
	private String region;
	private String industryName;
	private String category;
	private String scale;
	private String type;
	private int complianceScore;
	private Date establishedSince;
	private Date concentValidity;
	private int totalLegalActions;
	private int legalActionsPending;
	private Date lastVisited;
	
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
	public int getComplianceScore() {
		return complianceScore;
	}
	public void setComplianceScore(int complianceScore) {
		this.complianceScore = complianceScore;
	}
	public Date getEstablishedSince() {
		return establishedSince;
	}
	public void setEstablishedSince(Date establishedSince) {
		this.establishedSince = establishedSince;
	}
	public Date getConcentValidity() {
		return concentValidity;
	}
	public void setConcentValidity(Date concentValidity) {
		this.concentValidity = concentValidity;
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
	public Date getLastVisited() {
		return lastVisited;
	}
	public void setLastVisited(Date lastVisited) {
		this.lastVisited = lastVisited;
	}
	
}
