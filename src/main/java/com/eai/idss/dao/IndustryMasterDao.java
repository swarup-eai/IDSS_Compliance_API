package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.LegalDataMaster;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.IndustryMasterRequest;
import com.eai.idss.vo.PollutionScoreFilter;
import com.eai.idss.vo.PollutionScoreResponseVo;

public interface IndustryMasterDao {

	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page);
	
	public boolean saveIndustryMasterFilter(IndustryMasterRequest imr, String filterName);
	
	public Map<String, List<LegalDataMaster>> getByIndustryNameComplianceScoreData(ComlianceScoreFilter imr,Pageable page);

	public List<LegalDataMaster> getDataBetweenDuration(ComlianceScoreFilter imr,Pageable pageable);
	
	public Map<String,Map<String, List<PollutionScoreResponseVo>>> getByIndustryNamePollutionScoreData(PollutionScoreFilter imr,Pageable page);

	public Map<String, List<PollutionScoreResponseVo>> getComparisonData();

}
