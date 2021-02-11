package com.eai.idss.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eai.idss.dao.IndustryMasterDao;
import com.eai.idss.dao.OCEMSDataDaoImpl;
import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.OCEMS_Alerts;
import com.eai.idss.repository.OCEMSAlertsRepositoy;
import com.eai.idss.vo.PollutionScoreValueVo;

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

	@Scheduled(cron = "0 0 0/3 ? * * *")
	public void generateOCEMS3HoursAlerts() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String currentDayTime = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String threeHoursBack = currentTime.minusHours(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		List<IndustryMaster> filteredIndustryMaster = getIndustryList();
		for(IndustryMaster im : filteredIndustryMaster) {
			List<Map<String,String>> paramList = imd.getPollutionGraphParam(im.getIndustryId(),OCEMS);
			for(Map<String,String> m : paramList) {
				for(String value : m.values()) {
					List<PollutionScoreValueVo> psVoList = od.getOCEMSPollutionScoreValue(im.getIndustryId(),value,"2020-11-27T13:00:00.000+00:00","2020-11-27T16:00:00.000+00:00");
				}
			}
			createOCEMSAlert(im,"test");
		}
	}
	
	private void createOCEMSAlert(IndustryMaster im, String alertType) {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		OCEMS_Alerts oa = new OCEMS_Alerts();
		oa.setAlertCreatedDateTime(currentTime);
		oa.setDisabled(false);
		oa.setIndustryId(im.getIndustryId());
		oa.setIndustryName(im.getIndustryName());
		oa.setRegion(im.getRegion());
		oa.setSubRegion(im.getSubRegion());
//		oa.setRoUser(im.get);
		ocemsRepo.save(oa);
		
	}

	private List<IndustryMaster> getIndustryList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("applicationStatus").in(Arrays.asList("Approved","In Process")));
		query.with(Sort.by(Sort.Direction.ASC,"industryId"));
		
		logger.info("generateOCEMSAlerts Total Count="+mongoTemplate.count(query, IndustryMaster.class));
		
		return mongoTemplate.find(query, IndustryMaster.class);
	}
}
