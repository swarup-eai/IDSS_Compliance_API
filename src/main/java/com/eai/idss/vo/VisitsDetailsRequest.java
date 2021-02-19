package com.eai.idss.vo;

import java.util.List;

public class VisitsDetailsRequest {

	String visitStatus; // scheduled, pending, completed, legal actions
	String duration;
	List<String> scale;
	List<String> category;
	String subRegion;
	String region;
	private String userId;
	private String designation;
	
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getScale() {
		return scale;
	}
	public void setScale(List<String> scale) {
		this.scale = scale;
	}
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public String getVisitStatus() {
		return visitStatus;
	}
	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
}
