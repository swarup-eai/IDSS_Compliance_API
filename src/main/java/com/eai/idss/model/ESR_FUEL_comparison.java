package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "esr_FUEL_comparison")
public class ESR_FUEL_comparison {
	
	@Id
	private String _id;
	private Double industryId;
	private String fuelName;
	private int fuelUom;
	private String name;
	private int finantialYear;
	private double fuelQuantityActual;
	private double hwQuantityNow;
	private String newUom;
	
	public double getHwQuantityNow() {
		return hwQuantityNow;
	}

	public void setHwQuantityNow(double hwQuantityNow) {
		this.hwQuantityNow = hwQuantityNow;
	}

	public String getNewUom() {
		return newUom;
	}

	public void setNewUom(String newUom) {
		this.newUom = newUom;
	}

	public ESR_FUEL_comparison() {

	}

	public ESR_FUEL_comparison(String _id, Double industryId, String fuelName, int fuelUom, String name,
			int finantialYear) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.fuelName = fuelName;
		this.fuelUom = fuelUom;
		this.name = name;
		this.finantialYear = finantialYear;
	}
	
	public double getFuelQuantityActual() {
		return fuelQuantityActual;
	}

	public void setFuelQuantityActual(double fuelQuantityActual) {
		this.fuelQuantityActual = fuelQuantityActual;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFuelName() {
		return fuelName;
	}

	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}

	public int getFuelUom() {
		return fuelUom;
	}

	public void setFuelUom(int fuelUom) {
		this.fuelUom = fuelUom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public Double getIndustryId() {
		return industryId;
	}



	public void setIndustryId(Double industryId) {
		this.industryId = industryId;
	}



	public int getFinantialYear() {
		return finantialYear;
	}



	public void setFinantialYear(int finantialYear) {
		this.finantialYear = finantialYear;
	}

	

	
}
