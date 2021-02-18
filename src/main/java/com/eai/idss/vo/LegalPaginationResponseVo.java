package com.eai.idss.vo;

import java.util.List;

import com.eai.idss.model.Legal;

public class LegalPaginationResponseVo {

	private List<Legal> legalList;
	private long totalRecords;
	
	public LegalPaginationResponseVo() {
		
	}
	
	public LegalPaginationResponseVo(List<Legal> legalList,long totalRecords) {
		this.legalList = legalList;
		this.totalRecords = totalRecords;
	}
	
	public List<Legal> getLegalList() {
		return legalList;
	}
	public void setLegalList(List<Legal> legalList) {
		this.legalList = legalList;
	}
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	
}
