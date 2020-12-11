package com.eai.idss.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Consented_Air_Pollution_Comparison")
public class Consented_Air_Pollution_Comparison {

	@Id
	private String _id;
	private Double industryId;
	private String Industryname;
	private Double concentration;
	private String concentrationUom;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String created;
	private String parameter;

	public Consented_Air_Pollution_Comparison() {

	}

	public Consented_Air_Pollution_Comparison(String _id, Double industryId,String Industryname, Double concentration,
			String concentrationUom, String created, String parameter) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.Industryname=Industryname;
		this.concentration = concentration;
		this.concentrationUom = concentrationUom;
		this.created = created;
		this.parameter = parameter;
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

	public String getIndustryname() {
		return Industryname;
	}

	public void setIndustryname(String industryname) {
		Industryname = industryname;
	}

	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}

	public String getConcentrationUom() {
		return concentrationUom;
	}

	public void setConcentrationUom(String concentrationUom) {
		this.concentrationUom = concentrationUom;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}
