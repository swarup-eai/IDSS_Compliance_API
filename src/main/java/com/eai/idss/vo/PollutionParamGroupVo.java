package com.eai.idss.vo;

import java.util.List;

public class PollutionParamGroupVo {

	private String param;
	private List<SKU> consentSKU;
	private List<SKU> esrSKU;
	private List<SKU> form4SKU;
	
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public List<SKU> getConsentSKU() {
		return consentSKU;
	}
	public void setConsentSKU(List<SKU> consentSKU) {
		this.consentSKU = consentSKU;
	}
	public List<SKU> getEsrSKU() {
		return esrSKU;
	}
	public void setEsrSKU(List<SKU> esrSKU) {
		this.esrSKU = esrSKU;
	}
	public List<SKU> getForm4SKU() {
		return form4SKU;
	}
	public void setForm4SKU(List<SKU> form4sku) {
		form4SKU = form4sku;
	}
}
