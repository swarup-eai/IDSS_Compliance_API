package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "esr_WATER_comparison")
public class ESR_WATER_comparison {
	@Id
	private String _id;
	private Double industryId;
	private String companyName;
	private String waterPollutants;
	private Double waterPollutantQuantity;
	private String uomName;
	private int finantialyear;

	
	public ESR_WATER_comparison() {

	}


	public ESR_WATER_comparison(String _id, Double industryid, String companyname, String waterPollutants,
			Double waterPollutantQuantity, String uomName, int finantialyear) {
		super();
		this._id = _id;
		this.industryId = industryid;
		this.companyName = companyname;
		this.waterPollutants = waterPollutants;
		this.waterPollutantQuantity = waterPollutantQuantity;
		this.uomName = uomName;
		this.finantialyear = finantialyear;
	}


	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public Double getIndustryid() {
		return industryId;
	}


	public void setIndustryid(Double industryid) {
		this.industryId = industryid;
	}


	public String getCompanyname() {
		return companyName;
	}


	public void setCompanyname(String companyname) {
		this.companyName = companyname;
	}


	public String getWaterPollutants() {
		return waterPollutants;
	}


	public void setWaterPollutants(String waterPollutants) {
		this.waterPollutants = waterPollutants;
	}


	public Double getWaterPollutantQuantity() {
		return waterPollutantQuantity;
	}


	public void setWaterPollutantQuantity(Double waterPollutantQuantity) {
		this.waterPollutantQuantity = waterPollutantQuantity;
	}


	public String getUomName() {
		return uomName;
	}


	public void setUomName(String uomName) {
		this.uomName = uomName;
	}


	public int getFinantialyear() {
		return finantialyear;
	}


	public void setFinantialyear(int finantialyear) {
		this.finantialyear = finantialyear;
	}
	
	
}
