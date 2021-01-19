package com.eai.idss.vo;

import java.util.List;

public class LegalDetailsRequest {

	String action; //WN, SCN, PD, CD
	String duration;
	List<String> category;
	String subRegion;
	String region;
	List<String> scale;
	
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public List<String> getScale() {
		return scale;
	}
	public void setScale(List<String> scale) {
		this.scale = scale;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
