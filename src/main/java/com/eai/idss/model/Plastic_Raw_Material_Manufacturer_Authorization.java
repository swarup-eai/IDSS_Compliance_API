package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Plastic_raw_material_manufacturer_authorization")
public class Plastic_Raw_Material_Manufacturer_Authorization {

	private double produceQty;

	public double getProduceQty() {
		return produceQty;
	}

	public void setProduceQty(double produceQty) {
		this.produceQty = produceQty;
	}

}
