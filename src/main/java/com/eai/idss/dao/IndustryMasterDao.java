package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.IndustryMaster;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.ComparisonTableResponseVo;
import com.eai.idss.vo.ComplianceScoreResponseVo;
import com.eai.idss.vo.IndustryMasterRequest;
import com.eai.idss.vo.MandatoryReportsResponseVo;
import com.eai.idss.vo.PollutionScoreFilter;
import com.eai.idss.vo.PollutionScoreResponseVo;

public interface IndustryMasterDao {

	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page);
	
	public List<ComplianceScoreResponseVo> getByIndustryIdComplianceScoreData(ComlianceScoreFilter imr);

	public  List<PollutionScoreResponseVo> getPollutionScoreData(PollutionScoreFilter imr);

	public ComparisonTableResponseVo getComparisonData(long industryId,int consentYear,int esrYear,int form4Year);
	
	public MandatoryReportsResponseVo getMandatoryReportsData(long industryId,int year);
	
	public Map<String,String> getPollutionGraphParam(long industryId, String form);

}
