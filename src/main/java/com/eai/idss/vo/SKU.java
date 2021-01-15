package com.eai.idss.vo;

public class SKU {

	private String pollutant;
	private String quantity;
	private String unit;
	
	public SKU() {
		
	}
	
	public SKU(String pollutant,String quantity,String unit) {
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
