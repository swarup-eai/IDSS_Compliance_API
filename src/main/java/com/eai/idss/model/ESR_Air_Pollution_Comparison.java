package com.eai.idss.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "esr_air_pollution")
public class ESR_Air_Pollution_Comparison {

	@Id
	private String _id;
	private Double industryid;
	private String companyname;
	private String airpollutantquantity;
	private String qtyuom;
	private int finantialyear;
	private String airPollutants;
	private double airPollutantConcentration;
	private String concentrationUom;
	
	public String getAirPollutants() {
		return airPollutants;
	}

	public void setAirPollutants(String airPollutants) {
		this.airPollutants = airPollutants;
	}

	public double getAirPollutantConcentration() {
		return airPollutantConcentration;
	}

	public void setAirPollutantConcentration(double airPollutantConcentration) {
		this.airPollutantConcentration = airPollutantConcentration;
	}

	public String getConcentrationUom() {
		return concentrationUom;
	}

	public void setConcentrationUom(String concentrationUom) {
		this.concentrationUom = concentrationUom;
	}

	public ESR_Air_Pollution_Comparison() {

	}

	public ESR_Air_Pollution_Comparison(String _id, Double industryid, String companyname, String airpollutantquantity,
			String qtyuom, int finantialyear) {
		super();
		this._id = _id;
		this.industryid = industryid;
		this.companyname = companyname;
		this.airpollutantquantity = airpollutantquantity;
		this.qtyuom = qtyuom;
		this.finantialyear = finantialyear;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Double getIndustryid() {
		return industryid;
	}

	public void setIndustryid(Double industryid) {
		this.industryid = industryid;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getAirpollutantquantity() {
		return airpollutantquantity;
	}

	public void setAirpollutantquantity(String airpollutantquantity) {
		this.airpollutantquantity = airpollutantquantity;
	}

	public String getQtyuom() {
		return qtyuom;
	}

	public void setQtyuom(String qtyuom) {
		this.qtyuom = qtyuom;
	}

	public int getFinantialyear() {
		return finantialyear;
	}

	public void setFinantialyear(int finantialyear) {
		this.finantialyear = finantialyear;
	}

}
