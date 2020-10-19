package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

	private Map<String,Map<String,List<TileVo>>> dashboardMap;
	private Map<String,Integer> topPerformanceRating;

	
	public Map<String, Map<String, List<TileVo>>> getDashboardMap() {
		return dashboardMap;
	}
	public void setDashboardMap(Map<String, Map<String, List<TileVo>>> dashboardMap) {
		this.dashboardMap = dashboardMap;
	}
	public Map<String, Integer> getTopPerformanceRating() {
		return topPerformanceRating;
	}
	public void setTopPerformanceRating(Map<String, Integer> topPerformanceRating) {
		this.topPerformanceRating = topPerformanceRating;
	}
	
}
