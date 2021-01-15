package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "e_waste_authorization")
public class EWasteAnnualAuthorization {

	private double eWasteQtyGenerated;
	private double eWasteQtyRefurbishing;
	private double eWasteQtyRecycling;
	private double eWasteQtyDisposal;
	public double geteWasteQtyGenerated() {
		return eWasteQtyGenerated;
	}
	public void seteWasteQtyGenerated(double eWasteQtyGenerated) {
		this.eWasteQtyGenerated = eWasteQtyGenerated;
	}
	public double geteWasteQtyRefurbishing() {
		return eWasteQtyRefurbishing;
	}
	public void seteWasteQtyRefurbishing(double eWasteQtyRefurbishing) {
		this.eWasteQtyRefurbishing = eWasteQtyRefurbishing;
	}
	public double geteWasteQtyRecycling() {
		return eWasteQtyRecycling;
	}
	public void seteWasteQtyRecycling(double eWasteQtyRecycling) {
		this.eWasteQtyRecycling = eWasteQtyRecycling;
	}
	public double geteWasteQtyDisposal() {
		return eWasteQtyDisposal;
	}
	public void seteWasteQtyDisposal(double eWasteQtyDisposal) {
		this.eWasteQtyDisposal = eWasteQtyDisposal;
	}
	
}
