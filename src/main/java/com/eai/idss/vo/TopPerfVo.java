package com.eai.idss.vo;

public class TopPerfVo {

	private String region;
	private double rating;
	
	public TopPerfVo() {
		
	}
	
	public TopPerfVo(String region,int rating) {
		this.region = region;
		this.rating = rating;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
