package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "consent_WATER_comparison")
public class Consent_WATER_comparison {
	
	
	@Id
	private String _id;
	private Double industryId;
	private String industryName;
	private Double uom;
	private String name;
	private Double treatedEffluentBod;
	private Double treatedEffluentCod;
	private Double treatedEffluentSs;
	private Double treatedEffluentTds;
	private Double treatedEffluentPh;
	
	public Consent_WATER_comparison() {

	}

	public Consent_WATER_comparison(String _id, Double industryId, String industryName, Double uom, String name,
			Double treatedEffluentBod, Double treatedEffluentCod, Double treatedEffluentSs, Double treatedEffluentTds,
			Double treatedEffluentPh) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.industryName = industryName;
		this.uom = uom;
		this.name = name;
		this.treatedEffluentBod = treatedEffluentBod;
		this.treatedEffluentCod = treatedEffluentCod;
		this.treatedEffluentSs = treatedEffluentSs;
		this.treatedEffluentTds = treatedEffluentTds;
		this.treatedEffluentPh = treatedEffluentPh;
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

	public Double getTreatedEffluentBod() {
		return treatedEffluentBod;
	}

	public void setTreatedEffluentBod(Double treatedEffluentBod) {
		this.treatedEffluentBod = treatedEffluentBod;
	}

	public Double getTreatedEffluentCod() {
		return treatedEffluentCod;
	}

	public void setTreatedEffluentCod(Double treatedEffluentCod) {
		this.treatedEffluentCod = treatedEffluentCod;
	}

	public Double getTreatedEffluentSs() {
		return treatedEffluentSs;
	}

	public void setTreatedEffluentSs(Double treatedEffluentSs) {
		this.treatedEffluentSs = treatedEffluentSs;
	}

	public Double getTreatedEffluentTds() {
		return treatedEffluentTds;
	}

	public void setTreatedEffluentTds(Double treatedEffluentTds) {
		this.treatedEffluentTds = treatedEffluentTds;
	}

	public Double getTreatedEffluentPh() {
		return treatedEffluentPh;
	}

	public void setTreatedEffluentPh(Double treatedEffluentPh) {
		this.treatedEffluentPh = treatedEffluentPh;
	}

	
	
}
