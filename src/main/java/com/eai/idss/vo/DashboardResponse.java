package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

	private Map<String,Map<String,List<TileVo>>> dashboardMap;
	private List<TopPerfVo> topPerformers;
	private List<MyVisits> myVisits;

	
	public Map<String, Map<String, List<TileVo>>> getDashboardMap() {
		return dashboardMap;
	}
	public void setDashboardMap(Map<String, Map<String, List<TileVo>>> dashboardMap) {
		this.dashboardMap = dashboardMap;
	}
	public List<TopPerfVo> getTopPerformers() {
		return topPerformers;
	}
	public void setTopPerformers(List<TopPerfVo> topPerformers) {
		this.topPerformers = topPerformers;
	}
	public List<MyVisits> getMyVisits() {
		return myVisits;
	}
	public void setMyVisits(List<MyVisits> myVisits) {
		this.myVisits = myVisits;
	}
}
