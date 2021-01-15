package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Plastic_producer_authorization")
public class Plastic_Producer_Authorization {

	private double generationTotal;

	public double getGenerationTotal() {
		return generationTotal;
	}

	public void setGenerationTotal(double generationTotal) {
		this.generationTotal = generationTotal;
	}
	
}
