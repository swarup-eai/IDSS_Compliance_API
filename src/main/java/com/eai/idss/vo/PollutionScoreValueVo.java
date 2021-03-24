package com.eai.idss.vo;

public class PollutionScoreValueVo {

	private String year;
	private double value;
	private int thresholdValue; 
	
	public  PollutionScoreValueVo() {
		
	}
	public  PollutionScoreValueVo(String y,double v) {
		this.year=y;
		this.value=v;
	}
	public  PollutionScoreValueVo(String y,double v,int thresholdValue) {
		this.year=y;
		this.value=v;
		this.thresholdValue=thresholdValue;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	public int getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(int thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	
}
