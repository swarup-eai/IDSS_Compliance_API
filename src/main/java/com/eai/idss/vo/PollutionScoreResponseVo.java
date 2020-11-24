package com.eai.idss.vo;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PollutionScoreResponseVo {

	@Id
	private String _id;
	private String industryName;
	private String companyname;
	private Double industryId;
	private Double Industry_id;
	private Double concentration;
	private String concentrationUom;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String created;
	private String parameter;
	private String airpollutantquantity;
	private String qtyuom;
	private int finantialyear;

	private Double treatedEffluentBod;
	private Double treatedEffluentCod;
	private Double treatedEffluentSs;
	private Double treatedEffluentTds;
	private Double treatedEffluentPh;
	private Double uom;
	private String name;

	private String waterPollutants;
	private Double waterPollutantQuantity;
	private String uomName;
	private String UOM_name;

	private String fuelName;
	private int fuelUom;
	
	private String company_name;
	private String rawMaterialName;
	//private int uom;
	
	private String stackNumber;
	private String stackAttachedTo;
	private String capacityUom;
	private String stackFuelType;
	private Double stackFuelQuantity;
	private String fuelQtyUom;

	public PollutionScoreResponseVo() {

	}

	public PollutionScoreResponseVo(String _id, Double industryId, String industryName, Double concentration,
			String concentrationUom, String created, String parameter) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.industryName = industryName;
		this.concentration = concentration;
		this.concentrationUom = concentrationUom;
		this.created = created;
		this.parameter = parameter;
	}

	public PollutionScoreResponseVo(String _id, Double industryid, String companyname, String airpollutantquantity,
			String qtyuom, int finantialyear) {
		super();
		this._id = _id;
		this.industryId = industryid;
		this.companyname = companyname;
		this.airpollutantquantity = airpollutantquantity;
		this.qtyuom = qtyuom;
		this.finantialyear = finantialyear;
	}

	public PollutionScoreResponseVo(String _id, String industryName, Double treatedEffluentBod,
			Double treatedEffluentCod, Double treatedEffluentSs, Double treatedEffluentTds, Double treatedEffluentPh,
			Double uom, String name) {
		super();
		this._id = _id;
		this.industryName = industryName;
		this.treatedEffluentBod = treatedEffluentBod;
		this.treatedEffluentCod = treatedEffluentCod;
		this.treatedEffluentSs = treatedEffluentSs;
		this.treatedEffluentTds = treatedEffluentTds;
		this.treatedEffluentPh = treatedEffluentPh;
		this.uom = uom;
		this.name = name;
	}

	public PollutionScoreResponseVo(String _id, String industryName, int finantialyear, String waterPollutants,
			Double waterPollutantQuantity, String uomName) {
		super();
		this._id = _id;
		this.industryName = industryName;
		this.finantialyear = finantialyear;
		this.waterPollutants = waterPollutants;
		this.waterPollutantQuantity = waterPollutantQuantity;
		this.uomName = uomName;
	}

	public PollutionScoreResponseVo(String _id, Double industryId, String industryName, Double uom, String name,
			String fuelName) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.industryName = industryName;
		this.uom = uom;
		this.name = name;
		this.fuelName = fuelName;
	}

	public PollutionScoreResponseVo(String _id, Double industryId, String fuelName, int fuelUom, String name,
			int finantialyear) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.fuelName = fuelName;
		this.fuelUom = fuelUom;
		this.name = name;
		this.finantialyear = finantialyear;
	}

	/*
	 * public PollutionScoreResponseVo(String _id, Double industry_id, String
	 * industryName, String name, Double uOM, String uOM_name) { super(); this._id =
	 * _id; this.Industry_id = industry_id; this.industryName = industryName;
	 * this.name = name; this.UOM = uOM; this.UOM_name = uOM_name; }
	 */

	public PollutionScoreResponseVo(String _id, String industryName, Double industry_id, Double uom, String name,
			String uOM_name) {
		super();
		this._id = _id;
		this.industryName = industryName;
		this.Industry_id = industry_id;
		this.uom = uom;
		this.name = name;
		this.UOM_name = uOM_name;
	}

	public PollutionScoreResponseVo(String _id, Double industry_id, String company_name, String created, String name, Double uom,
			String uOM_name) {
		super();
		this._id = _id;
		this.Industry_id = industry_id;
		this.company_name = company_name;
		this.created = created;
		this.name = name;
		this.uom = uom;
		this.UOM_name = uOM_name;
	}
	
	public PollutionScoreResponseVo(String _id, Double industryId, String industryName, String rawMaterialName,
			Double uom, String name) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.industryName = industryName;
		this.rawMaterialName = rawMaterialName;
		this.uom = uom;
		this.name = name;
	}

	public PollutionScoreResponseVo(String _id,String companyname, Double industryId, 
			String rawMaterialName, String name, Double uom,int finantialyear) {
		super();
		this._id = _id;
		this.industryId = industryId;
		this.companyname = companyname;
		this.uom = uom;
		this.rawMaterialName = rawMaterialName;
		this.name = name;
		this.finantialyear=finantialyear;
	}
	
	public PollutionScoreResponseVo(String _id, Double industryId,String industryName, String stackNumber, String stackAttachedTo,
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

	public String getFuelName() {
		return fuelName;
	}

	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public Double getIndustry_id() {
		return Industry_id;
	}

	public void setIndustry_id(Double industry_id) {
		Industry_id = industry_id;
	}

	public String getUOM_name() {
		return UOM_name;
	}

	public void setUOM_name(String uOM_name) {
		UOM_name = uOM_name;
	}

	public int getFuelUom() {
		return fuelUom;
	}

	public void setFuelUom(int fuelUom) {
		this.fuelUom = fuelUom;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getRawMaterialName() {
		return rawMaterialName;
	}

	public void setRawMaterialName(String rawMaterialName) {
		this.rawMaterialName = rawMaterialName;
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

	/*public int getUOM() {
		return UOM;
	}

	public void setUOM(int uOM) {
		UOM = uOM;
	}*/
	
	

}
