package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "consent_STACK_comparison")
public class Consent_STACK_comparison {

	@Id
	private String _id;
	private Double industryId;
	private String industryName;
	private String stackNumber;
	private String stackAttachedTo;
	private String capacityUom;
	private String stackFuelType;
	private Double stackFuelQuantity;
	private String fuelQtyUom;
	
	
	public Consent_STACK_comparison() {

	}


	public Consent_STACK_comparison(String _id, Double industryId,String industryName, String stackNumber, String stackAttachedTo,
			String capacityUom, String stackFuelType, Double stackFuelQuantity, String fuelQtyUom) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.industryName=industryName;
		this.stackNumber = stackNumber;
		this.stackAttachedTo = stackAttachedTo;
		this.capacityUom = capacityUom;
		this.stackFuelType = stackFuelType;
		this.stackFuelQuantity = stackFuelQuantity;
		this.fuelQtyUom = fuelQtyUom;
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


	public String getStackNumber() {
		return stackNumber;
	}


	public void setStackNumber(String stackNumber) {
		this.stackNumber = stackNumber;
	}


	public String getStackAttachedTo() {
		return stackAttachedTo;
	}


	public void setStackAttachedTo(String stackAttachedTo) {
		this.stackAttachedTo = stackAttachedTo;
	}


	public String getCapacityUom() {
		return capacityUom;
	}


	public void setCapacityUom(String capacityUom) {
		this.capacityUom = capacityUom;
	}


	public String getStackFuelType() {
		return stackFuelType;
	}


	public void setStackFuelType(String stackFuelType) {
		this.stackFuelType = stackFuelType;
	}


	public Double getStackFuelQuantity() {
		return stackFuelQuantity;
	}


	public void setStackFuelQuantity(Double stackFuelQuantity) {
		this.stackFuelQuantity = stackFuelQuantity;
	}


	public String getFuelQtyUom() {
		return fuelQtyUom;
	}


	public void setFuelQtyUom(String fuelQtyUom) {
		this.fuelQtyUom = fuelQtyUom;
	}
	
	
}
