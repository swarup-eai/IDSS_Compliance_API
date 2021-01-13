package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import com.eai.idss.vo.*;
import org.springframework.data.domain.Pageable;

import com.eai.idss.model.User;
import com.eai.idss.model.VisitProcessEfficiency;
import com.eai.idss.model.Visits;

public interface VisitsDao {

	public Map<String,List<TileVo>> getPendingVisitsData(VisitsFilter vf,String region, String subRegion);
	
	public Map<String,Map<String,List<TileVo>>> getByRegionVisitsData(VisitsFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getBySubRegionVisitsData(String region,VisitsFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getByTeamVisitsData(VisitsFilter cf,User u);
	
	public List<Visits> getVisitsPaginatedRecords(VisitsDetailsRequest cdr, Pageable pageable);
	
	public List<Visits> getVisitsSchedulePaginatedRecords(VisitsScheduleDetailsRequest cdr, Pageable pageable,String userName);
	
	public List<VisitScheduleCurrentMonthResponseVo> getVisitsScheduleByUserName(String userName);
	
	public VisitDetails getVisitDetailsForOneIndustryOneVisit(long industryId,long visitId);
	
	public List<Visits> getVisitDetailsForOneIndustry(long industryId,String fromDate,String toDate);
	
	public Map<String,List<VisitsByComplianceVo>> getVisitsByCompliance(String region, String subRegion);
	
	public VisitProcessEfficiency getVisitProcessEfficiency(String region);
	
}
