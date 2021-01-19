package com.eai.idss.vo;

import java.util.List;

public class PollutionScoreFilter {

	private Double industryId;
	private List<String> parametersList;

	public List<String> getParametersList() {
		return parametersList;
	}

	public void setParametersList(List<String> parametersList) {
		this.parametersList = parametersList;
	}

	public Double getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Double industryId) {
		this.industryId = industryId;
	}


}
