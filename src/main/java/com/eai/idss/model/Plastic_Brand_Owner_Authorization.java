package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Plastic_brand_owner_authorization")
public class Plastic_Brand_Owner_Authorization {

	private double generationTotal;

	public double getGenerationTotal() {
		return generationTotal;
	}

	public void setGenerationTotal(double generationTotal) {
		this.generationTotal = generationTotal;
	}
	
}
