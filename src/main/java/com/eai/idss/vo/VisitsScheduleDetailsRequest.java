package com.eai.idss.vo;

public class VisitsScheduleDetailsRequest {

	String month; //MM-YYYY
	String when; // Historical (completed, pending), Upcoming (scheduled)
	String status; //pending, all, completed, scheduled
	String compliance; //0-25, 26-50, 51-75, 76-100
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
