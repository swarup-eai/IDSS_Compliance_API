package com.eai.idss.vo;

import java.util.List;

import com.eai.idss.model.Visits;

public class VisitsPaginationResponseVo {

	private List<Visits> visitsList;
	private long totalRecords;
	
	public VisitsPaginationResponseVo(List<Visits> visitsList,long totalRecords) {
		this.visitsList = visitsList;
		this.totalRecords = totalRecords;
	}
	
	public VisitsPaginationResponseVo() {
		
	}
	
	public List<Visits> getVisitsList() {
		return visitsList;
	}

	public void setVisitsList(List<Visits> visitsList) {
		this.visitsList = visitsList;
	}

	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
}
