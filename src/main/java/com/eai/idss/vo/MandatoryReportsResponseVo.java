package com.eai.idss.vo;

import java.util.List;

public class MandatoryReportsResponseVo {

	private String reportName;
	private List<SKU> data;
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public List<SKU> getData() {
		return data;
	}
	public void setData(List<SKU> data) {
		this.data = data;
	}
	
}
