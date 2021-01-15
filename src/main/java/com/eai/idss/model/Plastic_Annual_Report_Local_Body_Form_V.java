package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Plastic_annual_report_local_body_form_V")
public class Plastic_Annual_Report_Local_Body_Form_V {

	private double plasticWasteGeneratedQuantity;
	private double plasticWasteCollectedQuantity;
	private double plasticWasteRecycledQuantity;
	private double plasticWasteProcessedQuantity;
	private double landPlasticWasteQuantity;
	public double getPlasticWasteGeneratedQuantity() {
		return plasticWasteGeneratedQuantity;
	}
	public void setPlasticWasteGeneratedQuantity(double plasticWasteGeneratedQuantity) {
		this.plasticWasteGeneratedQuantity = plasticWasteGeneratedQuantity;
	}
	public double getPlasticWasteCollectedQuantity() {
		return plasticWasteCollectedQuantity;
	}
	public void setPlasticWasteCollectedQuantity(double plasticWasteCollectedQuantity) {
		this.plasticWasteCollectedQuantity = plasticWasteCollectedQuantity;
	}
	public double getPlasticWasteRecycledQuantity() {
		return plasticWasteRecycledQuantity;
	}
	public void setPlasticWasteRecycledQuantity(double plasticWasteRecycledQuantity) {
		this.plasticWasteRecycledQuantity = plasticWasteRecycledQuantity;
	}
	public double getPlasticWasteProcessedQuantity() {
		return plasticWasteProcessedQuantity;
	}
	public void setPlasticWasteProcessedQuantity(double plasticWasteProcessedQuantity) {
		this.plasticWasteProcessedQuantity = plasticWasteProcessedQuantity;
	}
	public double getLandPlasticWasteQuantity() {
		return landPlasticWasteQuantity;
	}
	public void setLandPlasticWasteQuantity(double landPlasticWasteQuantity) {
		this.landPlasticWasteQuantity = landPlasticWasteQuantity;
	}

}
