package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.Legal;
import com.eai.idss.vo.LegalDetailsRequest;
import com.eai.idss.vo.LegalFilter;
import com.eai.idss.vo.TileVo;

public interface LegalDao {

	public Map<String,List<TileVo>> getPendingLegalActionsData();
	
	public Map<String,Map<String,List<TileVo>>> getLegalActionsByIndustryScaleCategoryData();
	
	public Map<String,Map<String,List<TileVo>>> getByRegionLegalData(LegalFilter cf);
	
	public Map<String,Map<String,List<TileVo>>> getBySubRegionLegalData(String region,LegalFilter cf);
	
	public Map<String,List<TileVo>> getByTeamLegalData(LegalFilter cf);
	
	public List<Legal> getLegalPaginatedRecords(LegalDetailsRequest cdr, Pageable pageable);
}
