package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Consent_HW_Comparison")
public class Consent_HW_Comparison {
	
	@Id
	private String _id;
	private Double Industry_id;
	private String industryName;
	private String name;
	private Double UOM;
	private String UOM_name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String consentApprovalDate;
	private double quantity;
	
	public double getQuantity() {
		return quantity;
	}


	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}


	public Consent_HW_Comparison() {
		
	}


	public Consent_HW_Comparison(String _id, Double industry_id, String industryName, String name, Double uOM,
			String uOM_name) {
		super();
		this._id = _id;
		this.Industry_id = industry_id;
		this.industryName = industryName;
		this.name = name;
		this.UOM = uOM;
		this.UOM_name = uOM_name;
	}

	public String getConsentApprovalDate() {
		return consentApprovalDate;
	}


	public void setConsentApprovalDate(String consentApprovalDate) {
		this.consentApprovalDate = consentApprovalDate;
	}


	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public Double getIndustry_id() {
		return Industry_id;
	}


	public void setIndustry_id(Double industry_id) {
		Industry_id = industry_id;
	}


	public String getIndustryName() {
		return industryName;
	}


	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Double getUOM() {
		return UOM;
	}


	public void setUOM(Double uOM) {
		UOM = uOM;
	}


	public String getUOM_name() {
		return UOM_name;
	}


	public void setUOM_name(String uOM_name) {
		UOM_name = uOM_name;
	}
	
	
	
	

}
