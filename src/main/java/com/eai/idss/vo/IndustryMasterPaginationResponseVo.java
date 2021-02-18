package com.eai.idss.vo;

import java.util.List;

import com.eai.idss.model.IndustryMaster;

public class IndustryMasterPaginationResponseVo {

	private List<IndustryMaster> imList;
	private int totalRecords;
	
	public IndustryMasterPaginationResponseVo() {
		
	}
	
	public IndustryMasterPaginationResponseVo(List<IndustryMaster> imList,int totalRecords) {
		this.imList = imList;
		this.totalRecords = totalRecords;
	}
	
	public List<IndustryMaster> getImList() {
		return imList;
	}
	public void setImList(List<IndustryMaster> imList) {
		this.imList = imList;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	
}
