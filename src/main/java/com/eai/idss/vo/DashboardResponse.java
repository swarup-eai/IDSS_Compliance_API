package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

	private Map<String,Map<String,List<TileVo>>> dashboardMap;
	private List<TileVo> topPerformers;

	
	public Map<String, Map<String, List<TileVo>>> getDashboardMap() {
		return dashboardMap;
	}
	public void setDashboardMap(Map<String, Map<String, List<TileVo>>> dashboardMap) {
		this.dashboardMap = dashboardMap;
	}
	public List<TileVo> getTopPerformers() {
		return topPerformers;
	}
	public void setTopPerformers(List<TileVo> topPerformers) {
		this.topPerformers = topPerformers;
	}

}
