package com.eai.idss.vo;

import java.util.List;

public class PollutionScoreFilter {

	private long industryId;
	private List<String> parametersList;
	private String fromDate;
	private String toDate;
	private int hours;
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public List<String> getParametersList() {
		return parametersList;
	}

	public void setParametersList(List<String> parametersList) {
		this.parametersList = parametersList;
	}

	public long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}


}
