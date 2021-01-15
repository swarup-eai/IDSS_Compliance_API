package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BMW_Annual_return_Comparison")
public class BMW_Annual_Return_Comparison {
	
	@Id
	private String _id;
	private double yellowCategory;
	private double redCategory;
	private double whiteCategory;
	private double blueCategory;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public double getYellowCategory() {
		return yellowCategory;
	}
	public void setYellowCategory(double yellowCategory) {
		this.yellowCategory = yellowCategory;
	}
	public double getRedCategory() {
		return redCategory;
	}
	public void setRedCategory(double redCategory) {
		this.redCategory = redCategory;
	}
	public double getWhiteCategory() {
		return whiteCategory;
	}
	public void setWhiteCategory(double whiteCategory) {
		this.whiteCategory = whiteCategory;
	}
	public double getBlueCategory() {
		return blueCategory;
	}
	public void setBlueCategory(double blueCategory) {
		this.blueCategory = blueCategory;
	}
}
