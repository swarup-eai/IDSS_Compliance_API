package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.ComparisonTableResponseVo;
import com.eai.idss.vo.ComplianceScoreResponseVo;
import com.eai.idss.vo.IndustryMasterDetailResponseVo;
import com.eai.idss.vo.IndustryMasterPaginationResponseVo;
import com.eai.idss.vo.IndustryMasterRequest;
import com.eai.idss.vo.MandatoryReportsResponseVo;
import com.eai.idss.vo.PollutionScoreFilter;

public interface IndustryMasterDao {

	public IndustryMasterPaginationResponseVo getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page);
	
	public List<ComplianceScoreResponseVo> getByIndustryIdComplianceScoreData(ComlianceScoreFilter imr);

	public  List<Map<String,String>> getPollutionScoreData(PollutionScoreFilter imr);

	public ComparisonTableResponseVo getComparisonData(long industryId,int consentYear,int esrYear,int form4Year);
	
	public MandatoryReportsResponseVo getMandatoryReportsData(long industryId,int year);
	
	public List<Map<String,String>> getPollutionGraphParam(long industryId, String form);

	public IndustryMasterDetailResponseVo getIndustryMasterDetailByIndustryId(long industryId);
	
}
