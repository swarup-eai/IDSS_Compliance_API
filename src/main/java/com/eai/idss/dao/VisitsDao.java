package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.Visits;
import com.eai.idss.vo.TileVo;
import com.eai.idss.vo.VisitsDetailsRequest;
import com.eai.idss.vo.VisitsFilter;
import com.eai.idss.vo.VisitsScheduleDetailsRequest;

public interface VisitsDao {

	public Map<String,List<TileVo>> getPendingVisitsData(VisitsFilter vf);
	
	public Map<String,Map<String,List<TileVo>>> getByRegionVisitsData(VisitsFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getBySubRegionVisitsData(String region,VisitsFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getByTeamVisitsData(VisitsFilter cf,String region);
	
	public List<Visits> getVisitsPaginatedRecords(VisitsDetailsRequest cdr, Pageable pageable);
	
	public List<Visits> getVisitsSchedulePaginatedRecords(VisitsScheduleDetailsRequest cdr, Pageable pageable);
	
}
