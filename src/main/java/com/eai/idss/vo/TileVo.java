package com.eai.idss.vo;

import java.util.List;

public class TileVo {

	private String caseType;
	private int caseCount;
	private List<Integer> industries;
	
	public TileVo() {
		
	}
	
	public TileVo(String caseType,int caseCount) {
		this.caseType = caseType;
		this.caseCount = caseCount;
	}
	
	public List<Integer> getIndustries() {
		return industries;
	}

	public void setIndustries(List<Integer> industries) {
		this.industries = industries;
	}

	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public int getCaseCount() {
		return caseCount;
	}
	public void setCaseCount(int caseCount) {
		this.caseCount = caseCount;
	}
	
}
