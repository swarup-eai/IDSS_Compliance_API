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
	
}
