package com.eai.idss.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "OCEMS_Alerts")
public class OCEMS_Alerts {

	@Id
	private String _id;
	private long industryId;
	private String region;
	private String subRegion;
	private String industryName;
	private String roUser;
	private String sroUser;
	private String alertType;
	private boolean isDisabled;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime alertCreatedDateTime;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getRoUser() {
		return roUser;
	}
	public void setRoUser(String roUser) {
		this.roUser = roUser;
	}
	public String getSroUser() {
		return sroUser;
	}
	public void setSroUser(String sroUser) {
		this.sroUser = sroUser;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public boolean isDisabled() {
		return isDisabled;
	}
	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	public LocalDateTime getAlertCreatedDateTime() {
		return alertCreatedDateTime;
	}
	public void setAlertCreatedDateTime(LocalDateTime alertCreatedDateTime) {
		this.alertCreatedDateTime = alertCreatedDateTime;
	}

	
}