package com.eai.idss.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
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
import com.eai.idss.model.OCEMS_data;
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
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Scheduled(cron = "0 0 1 1/2 * ? *")
	public void generateOCEMS48HoursAlerts() {
		logger.info("OCEMS Data 48 Hours alert check started.");
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String fourDaysBack = currentTime.minusHours(48).format(formatter);
		List<IndustryMaster> filteredIndustryMaster = getIndustryList();
		ExecutorService  executorService = Executors.newFixedThreadPool(5);
		for(IndustryMaster im : filteredIndustryMaster) {
			CompletableFuture.runAsync(() -> {
				if(!isOCEMSDataPresent(im.getIndustryId(),fourDaysBack)) {
					createOCEMSAlert(im,null,"No data captured for industry for 48 hours. ",currentTime.minusHours(96),currentTime);
				}
			},executorService);
		}
		logger.info("OCEMS Data 48 Hours alert check ended.");
	}
	
	@Scheduled(cron = "0 0 5 1/4 * ? *")
	public void generateOCEMS96HoursAlerts() {
		logger.info("OCEMS Data 96 Hours alert check started.");
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String fourDaysBack = currentTime.minusHours(96).format(formatter);
		List<IndustryMaster> filteredIndustryMaster = getIndustryList();
		ExecutorService  executorService = Executors.newFixedThreadPool(5);
		for(IndustryMaster im : filteredIndustryMaster) {
			CompletableFuture.runAsync(() -> {
				if(!isOCEMSDataPresent(im.getIndustryId(),fourDaysBack)) {
					createOCEMSAlert(im,null,"No data captured for industry for 96 hours. ",currentTime.minusHours(96),currentTime);
				}
			},executorService);
		}
		logger.info("OCEMS Data 96 Hours alert check ended.");
	}
	
	private boolean isOCEMSDataPresent(long inductryId,String fourDaysBack) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("industryId").is(inductryId));
			query.addCriteria(Criteria.where("time_stamp").gt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fourDaysBack)));
			query.limit(1);
			
			long cnt = mongoTemplate.count(query, OCEMS_data.class);
			logger.info("generateOCEMSAlerts for inductry = "+inductryId+", date-time="+fourDaysBack+", isOCEMSDataPresent Count="+cnt);
			
			return cnt>0?true:false;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Scheduled(cron = "0 0 0/3 ? * * *")
	public void generateOCEMS3HoursAlerts() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		logger.info("OCEMS Data 3 Hours alert check started.");
		String currentDayTime = currentTime.format(formatter);
		String threeHoursBack = currentTime.minusHours(3).format(formatter);
		String twoHoursBack = currentTime.minusHours(2).format(formatter);
		String oneHoursBack = currentTime.minusHours(1).format(formatter);
		List<IndustryMaster> filteredIndustryMaster = getIndustryList();
		
		ExecutorService  executorService = Executors.newFixedThreadPool(5);
		
		for(IndustryMaster im : filteredIndustryMaster) {
			CompletableFuture.runAsync(() -> {
				logger.info("Start - OCEMS Data alerts 3 hours Industry="+im.getIndustryId());
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
				logger.info("End - OCEMS Data alerts 3 hours Industry="+im.getIndustryId());
			},executorService);
		}
		
		logger.info("OCEMS Data 3 Hours alert check ended.");
	}

	private void logAlert(IndustryMaster im, String value, boolean b,String threshold,LocalDateTime startdt,LocalDateTime enddt) {
		if(!b)
			logger.info("OCEMS Data in limit for industry - "+im.getIndustryId()+", for parameter - "+value+", for threshold="+threshold);
		else {
			logger.info("OCEMS Data is NOT in limit for industry - "+im.getIndustryId()+", for parameter - "+value+", threshhold- "+threshold);
			createOCEMSAlert(im,value,"Value for the parameter "+value+" falls in range "+threshold,startdt,enddt);
		}
	}
	
	private void createOCEMSAlert(IndustryMaster im, String param, String alertType,LocalDateTime startdt,LocalDateTime enddt) {
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
		oa.setParameter(param);
		oa.setAlertType(alertType);
		ocemsRepo.save(oa);
		
	}

	private List<IndustryMaster> getIndustryList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("applicationStatus").in(Arrays.asList("Approved","In Process")));
//		query.addCriteria(Criteria.where("industryId").in(Arrays.asList(5704,17653)));
		query.with(Sort.by(Sort.Direction.ASC,"industryId"));
		
		logger.info("generateOCEMSAlerts Total Count="+mongoTemplate.count(query, IndustryMaster.class));
		
		return mongoTemplate.find(query, IndustryMaster.class);
	}
}
