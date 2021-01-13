package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "consent_EFFLUENT_comparison")
public class Consent_EFFLUENT_Comparison {

	private String name;
	private double capacityOfEtp;
	private double capacityOfStp;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCapacityOfEtp() {
		return capacityOfEtp;
	}
	public void setCapacityOfEtp(double capacityOfEtp) {
		this.capacityOfEtp = capacityOfEtp;
	}
	public double getCapacityOfStp() {
		return capacityOfStp;
	}
	public void setCapacityOfStp(double capacityOfStp) {
		this.capacityOfStp = capacityOfStp;
	}
	
}
