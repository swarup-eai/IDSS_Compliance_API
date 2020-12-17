package com.eai.idss.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "cscoreMaster")
public class CScoreMaster {

	private long industryid;
	private double cscore;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate calculateddate;
	
	public long getIndustryid() {
		return industryid;
	}
	public void setIndustryid(long industryid) {
		this.industryid = industryid;
	}
	public double getCscore() {
		return cscore;
	}
	public void setCscore(double cscore) {
		this.cscore = cscore;
	}
	public LocalDate getCalculateddate() {
		return calculateddate;
	}
	public void setCalculateddate(LocalDate calculateddate) {
		this.calculateddate = calculateddate;
	}
	
}
