package com.eai.idss.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Visit_master")
public class Visits {

	private long elapsedDays;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date visitedDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate schduledOn;
	private String industryName;
	private String visitStatus;
	private String scale;
	private String region;
	private String type;
	private String category;
	private int cScore;
	
	public long getElapsedDays() {
		return elapsedDays;
	}
	public void setElapsedDays(long elapsedDays) {
		this.elapsedDays = elapsedDays;
	}
	public Date getVisitedDate() {
		return visitedDate;
	}
	public void setVisitedDate(Date visitedDate) {
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
	public int getcScore() {
		return cScore;
	}
	public void setcScore(int cScore) {
		this.cScore = cScore;
	}
	
}
