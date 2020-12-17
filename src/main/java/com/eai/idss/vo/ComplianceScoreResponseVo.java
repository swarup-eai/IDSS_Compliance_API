package com.eai.idss.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ComplianceScoreResponseVo {

	private long industryId;
	private int cScore;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate calculateddate;
	private String monthYear;
	private long visitId;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate visitDate;
	private int legalActionsCnt;
	
	public LocalDate getCalculateddate() {
		return calculateddate;
	}
	public void setCalculateddate(LocalDate calculateddate) {
		this.calculateddate = calculateddate;
	}
	public String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	public long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}
	public long getVisitId() {
		return visitId;
	}
	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}
	public int getcScore() {
		return cScore;
	}
	public void setcScore(int cScore) {
		this.cScore = cScore;
	}
	public LocalDate getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(LocalDate visitDate) {
		this.visitDate = visitDate;
	}
	public int getLegalActionsCnt() {
		return legalActionsCnt;
	}
	public void setLegalActionsCnt(int legalActionsCnt) {
		this.legalActionsCnt = legalActionsCnt;
	}
	
}
