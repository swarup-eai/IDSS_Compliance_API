package com.eai.idss.vo;

import java.util.List;

public class LegalFilter {

	List<String> pendingByTeamActionList;
	List<String> pendingByTeamCategoryList;
	List<String> regionWiseScaleList;
	List<String> regionWiseCategoryList;
	List<String> subRegionWiseScaleList;
	List<String> subRegionWiseCategoryList;
	
	List<String> pendingResponseByIndustryScaleList;
	List<String> pendingResponseByIndustryCategoryList;
	
	List<String> legalActionsByIndustryScaleList;
	List<String> legalActionsByIndustryCategoryList;

	String region;
	String subRegion;

	public List<String> getPendingResponseByIndustryScaleList() {
		return pendingResponseByIndustryScaleList;
	}
	public void setPendingResponseByIndustryScaleList(List<String> pendingResponseByIndustryScaleList) {
		this.pendingResponseByIndustryScaleList = pendingResponseByIndustryScaleList;
	}
	public List<String> getPendingResponseByIndustryCategoryList() {
		return pendingResponseByIndustryCategoryList;
	}
	public void setPendingResponseByIndustryCategoryList(List<String> pendingResponseByIndustryCategoryList) {
		this.pendingResponseByIndustryCategoryList = pendingResponseByIndustryCategoryList;
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
	public List<String> getPendingByTeamActionList() {
		return pendingByTeamActionList;
	}
	public void setPendingByTeamActionList(List<String> pendingByTeamActionList) {
		this.pendingByTeamActionList = pendingByTeamActionList;
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
	public List<String> getLegalActionsByIndustryScaleList() {
		return legalActionsByIndustryScaleList;
	}
	public void setLegalActionsByIndustryScaleList(List<String> legalActionsByIndustryScaleList) {
		this.legalActionsByIndustryScaleList = legalActionsByIndustryScaleList;
	}
	public List<String> getLegalActionsByIndustryCategoryList() {
		return legalActionsByIndustryCategoryList;
	}
	public void setLegalActionsByIndustryCategoryList(List<String> legalActionsByIndustryCategoryList) {
		this.legalActionsByIndustryCategoryList = legalActionsByIndustryCategoryList;
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
