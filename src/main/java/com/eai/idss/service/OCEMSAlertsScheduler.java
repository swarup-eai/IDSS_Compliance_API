package com.eai.idss.service;

import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eai.idss.model.IndustryMaster;

@Component
public class OCEMSAlertsScheduler {
	
	public static final Logger logger = Logger.getLogger(OCEMSAlertsScheduler.class);
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Scheduled(cron = "59 * * * * *")
	public void generateOCEMSAlerts() {
		List<IndustryMaster> filteredIndustryMaster = getIndustryList();
	}

	private List<IndustryMaster> getIndustryList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("applicationStatus").in(Arrays.asList("Approved","In Process")));
		query.with(Sort.by(Sort.Direction.ASC,"industryId"));
		
		logger.info("generateOCEMSAlerts Total Count="+mongoTemplate.count(query, IndustryMaster.class));
		
		return mongoTemplate.find(query, IndustryMaster.class);
	}
}
