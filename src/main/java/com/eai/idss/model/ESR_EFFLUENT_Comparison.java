package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "esr_EFFLUENT_comparison")
public class ESR_EFFLUENT_Comparison {

	private String effluentParticulars;
	private double effluentParticularsQuantityActual;
	private String effluentUom;
	
	public String getEffluentParticulars() {
		return effluentParticulars;
	}
	public void setEffluentParticulars(String effluentParticulars) {
		this.effluentParticulars = effluentParticulars;
	}
	public double getEffluentParticularsQuantityActual() {
		return effluentParticularsQuantityActual;
	}
	public void setEffluentParticularsQuantityActual(double effluentParticularsQuantityActual) {
		this.effluentParticularsQuantityActual = effluentParticularsQuantityActual;
	}
	public String getEffluentUom() {
		return effluentUom;
	}
	public void setEffluentUom(String effluentUom) {
		this.effluentUom = effluentUom;
	}

}
