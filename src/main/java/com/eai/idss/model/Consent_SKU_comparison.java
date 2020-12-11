package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "consent_SKU_comparison")
public class Consent_SKU_comparison {

	@Id
	private String _id;
	private Double industryId;
	private String industryName;
	private String type;
	private String region;
	private String subRegion;
	private String productname;
	private Double uom;
	private String name;

	public Consent_SKU_comparison() {

	}

	public Consent_SKU_comparison(String _id, Double industryId, String industryName, String type, String region,
			String subRegion, String productname, Double uom, String name) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.industryName = industryName;
		this.type = type;
		this.region = region;
		this.subRegion = subRegion;
		this.productname = productname;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
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
