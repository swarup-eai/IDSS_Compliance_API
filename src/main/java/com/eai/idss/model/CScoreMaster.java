package com.eai.idss.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "cscoreMaster")
public class CScoreMaster {

	private long industryId;
	private double cscore;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate calculatedDate;
	
	
	public double getCscore() {
		return cscore;
	}
	public void setCscore(double cscore) {
		this.cscore = cscore;
	}
	public long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}
	public LocalDate getCalculatedDate() {
		return calculatedDate;
	}
	public void setCalculatedDate(LocalDate calculatedDate) {
		this.calculatedDate = calculatedDate;
	}
	
}
