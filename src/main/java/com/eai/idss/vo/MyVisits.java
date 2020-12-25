package com.eai.idss.vo;

import java.util.List;

public class MyVisits {
	private String dateView;
	private List<MyVisitsIndustries> visitDetail;
	public String getDateView() {
		return dateView;
	}
	public void setDateView(String dateView) {
		this.dateView = dateView;
	}
	public List<MyVisitsIndustries> getVisitDetail() {
		return visitDetail;
	}
	public void setVisitDetail(List<MyVisitsIndustries> visitDetail) {
		this.visitDetail = visitDetail;
	}
	
}
