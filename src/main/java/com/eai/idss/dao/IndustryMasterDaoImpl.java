package com.eai.idss.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eai.idss.model.IndustryMaster;
import com.eai.idss.vo.IndustryMasterRequest;

@Repository
public class IndustryMasterDaoImpl implements IndustryMasterDao {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page){
		
		
		Query query = new Query().with(page);
		if(null!=imr) {
			if(StringUtils.hasText(imr.getRegion()) && !"All".equalsIgnoreCase(imr.getRegion()))
				query.addCriteria(Criteria.where("region").is(imr.getRegion()));
			if(StringUtils.hasText(imr.getCategory()) && !"All".equalsIgnoreCase(imr.getCategory()))
				query.addCriteria(Criteria.where("category").is(imr.getCategory()));
			if(StringUtils.hasText(imr.getScale()) && !"All".equalsIgnoreCase(imr.getScale()))
				query.addCriteria(Criteria.where("scale").is(imr.getScale()));
			if(StringUtils.hasText(imr.getType()) && !"All".equalsIgnoreCase(imr.getType()))
				query.addCriteria(Criteria.where("type").is(imr.getType()));
			if(null!=imr.getComplianceScore() )
				query.addCriteria(Criteria.where("complianceScore").is(imr.getComplianceScore()));
			if(StringUtils.hasText(imr.getLegalActions()) && !"All".equalsIgnoreCase(imr.getLegalActions()))
				query.addCriteria(Criteria.where("legalActions").is(imr.getLegalActions()));
			if(StringUtils.hasText(imr.getPendingCases()) && !"All".equalsIgnoreCase(imr.getPendingCases()))
				query.addCriteria(Criteria.where("pendingCases").is(imr.getPendingCases()));
		}
		

		System.out.println(mongoTemplate.count(query, IndustryMaster.class));
		
		List<IndustryMaster> filteredIndustryMaster= 
		mongoTemplate.find(query, IndustryMaster.class);
		Page<IndustryMaster> imPage = PageableExecutionUtils.getPage(
				filteredIndustryMaster,
				page,
		        () -> mongoTemplate.count(query, IndustryMaster.class));
		
		return imPage.toList();
	}

	public boolean saveIndustryMasterFilter(IndustryMasterRequest imr, String filterName) {
		
		return true;
	}
}
