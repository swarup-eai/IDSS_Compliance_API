package com.eai.idss.dao;

import java.util.List;
import java.util.Map;

import com.eai.idss.model.OCEMS_Alerts;

public interface OCEMSDataDao {

	public boolean isParamValueInLimit(long industryId, String paramValue, String fromDate,String toDate,String thresholdRange);
	
	public List<Map<String,String>> getOCEMSPollutionScoreValue(OCEMS_Alerts oa) ;
	
}
