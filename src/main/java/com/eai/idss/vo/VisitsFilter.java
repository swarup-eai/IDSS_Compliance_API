package com.eai.idss.vo;

import java.util.List;

public class VisitsFilter {

	List<String> pendingScaleList;
	List<String> pendingCategoryList;
	List<String> pendingByTeamScaleList;
	List<String> pendingByTeamCategoryList;
	List<String> regionWiseScaleList;
	List<String> regionWiseCategoryList;
	List<String> subRegionWiseScaleList;
	List<String> subRegionWiseCategoryList;

	String region;
	String subRegion;

	public List<String> getPendingScaleList() {
		return pendingScaleList;
	}
	public void setPendingScaleList(List<String> pendingScaleList) {
		this.pendingScaleList = pendingScaleList;
	}
	public List<String> getPendingCategoryList() {
		return pendingCategoryList;
	}
	public void setPendingCategoryList(List<String> pendingCategoryList) {
		this.pendingCategoryList = pendingCategoryList;
	}
	
	public List<String> getRegionWiseScaleList() {
		return regionWiseScaleList;
	}
	public void setRegionWiseScaleList(List<String> regionWiseScaleList) {
		this.regionWiseScaleList = regionWiseScaleList;
	}
	public List<String> getRegionWiseCategoryList() {
		return regionWiseCategoryList;
	}
	public void setRegionWiseCategoryList(List<String> regionWiseCategoryList) {
		this.regionWiseCategoryList = regionWiseCategoryList;
	}
	public List<String> getPendingByTeamScaleList() {
		return pendingByTeamScaleList;
	}
	public void setPendingByTeamScaleList(List<String> pendingByTeamScaleList) {
		this.pendingByTeamScaleList = pendingByTeamScaleList;
	}
	public List<String> getPendingByTeamCategoryList() {
		return pendingByTeamCategoryList;
	}
	public void setPendingByTeamCategoryList(List<String> pendingByTeamCategoryList) {
		this.pendingByTeamCategoryList = pendingByTeamCategoryList;
	}
	public List<String> getSubRegionWiseScaleList() {
		return subRegionWiseScaleList;
	}
	public void setSubRegionWiseScaleList(List<String> subRegionWiseScaleList) {
		this.subRegionWiseScaleList = subRegionWiseScaleList;
	}
	public List<String> getSubRegionWiseCategoryList() {
		return subRegionWiseCategoryList;
	}
	public void setSubRegionWiseCategoryList(List<String> subRegionWiseCategoryList) {
		this.subRegionWiseCategoryList = subRegionWiseCategoryList;
	}

	public String getRegion() {
		return region;
	}
	public void setUpcomingRenewalCategoryList(String region) {
		this.region = region;
	}

	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}
}
