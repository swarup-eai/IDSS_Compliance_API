package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

public class PollutionScoreFilter {

	private String industryName;
	private String companyname;
	private Double industryId;
	private String site_id;
	private List<String> parametersList;

	private List<Map<String, String>> paramList;

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public List<String> getParametersList() {
		return parametersList;
	}

	public void setParametersList(List<String> parametersList) {
		this.parametersList = parametersList;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public Double getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Double industryId) {
		this.industryId = industryId;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public List<Map<String, String>> getParamList() {
		return paramList;
	}

	public void setParamList(List<Map<String, String>> paramList) {
		this.paramList = paramList;
	}

}
