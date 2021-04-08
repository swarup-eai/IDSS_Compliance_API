package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import com.eai.idss.vo.*;
import org.springframework.data.domain.Pageable;

import com.eai.idss.model.User;

public interface ConcentDao {

	public Map<String,List<TileVo>> getPendingRequestConcentData(ConcentFilter cf,String region,String subRegion);
	
	public Map<String,Map<String,List<TileVo>>> getUpcomingRenewalConcentData(String region,String subRegion);
	
	public Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>> getByRegionConcentData(ConcentFilter cf);
	
	public Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>> getBySubRegionConcentData(List<String> subRegion,ConcentFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getByTeamConcentData(ConcentFilter cf,User u);
	
	public ConsentPaginationResponseVo getConsentPaginatedRecords(ConsentDetailsRequest cdr, Pageable pageable);
	
	public Map<String,List<TileVo>> getUpcomingRenewalConcentDataNew(ConcentFilter cf,String region,String subRegion);
}
