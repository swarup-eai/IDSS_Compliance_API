package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Plastic_annual_report_recycling_facility_form_IV")
public class Plastic_Annual_Report_Recycling_Facility_Form_IV {

	private double quantityReceived;
	private double quantityRecycled;
	private double quantityDisposed;
	private double quantityInerts;
	public double getQuantityReceived() {
		return quantityReceived;
	}
	public void setQuantityReceived(double quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	public double getQuantityRecycled() {
		return quantityRecycled;
	}
	public void setQuantityRecycled(double quantityRecycled) {
		this.quantityRecycled = quantityRecycled;
	}
	public double getQuantityDisposed() {
		return quantityDisposed;
	}
	public void setQuantityDisposed(double quantityDisposed) {
		this.quantityDisposed = quantityDisposed;
	}
	public double getQuantityInerts() {
		return quantityInerts;
	}
	public void setQuantityInerts(double quantityInerts) {
		this.quantityInerts = quantityInerts;
	}

}
