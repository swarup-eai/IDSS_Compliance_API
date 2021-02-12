package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.User;
import com.eai.idss.model.VisitProcessEfficiency;
import com.eai.idss.model.Visits;
import com.eai.idss.vo.DecisionMakingVo;
import com.eai.idss.vo.TileVo;
import com.eai.idss.vo.VisitDetails;
import com.eai.idss.vo.VisitScheduleCurrentMonthResponseVo;
import com.eai.idss.vo.VisitsByComplianceVo;
import com.eai.idss.vo.VisitsDetailsRequest;
import com.eai.idss.vo.VisitsFilter;
import com.eai.idss.vo.VisitsScheduleDetailsRequest;

public interface VisitsDao {

	public Map<String,List<TileVo>> getPendingVisitsData(VisitsFilter vf,String region, String subRegion);
	
	public Map<String,Map<String,List<TileVo>>> getByRegionVisitsData(VisitsFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getBySubRegionVisitsData(String region,VisitsFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getByTeamVisitsData(VisitsFilter cf,User u);
	
	public List<Visits> getVisitsPaginatedRecords(VisitsDetailsRequest cdr, Pageable pageable);
	
	public List<Visits> getVisitsSchedulePaginatedRecords(VisitsScheduleDetailsRequest cdr, Pageable pageable,String userName);
	
	public Map<String,List<TileVo>> getVisitsScheduleByScaleCategory(String userName);
	
	public VisitDetails getVisitDetailsForOneIndustryOneVisit(long industryId,long visitId);
	
	public List<Visits> getVisitDetailsForOneIndustry(long industryId,String fromDate,String toDate);
	
	public Map<String,List<VisitsByComplianceVo>> getVisitsByCompliance(String region, String subRegion);
	
	public VisitProcessEfficiency getVisitProcessEfficiency(String region,String duration);
	
	public List<VisitScheduleCurrentMonthResponseVo> getVisitsScheduleByUserName(String userName);
	
	public DecisionMakingVo getVisitDetailsDecisionMaking(long industryId,long visitId);
	
}
