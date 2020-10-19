package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import com.eai.idss.vo.DashboardRequest;
import com.eai.idss.vo.TileVo;

public interface GenericDao {

	public Map<String,List<TileVo>> getConcentTileData(DashboardRequest dbr);
	
	public Map<String,List<TileVo>> getLegalTileData(DashboardRequest dbr);
	
	public Map<String,List<TileVo>> getVisitsTileData(DashboardRequest dbr);
	
	public Map<String,List<TileVo>> getMyVisitsData(String userName);
}
