package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.eai.idss.vo.IndustryMasterRequest;

@Document(collection = "User_Filters")
public class User_Filters {

	private String filterName;
	private String userName;
	private IndustryMasterRequest imr;
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public IndustryMasterRequest getImr() {
		return imr;
	}
	public void setImr(IndustryMasterRequest imr) {
		this.imr = imr;
	}
	
	
}
