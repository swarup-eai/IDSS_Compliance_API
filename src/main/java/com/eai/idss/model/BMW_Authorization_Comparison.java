package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BMW_Authorization_Comparison")
public class BMW_Authorization_Comparison {
	
	@Id
	private String _id;
	private String bioMedicalWasteName;
	private double bioMedicalWasteQuantity;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getBioMedicalWasteName() {
		return bioMedicalWasteName;
	}
	public void setBioMedicalWasteName(String bioMedicalWasteName) {
		this.bioMedicalWasteName = bioMedicalWasteName;
	}
	public double getBioMedicalWasteQuantity() {
		return bioMedicalWasteQuantity;
	}
	public void setBioMedicalWasteQuantity(double bioMedicalWasteQuantity) {
		this.bioMedicalWasteQuantity = bioMedicalWasteQuantity;
	}
}
