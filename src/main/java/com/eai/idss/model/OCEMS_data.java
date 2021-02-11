package com.eai.idss.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;


@Document(collection = "OCEMS_data")
public class OCEMS_data {
	
	@Id
	private String site_id;
	private String  industry_name;
	private String industry;
	private String station_name;
	private String parameter_name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date time_stamp;
	private Double value;
	private String units;
	private double upper_limit;
	
	
	public double getUpper_limit() {
		return upper_limit;
	}

	public void setUpper_limit(double upper_limit) {
		this.upper_limit = upper_limit;
	}

	public OCEMS_data() {

	}

	public OCEMS_data(String site_id, String industry_name, String industry, String station_name, String parameter_name,
			Date time_stamp, Double value, String units) {
		super();
		this.site_id = site_id;
		this.industry_name = industry_name;
		this.industry = industry;
		this.station_name = station_name;
		this.parameter_name = parameter_name;
		this.time_stamp = time_stamp;
		this.value = value;
		this.units = units;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getIndustry_name() {
		return industry_name;
	}

	public void setIndustry_name(String industry_name) {
		this.industry_name = industry_name;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getParameter_name() {
		return parameter_name;
	}

	public void setParameter_name(String parameter_name) {
		this.parameter_name = parameter_name;
	}

	public Date getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(Date time_stamp) {
		this.time_stamp = time_stamp;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	
}
