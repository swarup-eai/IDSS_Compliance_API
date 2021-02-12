package com.eai.idss.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.eai.idss.dao.IndustryMasterDao;
import com.eai.idss.dao.OCEMSDataDaoImpl;
import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.OCEMS_Alerts;
import com.eai.idss.repository.OCEMSAlertsRepositoy;

@Component
public class OCEMSAlertsScheduler {
	
	public static final Logger logger = Logger.getLogger(OCEMSAlertsScheduler.class);
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	OCEMSAlertsRepositoy ocemsRepo;
	
	@Autowired
	IndustryMasterDao imd;
	
	@Autowired
	OCEMSDataDaoImpl od;
	
	private static final String OCEMS = "OCEMS";

	//@Scheduled(cron = "0 0 0/3 ? * * *")
	public void generateOCEMS3HoursAlerts() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String currentDayTime = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String threeHoursBack = currentTime.minusHours(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String twoHoursBack = currentTime.minusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String oneHoursBack = currentTime.minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		List<IndustryMaster> filteredIndustryMaster = getIndustryList();
		for(IndustryMaster im : filteredIndustryMaster) {
			List<Map<String,String>> paramList = imd.getPollutionGraphParam(im.getIndustryId(),OCEMS);
			for(Map<String,String> m : paramList) {
				String value = m.get("name").substring(m.get("name").indexOf("-")+1);
				boolean b = od.isParamValueInLimit(im.getIndustryId(),value,threeHoursBack,currentDayTime,"90-94");
				logAlert(im, value, b,"90-94",currentTime.minusHours(3),currentTime);
				
				b = od.isParamValueInLimit(im.getIndustryId(),value,twoHoursBack,currentDayTime,"94-98");
				logAlert(im, value, b,"94-98",currentTime.minusHours(2),currentTime);
				
				b = od.isParamValueInLimit(im.getIndustryId(),value,oneHoursBack,currentDayTime,"94-101");
				logAlert(im, value, b,"98-101",currentTime.minusHours(1),currentTime);
			}
		}
	}

	private void logAlert(IndustryMaster im, String value, boolean b,String threshold,LocalDateTime startdt,LocalDateTime enddt) {
		if(b)
			logger.info("OCEMS Data in limit for industry - "+im.getIndustryId()+", for parameter - "+value);
		else {
			logger.info("OCEMS Data is NOT in limit for industry - "+im.getIndustryId()+", for parameter - "+value+", threshhold- "+threshold);
			createOCEMSAlert(im,"Value for the parameter "+value+" falls in range "+threshold,startdt,enddt);
		}
	}
	
	private void createOCEMSAlert(IndustryMaster im, String alertType,LocalDateTime startdt,LocalDateTime enddt) {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		OCEMS_Alerts oa = new OCEMS_Alerts();
		oa.setAlertCreatedDateTime(currentTime);
		oa.setDisabled(false);
		oa.setIndustryId(im.getIndustryId());
		oa.setIndustryName(im.getIndustryName());
		oa.setRegion(im.getRegion());
		oa.setSubRegion(im.getSubRegion());
		oa.setSroUser(im.getSroEmailId());
		oa.setViolationStartDateTime(startdt);
		oa.setViolationEndDateTime(enddt);
		ocemsRepo.save(oa);
		
	}

	private List<IndustryMaster> getIndustryList() {
		Query query = new Query();
		//query.addCriteria(Criteria.where("applicationStatus").in(Arrays.asList("Approved","In Process")));
		query.addCriteria(Criteria.where("industryId").is(5704));
		query.with(Sort.by(Sort.Direction.ASC,"industryId"));
		
		logger.info("generateOCEMSAlerts Total Count="+mongoTemplate.count(query, IndustryMaster.class));
		
		return mongoTemplate.find(query, IndustryMaster.class);
	}
}
