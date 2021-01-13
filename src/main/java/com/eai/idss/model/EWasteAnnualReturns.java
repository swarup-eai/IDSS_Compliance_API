package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "e_waste_annual_returns")
public class EWasteAnnualReturns {

	private String eWasteNameProducer;
	private double eWasteQtyProducer;
	public String geteWasteNameProducer() {
		return eWasteNameProducer;
	}
	public void seteWasteNameProducer(String eWasteNameProducer) {
		this.eWasteNameProducer = eWasteNameProducer;
	}
	public double geteWasteQtyProducer() {
		return eWasteQtyProducer;
	}
	public void seteWasteQtyProducer(double eWasteQtyProducer) {
		this.eWasteQtyProducer = eWasteQtyProducer;
	}
}
