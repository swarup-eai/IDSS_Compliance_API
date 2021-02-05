package com.eai.idss.vo;

public class VisitsScheduleDetailsRequest {

	String when; // Historical (completed, pending), Upcoming (scheduled)
	String status; //pending, all, completed, scheduled
	String compliance; //0-25, 26-50, 51-75, 76-100
	private String fromDate;
	private String toDate;
	
	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompliance() {
		return compliance;
	}
	public void setCompliance(String compliance) {
		this.compliance = compliance;
	}

}
