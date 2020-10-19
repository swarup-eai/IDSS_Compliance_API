package com.eai.idss.vo;

public class TileVo {

	private String caseType;
	private int caseCount;
	
	public TileVo() {
		
	}
	
	public TileVo(String caseType,int caseCount) {
		this.caseType = caseType;
		this.caseCount = caseCount;
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
