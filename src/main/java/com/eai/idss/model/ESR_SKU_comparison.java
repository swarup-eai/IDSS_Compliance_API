package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "esr_SKU_comparison")
public class ESR_SKU_comparison {
	
	@Id
	private String _id;
	private Double industryId;
	private String companyName;
	private String productname;
	private Double uom;
	private String name;
	private double productQty;
	private int finantialYear;
	
	public ESR_SKU_comparison() {

	}

	public ESR_SKU_comparison(String _id, Double industryId, String companyName, String productname, Double uom,
			String name, int finantialYear) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.companyName = companyName;
		this.productname = productname;
		this.uom = uom;
		this.name = name;
		this.finantialYear = finantialYear;
	}
	
	public double getProductQty() {
		return productQty;
	}

	public void setProductQty(double productQty) {
		this.productQty = productQty;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public int getFinantialYear() {
		return finantialYear;
	}

	public void setFinantialYear(int finantialYear) {
		this.finantialYear = finantialYear;
	}
	
	
}
