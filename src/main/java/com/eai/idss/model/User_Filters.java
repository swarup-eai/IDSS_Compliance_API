package com.eai.idss.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eai.idss.vo.IndustryMasterRequest;

@Document(collection = "User_Filters")
public class User_Filters {

	@Id
	private String _id;
	private String filterName;
	private String userName;
	private IndustryMasterRequest imr;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
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
