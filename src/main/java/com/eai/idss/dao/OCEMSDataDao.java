package com.eai.idss.dao;

import java.util.List;

import com.eai.idss.vo.PollutionScoreValueVo;

public interface OCEMSDataDao {

	public List<PollutionScoreValueVo> getOCEMSPollutionScoreValue(long industryId, String paramValue, String fromDate,String toDate);

}
