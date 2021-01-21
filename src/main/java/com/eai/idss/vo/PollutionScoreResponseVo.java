package com.eai.idss.vo;

import java.util.List;

public class PollutionScoreResponseVo {
	private String form;
	private String param;
	private List<PollutionScoreValueVo> psv;
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public List<PollutionScoreValueVo> getPsv() {
		return psv;
	}
	public void setPsv(List<PollutionScoreValueVo> psv) {
		this.psv = psv;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
}
