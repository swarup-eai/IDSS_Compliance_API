package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "visitProcessEfficiency")
public class VisitProcessEfficiency {

	private String region;
	private int defined_report_filed;
	private int defined_samples_submitted;
	private int defined_samples_analysed;
	private int defined_review_comp;
	private int defined_legal_action;
	private double avg_report_filed;
	private int avg_samples_submitted;
	private int avg_samples_analysed;
	private int avg_review_comp;
	private double avg_legal_action;
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public int getDefined_report_filed() {
		return defined_report_filed;
	}
	public void setDefined_report_filed(int defined_report_filed) {
		this.defined_report_filed = defined_report_filed;
	}
	public int getDefined_samples_submitted() {
		return defined_samples_submitted;
	}
	public void setDefined_samples_submitted(int defined_samples_submitted) {
		this.defined_samples_submitted = defined_samples_submitted;
	}
	public int getDefined_samples_analysed() {
		return defined_samples_analysed;
	}
	public void setDefined_samples_analysed(int defined_samples_analysed) {
		this.defined_samples_analysed = defined_samples_analysed;
	}
	public int getDefined_review_comp() {
		return defined_review_comp;
	}
	public void setDefined_review_comp(int defined_review_comp) {
		this.defined_review_comp = defined_review_comp;
	}
	public int getDefined_legal_action() {
		return defined_legal_action;
	}
	public void setDefined_legal_action(int defined_legal_action) {
		this.defined_legal_action = defined_legal_action;
	}
	public double getAvg_report_filed() {
		return avg_report_filed;
	}
	public void setAvg_report_filed(double avg_report_filed) {
		this.avg_report_filed = avg_report_filed;
	}
	public int getAvg_samples_submitted() {
		return avg_samples_submitted;
	}
	public void setAvg_samples_submitted(int avg_samples_submitted) {
		this.avg_samples_submitted = avg_samples_submitted;
	}
	public int getAvg_samples_analysed() {
		return avg_samples_analysed;
	}
	public void setAvg_samples_analysed(int avg_samples_analysed) {
		this.avg_samples_analysed = avg_samples_analysed;
	}
	public int getAvg_review_comp() {
		return avg_review_comp;
	}
	public void setAvg_review_comp(int avg_review_comp) {
		this.avg_review_comp = avg_review_comp;
	}
	public double getAvg_legal_action() {
		return avg_legal_action;
	}
	public void setAvg_legal_action(double avg_legal_action) {
		this.avg_legal_action = avg_legal_action;
	}
	
}
