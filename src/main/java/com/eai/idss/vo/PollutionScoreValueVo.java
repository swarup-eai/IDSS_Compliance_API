package com.eai.idss.vo;

public class PollutionScoreValueVo {

	private String year;
	private double value;
	
	public  PollutionScoreValueVo() {
		
	}
	
	public  PollutionScoreValueVo(String y,double v) {
		this.year=y;
		this.value=v;
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
	
}
