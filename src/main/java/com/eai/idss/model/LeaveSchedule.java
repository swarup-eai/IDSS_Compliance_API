package com.eai.idss.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "leaveSchedule")
public class LeaveSchedule {

	private String userName;
	private String monthYear;
	private List<String> leaveDates;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	public List<String> getLeaveDates() {
		return leaveDates;
	}
	public void setLeaveDates(List<String> leaveDates) {
		this.leaveDates = leaveDates;
	}
	
}
