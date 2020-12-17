package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.IndustryMaster;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.ComplianceScoreResponseVo;
import com.eai.idss.vo.IndustryMasterRequest;
import com.eai.idss.vo.PollutionScoreFilter;
import com.eai.idss.vo.PollutionScoreResponseVo;

public interface IndustryMasterDao {

	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page);
	
	public List<ComplianceScoreResponseVo> getByIndustryIdComplianceScoreData(ComlianceScoreFilter imr);

	public Map<String,Map<String, List<PollutionScoreResponseVo>>> getByIndustryNamePollutionScoreData(PollutionScoreFilter imr);

	public Map<String, List<PollutionScoreResponseVo>> getComparisonData(long industryId);

}
