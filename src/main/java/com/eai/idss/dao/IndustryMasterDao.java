package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import com.eai.idss.vo.*;
import org.springframework.data.domain.Pageable;

import com.eai.idss.model.IndustryMaster;

public interface IndustryMasterDao {

	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page);
	
	public List<ComplianceScoreResponseVo> getByIndustryIdComplianceScoreData(ComlianceScoreFilter imr);

	public  List<Map<String,String>> getPollutionScoreData(PollutionScoreFilter imr);

	public ComparisonTableResponseVo getComparisonData(long industryId,int consentYear,int esrYear,int form4Year);
	
	public MandatoryReportsResponseVo getMandatoryReportsData(long industryId,int year);
	
	public List<Map<String,String>> getPollutionGraphParam(long industryId, String form);

	public IndustryMasterDetailResponseVo getIndustryMasterDetailByIndustryId(long industryId);
	
}
