package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.User;
import com.eai.idss.vo.ConcentFilter;
import com.eai.idss.vo.ConsentDetailsRequest;
import com.eai.idss.vo.ConsentPaginationResponseVo;
import com.eai.idss.vo.TileVo;

public interface ConcentDao {

	public Map<String,List<TileVo>> getPendingRequestConcentData(ConcentFilter cf,String region,String subRegion);
	
	public Map<String,Map<String,List<TileVo>>> getUpcomingRenewalConcentData(String region,String subRegion);
	
	public Map<String,Map<String,List<TileVo>>> getByRegionConcentData(ConcentFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getBySubRegionConcentData(String region,ConcentFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getByTeamConcentData(ConcentFilter cf,User u);
	
	public ConsentPaginationResponseVo getConsentPaginatedRecords(ConsentDetailsRequest cdr, Pageable pageable);
	
	public Map<String,List<TileVo>> getUpcomingRenewalConcentDataNew(ConcentFilter cf,String region,String subRegion);
}
