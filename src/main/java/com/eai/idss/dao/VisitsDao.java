package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.Visits;
import com.eai.idss.vo.VisitsDetailsRequest;
import com.eai.idss.vo.VisitsFilter;
import com.eai.idss.vo.TileVo;

public interface VisitsDao {

	public Map<String,List<TileVo>> getPendingVisitsData();
	
	public Map<String,Map<String,List<TileVo>>> getByRegionVisitsData(VisitsFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getBySubRegionVisitsData(String region,VisitsFilter cf);
	
	public Map<String,List<TileVo>> getByTeamVisitsData(VisitsFilter cf);
	
	public List<Visits> getVisitsPaginatedRecords(VisitsDetailsRequest cdr, Pageable pageable);
}
