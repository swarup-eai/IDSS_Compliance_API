package com.eai.idss.vo;

public class SKU {

	private String pollutant;
	private double quantity;
	private String unit;
	
	public SKU() {
		
	}
	
	public SKU(String pollutant,double quantity,String unit) {
		this.pollutant = pollutant;
		this.quantity = quantity;
		this.unit = unit;
	}
	
	public String getPollutant() {
		return pollutant;
	}
	public void setPollutant(String pollutant) {
		this.pollutant = pollutant;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
