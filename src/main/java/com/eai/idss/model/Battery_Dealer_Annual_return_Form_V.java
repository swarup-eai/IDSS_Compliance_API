package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Battery_Dealer_Annual_return_Form_V")
public class Battery_Dealer_Annual_return_Form_V {

	private double twoWheelerBaterryCount;
	private double fourWheelerBaterryCount;
	private double upsBatteryCount;
	private double motivePowerCount;
	private double standByCount;
	private double othersCount;
	
	private double twoWheelerBaterryCountOld;
	private double fourWheelerBaterryCountOld;
	private double upsBatteryCountOld;
	private double motivePowerCountOld;
	private double standByCountOld;
	private double othersCountOld;
	
	private double dealersBatteryCount;
	private double bulkConsumersBatteryCount;
	private double oemBatteryCount;
	private double anyOtherParty;
	public double getTwoWheelerBaterryCount() {
		return twoWheelerBaterryCount;
	}
	public void setTwoWheelerBaterryCount(double twoWheelerBaterryCount) {
		this.twoWheelerBaterryCount = twoWheelerBaterryCount;
	}
	public double getFourWheelerBaterryCount() {
		return fourWheelerBaterryCount;
	}
	public void setFourWheelerBaterryCount(double fourWheelerBaterryCount) {
		this.fourWheelerBaterryCount = fourWheelerBaterryCount;
	}
	public double getUpsBatteryCount() {
		return upsBatteryCount;
	}
	public void setUpsBatteryCount(double upsBatteryCount) {
		this.upsBatteryCount = upsBatteryCount;
	}
	public double getMotivePowerCount() {
		return motivePowerCount;
	}
	public void setMotivePowerCount(double motivePowerCount) {
		this.motivePowerCount = motivePowerCount;
	}
	public double getStandByCount() {
		return standByCount;
	}
	public void setStandByCount(double standByCount) {
		this.standByCount = standByCount;
	}
	public double getOthersCount() {
		return othersCount;
	}
	public void setOthersCount(double othersCount) {
		this.othersCount = othersCount;
	}
	public double getTwoWheelerBaterryCountOld() {
		return twoWheelerBaterryCountOld;
	}
	public void setTwoWheelerBaterryCountOld(double twoWheelerBaterryCountOld) {
		this.twoWheelerBaterryCountOld = twoWheelerBaterryCountOld;
	}
	public double getFourWheelerBaterryCountOld() {
		return fourWheelerBaterryCountOld;
	}
	public void setFourWheelerBaterryCountOld(double fourWheelerBaterryCountOld) {
		this.fourWheelerBaterryCountOld = fourWheelerBaterryCountOld;
	}
	public double getUpsBatteryCountOld() {
		return upsBatteryCountOld;
	}
	public void setUpsBatteryCountOld(double upsBatteryCountOld) {
		this.upsBatteryCountOld = upsBatteryCountOld;
	}
	public double getMotivePowerCountOld() {
		return motivePowerCountOld;
	}
	public void setMotivePowerCountOld(double motivePowerCountOld) {
		this.motivePowerCountOld = motivePowerCountOld;
	}
	public double getStandByCountOld() {
		return standByCountOld;
	}
	public void setStandByCountOld(double standByCountOld) {
		this.standByCountOld = standByCountOld;
	}
	public double getOthersCountOld() {
		return othersCountOld;
	}
	public void setOthersCountOld(double othersCountOld) {
		this.othersCountOld = othersCountOld;
	}
	public double getDealersBatteryCount() {
		return dealersBatteryCount;
	}
	public void setDealersBatteryCount(double dealersBatteryCount) {
		this.dealersBatteryCount = dealersBatteryCount;
	}
	public double getBulkConsumersBatteryCount() {
		return bulkConsumersBatteryCount;
	}
	public void setBulkConsumersBatteryCount(double bulkConsumersBatteryCount) {
		this.bulkConsumersBatteryCount = bulkConsumersBatteryCount;
	}
	public double getOemBatteryCount() {
		return oemBatteryCount;
	}
	public void setOemBatteryCount(double oemBatteryCount) {
		this.oemBatteryCount = oemBatteryCount;
	}
	public double getAnyOtherParty() {
		return anyOtherParty;
	}
	public void setAnyOtherParty(double anyOtherParty) {
		this.anyOtherParty = anyOtherParty;
	}
	
}
