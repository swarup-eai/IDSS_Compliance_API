package com.eai.idss.vo;

import java.util.List;

public class ComparisonTableSKUVo {

	private String param;
	private List<ComparisonTablePollutantVo> data;
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public List<ComparisonTablePollutantVo> getData() {
		return data;
	}
	public void setData(List<ComparisonTablePollutantVo> data) {
		this.data = data;
	}
	
}
