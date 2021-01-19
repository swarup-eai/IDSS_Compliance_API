package com.eai.idss.vo;

import java.util.List;

public class ConsentDetailsRequest {

	String status; //applied, approved, pending, rejected
	Integer duration;
	List<String> scale;
	List<String> category;
	String subRegion;
	String region;
	String consentStatus; //expansion, renewal, operate, establish
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
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
	public String getConsentStatus() {
		return consentStatus;
	}
	public void setConsentStatus(String consentStatus) {
		this.consentStatus = consentStatus;
	}
	
}
