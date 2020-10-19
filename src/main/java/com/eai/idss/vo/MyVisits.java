package com.eai.idss.vo;

import java.util.List;

public class MyVisits {
	private String date;
	private List<MyVisitsIndustries> industries;
	
	public List<MyVisitsIndustries> getIndustries() {
		return industries;
	}
	public void setIndustries(List<MyVisitsIndustries> industries) {
		this.industries = industries;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
