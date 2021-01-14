package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import com.eai.idss.model.IndustryTypes;
import com.eai.idss.vo.*;

public interface GenericDao {

	public Map<String,List<TileVo>> getConcentTileData(DashboardRequest dbr);
	
	public Map<String,List<TileVo>> getLegalTileData(DashboardRequest dbr);
	
	public Map<String,List<TileVo>> getVisitsTileData(DashboardRequest dbr);
	
	public List<MyVisits> getMyVisitsData(String userName);
	
	public List<TopPerfVo> getTopPerformer(String region);
	
	public List<String> getIndustryTypes(String category) ;

	public Map<String,List<HeatmapResponseVo>> getHeatmapData(DashboardRequest dbr);
}
