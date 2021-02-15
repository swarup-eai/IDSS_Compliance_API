package com.eai.idss.dao;

public interface OCEMSDataDao {

	public boolean isParamValueInLimit(long industryId, String paramValue, String fromDate,String toDate,String thresholdRange);
	
}
