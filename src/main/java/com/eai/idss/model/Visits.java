package com.eai.idss.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Visit_master")
public class Visits {

	private long industryId;
	private long visitId;
	private long elapsedDays;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate visitedDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate schduledOn;
	private String industryName;
	private String visitStatus;
	private String scale;
	private String region;
	private String type;
	private String category;
	private double cScore;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate reportCreatedOn;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate legalDirectionIssuedOn;

	private Double latitude;
	private Double longitude;
	private String priority;
	
	@Field("reportingToName")
	private String sroName;
	private String subRegion;
	
	@Field("adminName")
	private String visitedBy;

	private String legalDirection;
	
	public String getVisitedBy() {
		return visitedBy;
	}
	public void setVisitedBy(String visitedBy) {
		this.visitedBy = visitedBy;
	}
	public String getSroName() {
		return sroName;
	}
	public void setSroName(String sroName) {
		this.sroName = sroName;
	}
	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}

	public long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}
	public long getVisitId() {
		return visitId;
	}
	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}
	public LocalDate getReportCreatedOn() {
		return reportCreatedOn;
	}
	public void setReportCreatedOn(LocalDate reportCreatedOn) {
		this.reportCreatedOn = reportCreatedOn;
	}
	public LocalDate getLegalDirectionIssuedOn() {
		return legalDirectionIssuedOn;
	}
	public void setLegalDirectionIssuedOn(LocalDate legalDirectionIssuedOn) {
		this.legalDirectionIssuedOn = legalDirectionIssuedOn;
	}
	public long getElapsedDays() {
		return elapsedDays;
	}
	public void setElapsedDays(long elapsedDays) {
		this.elapsedDays = elapsedDays;
	}
	public LocalDate getVisitedDate() {
		return visitedDate;
	}
	public void setVisitedDate(LocalDate visitedDate) {
		this.visitedDate = visitedDate;
	}
	public LocalDate getSchduledOn() {
		return schduledOn;
	}
	public void setSchduledOn(LocalDate schduledOn) {
		this.schduledOn = schduledOn;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getVisitStatus() {
		return visitStatus;
	}
	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
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
	public double getcScore() {
		return cScore;
	}
	public void setcScore(double cScore) {
		this.cScore = cScore;
	}

	public Double  getLatitude() {
		return latitude;
	}
	public void setLatitude(Double  latitude) {
		this.latitude = latitude;
	}
	public Double  getLongitude() {
		return longitude;
	}
	public void setLongitude(Double  longitude) {
		this.longitude = longitude;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getPriority() {
		return priority;
	}
	public String getLegalDirection() {
		return legalDirection;
	}
	public void setLegalDirection(String legalDirection) {
		this.legalDirection = legalDirection;
	}
	
}
