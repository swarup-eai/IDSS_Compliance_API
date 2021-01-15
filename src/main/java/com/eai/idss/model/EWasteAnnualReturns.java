package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "e_waste_annual_returns")
public class EWasteAnnualReturns {

	private double eWasteQtyConsumer;
	private double eWasteQtyDismantlersProcessed;
	private double eWasteQtyDismantlersRecoveredSold;
	private double eWasteQtyRecyclersSentTsdf;
	private double eWasteQtyDismantlersSentRecyclers;
	public double geteWasteQtyConsumer() {
		return eWasteQtyConsumer;
	}
	public void seteWasteQtyConsumer(double eWasteQtyConsumer) {
		this.eWasteQtyConsumer = eWasteQtyConsumer;
	}
	public double geteWasteQtyDismantlersProcessed() {
		return eWasteQtyDismantlersProcessed;
	}
	public void seteWasteQtyDismantlersProcessed(double eWasteQtyDismantlersProcessed) {
		this.eWasteQtyDismantlersProcessed = eWasteQtyDismantlersProcessed;
	}
	public double geteWasteQtyDismantlersRecoveredSold() {
		return eWasteQtyDismantlersRecoveredSold;
	}
	public void seteWasteQtyDismantlersRecoveredSold(double eWasteQtyDismantlersRecoveredSold) {
		this.eWasteQtyDismantlersRecoveredSold = eWasteQtyDismantlersRecoveredSold;
	}
	public double geteWasteQtyRecyclersSentTsdf() {
		return eWasteQtyRecyclersSentTsdf;
	}
	public void seteWasteQtyRecyclersSentTsdf(double eWasteQtyRecyclersSentTsdf) {
		this.eWasteQtyRecyclersSentTsdf = eWasteQtyRecyclersSentTsdf;
	}
	public double geteWasteQtyDismantlersSentRecyclers() {
		return eWasteQtyDismantlersSentRecyclers;
	}
	public void seteWasteQtyDismantlersSentRecyclers(double eWasteQtyDismantlersSentRecyclers) {
		this.eWasteQtyDismantlersSentRecyclers = eWasteQtyDismantlersSentRecyclers;
	}
	
}
