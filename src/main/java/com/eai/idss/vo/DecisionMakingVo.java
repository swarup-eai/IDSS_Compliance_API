package com.eai.idss.vo;

import java.util.List;

public class DecisionMakingVo {
	private String action;
	private String suggestion;
	private List<DecisionMakingParamVo> dmpList;
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
