package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Plastic_recycler_authorization")
public class Plastic_Recycler_Authorization {

	private double wasteQuantumProcess;

	public double getWasteQuantumProcess() {
		return wasteQuantumProcess;
	}

	public void setWasteQuantumProcess(double wasteQuantumProcess) {
		this.wasteQuantumProcess = wasteQuantumProcess;
	}

}
