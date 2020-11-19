package com.eai.idss.vo;

import java.util.List;

public class ComlianceScoreFilter {

	private String industryName;
	
	private String duration;
	
	List<String> industryNameList;

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public List<String> getIndustryNameList() {
		return industryNameList;
	}

	public void setIndustryNameList(List<String> industryNameList) {
		this.industryNameList = industryNameList;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	
	
}
