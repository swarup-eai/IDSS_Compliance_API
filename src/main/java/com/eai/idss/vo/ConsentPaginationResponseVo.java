package com.eai.idss.vo;

import java.util.List;

import com.eai.idss.model.Consent;

public class ConsentPaginationResponseVo {

	private List<Consent> consentList;
	private long totalRecords;
	
	public ConsentPaginationResponseVo() {
		
	}
	
	public ConsentPaginationResponseVo(List<Consent> consentList,long totalRecords) {
		this.consentList = consentList;
		this.totalRecords = totalRecords;
	}
	
	public List<Consent> getConsentList() {
		return consentList;
	}
	public void setConsentList(List<Consent> consentList) {
		this.consentList = consentList;
	}
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
}
