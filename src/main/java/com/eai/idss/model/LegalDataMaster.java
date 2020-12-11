package com.eai.idss.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "legalDataMaster")
public class LegalDataMaster {

	@Id
	private String _id;
	private String industryName;
	private String sroName;
	private int totalLegalActionsCreated;
	//@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate  visitedDate;
	private int complianceScore;

	public LegalDataMaster() {

	}

	public LegalDataMaster(String _id, String industryName, String sroName, int totalLegalActionsCreated,
			LocalDate visitedDate,int complianceScore) {
		super();
		this._id = _id;
		this.industryName = industryName;
		this.sroName = sroName;
		this.totalLegalActionsCreated = totalLegalActionsCreated;
		this.visitedDate = visitedDate;
		this.complianceScore = complianceScore;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getSroName() {
		return sroName;
	}

	public void setSroName(String sroName) {
		this.sroName = sroName;
	}

	public int getTotalLegalActionsCreated() {
		return totalLegalActionsCreated;
	}

	public void setTotalLegalActionsCreated(int totalLegalActionsCreated) {
		this.totalLegalActionsCreated = totalLegalActionsCreated;
	}

	public LocalDate getVisitedDate() {
		return visitedDate;
	}

	public void setVisitedDate(LocalDate visitedDate) {
		this.visitedDate = visitedDate;
	}

	public int getComplianceScore() {
		return complianceScore;
	}

	public void setComplianceScore(int complianceScore) {
		this.complianceScore = complianceScore;
	}

}
