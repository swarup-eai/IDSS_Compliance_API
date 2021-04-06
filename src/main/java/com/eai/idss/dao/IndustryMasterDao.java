package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import com.eai.idss.vo.*;
import org.springframework.data.domain.Pageable;

public interface IndustryMasterDao {

	public IndustryMasterPaginationResponseVo getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page);
	
	public List<ComplianceScoreResponseVo> getByIndustryIdComplianceScoreData(ComlianceScoreFilter imr);

	public  List<Map<String,String>> getPollutionScoreData(PollutionScoreFilter imr);

	public ComparisonTableResponseVo getComparisonData(long industryId,int consentYear,int esrYear,int form4Year);
	
	public MandatoryReportsResponseVo getMandatoryReportsData(long industryId,int year);
	
	public List<Map<String,String>> getPollutionGraphParam(long industryId, String form);

	public IndustryMasterDetailResponseVo getIndustryMasterDetailByIndustryId(long industryId);

	public List<String> getIndustryNameBySearch(String industryName);

	public List<ComplianceScoreTableResponseVo> getComplianceScpreDetailByIndustryId(long industryId);
	
}
