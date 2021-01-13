package com.eai.idss.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "consent_FUEL_comparison")
public class Consent_FUEL_comparison {

	@Id
	private String _id;
	private Double industryId;
	private String industryName;
	private String fuelName;
	private Double uom;
	private String name;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate applicationCreatedOn;
	private double fuelConsumptions;
	
	public double getFuelConsumptions() {
		return fuelConsumptions;
	}

	public void setFuelConsumptions(double fuelConsumptions) {
		this.fuelConsumptions = fuelConsumptions;
	}

	public void setApplicationCreatedOn(LocalDate applicationCreatedOn) {
		this.applicationCreatedOn = applicationCreatedOn;
	}

	public LocalDate getApplicationCreatedOn() {
		return applicationCreatedOn;
	}

	public Consent_FUEL_comparison() {

	}

	public Consent_FUEL_comparison(String _id,Double industryId, String industryName, String fuelName, Double uom, String name) {
		super();
		this._id = _id;
		this.industryId=industryId;
		this.industryName = industryName;
		this.fuelName = fuelName;
		this.uom = uom;
		this.name = name;
	}
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Double getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Double industryId) {
		this.industryId = industryId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getFuelName() {
		return fuelName;
	}

	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}

	public Double getUom() {
		return uom;
	}

	public void setUom(Double uom) {
		this.uom = uom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
