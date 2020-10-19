package com.eai.idss.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.eai.idss.model.IndustryMaster;
import com.eai.idss.vo.IndustryMasterRequest;

public interface IndustryMasterDao {

	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page);
	
	public boolean saveIndustryMasterFilter(IndustryMasterRequest imr, String filterName);
}
