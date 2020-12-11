package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Esr_HW_Comparison")
public class Esr_HW_Comparison {

	@Id
	private String _id;
	private Double Industry_id;
	private String company_name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String created;
	private String name;
	private Double uom;
	private String UOM_name;

	public Esr_HW_Comparison() {

	}

	public Esr_HW_Comparison(String _id, Double industry_id, String company_name, String created, String name,
			Double uOM, String uOM_name) {
		super();
		this._id = _id;
		Industry_id = industry_id;
		this.company_name = company_name;
		this.created = created;
		this.name = name;
		this.uom = uOM;
		this.UOM_name = uOM_name;
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

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getUom() {
		return uom;
	}

	public void setUom(Double uom) {
		this.uom = uom;
	}

	public String getUOM_name() {
		return UOM_name;
	}

	public void setUOM_name(String uOM_name) {
		UOM_name = uOM_name;
	}

}
