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

	private long scoreByVisitsReport;
	private long scoreByEsrFiled;
	private long scoreByOtherSubmissions;
	private long scoreByConsentExpiry;
	private long scoreByOcemsViolations;
	private long scoreByEsrViolations;
	private long scoreByLegalWarnings;
	private long scoreByLegalActions;
	private long scoreByRepeatOffenders;
	
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

	public long getScoreByVisitsReport() {
		return scoreByVisitsReport;
	}
	public void setScoreByVisitsReport(long scoreByVisitsReport) {
		this.scoreByVisitsReport = scoreByVisitsReport;
	}

	public long getScoreByEsrFiled() {
		return scoreByEsrFiled;
	}
	public void setScoreByEsrFiled(long scoreByEsrFiled) {
		this.scoreByEsrFiled = scoreByEsrFiled;
	}

	public long getScoreByOtherSubmissions() {
		return scoreByOtherSubmissions;
	}
	public void setScoreByOtherSubmissions(long scoreByOtherSubmissions) {
		this.scoreByOtherSubmissions = scoreByOtherSubmissions;
	}

	public long getScoreByConsentExpiry() {
		return scoreByConsentExpiry;
	}
	public void setScoreByConsentExpiry(long scoreByConsentExpiry) {
		this.scoreByConsentExpiry = scoreByConsentExpiry;
	}

	public long getScoreByOcemsViolations() {
		return scoreByOcemsViolations;
	}
	public void setScoreByOcemsViolations(long scoreByOcemsViolations) {
		this.scoreByOcemsViolations = scoreByOcemsViolations;
	}

	public long getScoreByEsrViolations() {
		return scoreByEsrViolations;
	}
	public void setScoreByEsrViolations(long scoreByEsrViolations) {
		this.scoreByEsrViolations = scoreByEsrViolations;
	}

	public long getScoreByLegalWarnings() {
		return scoreByLegalWarnings;
	}
	public void setScoreByLegalWarnings(long scoreByLegalWarnings) {
		this.scoreByLegalWarnings = scoreByLegalWarnings;
	}

	public long getScoreByLegalActions() {
		return scoreByLegalActions;
	}
	public void setScoreByLegalActions(long scoreByLegalActions) {
		this.scoreByLegalActions = scoreByLegalActions;
	}

	public long getScoreByRepeatOffenders() {
		return scoreByRepeatOffenders;
	}
	public void setScoreByRepeatOffenders(long scoreByRepeatOffenders) {
		this.scoreByRepeatOffenders = scoreByRepeatOffenders;
	}
}
