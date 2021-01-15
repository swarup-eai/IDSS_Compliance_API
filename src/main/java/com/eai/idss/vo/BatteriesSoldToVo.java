package com.eai.idss.vo;

public class BatteriesSoldToVo {

	private double dealersBatteryCount;
	private double bulkConsumersBatteryCount;
	private double oemBatteryCount;
	private double anyOtherParty;
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
