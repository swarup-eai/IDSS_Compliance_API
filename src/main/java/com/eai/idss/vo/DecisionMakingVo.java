package com.eai.idss.vo;

import java.util.List;

public class DecisionMakingVo {
	private String action;
	private String suggestion;
	private List<DecisionMakingParamVo> dmpList;
	private String date1;
	private String date2;
	private String date3;
	private String date4;
	
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = date2;
	}
	public String getDate3() {
		return date3;
	}
	public void setDate3(String date3) {
		this.date3 = date3;
	}
	public String getDate4() {
		return date4;
	}
	public void setDate4(String date4) {
		this.date4 = date4;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public List<DecisionMakingParamVo> getDmpList() {
		return dmpList;
	}
	public void setDmpList(List<DecisionMakingParamVo> dmpList) {
		this.dmpList = dmpList;
	}
}
