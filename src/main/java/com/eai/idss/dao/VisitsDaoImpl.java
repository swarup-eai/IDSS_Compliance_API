package com.eai.idss.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eai.idss.model.User;
import com.eai.idss.model.VisitProcessEfficiency;
import com.eai.idss.model.Visits;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.ConcentByRegionVo;
import com.eai.idss.vo.TileVo;
import com.eai.idss.vo.VisitDetails;
import com.eai.idss.vo.VisitScheduleCurrentMonthResponseVo;
import com.eai.idss.vo.VisitsByComplianceVo;
import com.eai.idss.vo.VisitsByScaleCategory;
import com.eai.idss.vo.VisitsDetailsRequest;
import com.eai.idss.vo.VisitsFilter;
import com.eai.idss.vo.VisitsScheduleDetailsRequest;
import com.eai.idss.vo.VisitsSubRegionVo;
import com.eai.idss.vo.VisitsTeamVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class VisitsDaoImpl implements VisitsDao {
	
	private static final String LEGAL_ACTION = "legalAction";

	private static final String REPORT_FILED = "reportFiled";

	private static final String REPORTS = "Reports";

	private static final String NA = "NA";

	private static final String VISITED = "Visited";

	private static final String TEAM_WISE = "TeamWise";

	private static final String REGION_WISE = "RegionWise";
	
	private static final String SUB_REGION_WISE = "SubRegionWise";

	private static final String LEGAL_NOTICES = "legalNotices";

	private static final String COMPLETED = "completed";

	private static final String PENDING = "Pending";

	private static final String SCHEDULED = "Scheduled";

	public static final Logger logger = Logger.getLogger(VisitsDaoImpl.class);
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoClient mongoClient;

	public Map<String,List<TileVo>> getPendingVisitsData(VisitsFilter vf,String region, String subRegion){
		try {
			logger.info("getPendingVisitsData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForVisits();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getPendingVisitsData : "+days);
	            List<? extends Bson> pipeline = getPendingVisitsPipeline(daysMap.get(days),vf,region, subRegion);
	            
	            List<TileVo> tVoList = new ArrayList<TileVo>();
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										TileVo tVo = new ObjectMapper().readValue(document.toJson(), TileVo.class);
										tVoList.add(tVo);
									
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (JsonProcessingException e) {
										e.printStackTrace();
									}
		    	                    
		    	                }
		    	            }
	                    );
	            tileMap.put(days, tVoList);
            
            }
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getPendingVisitsPipeline(List<String> days,VisitsFilter vf,String region, String subRegion) throws ParseException {
		
		Document matchDoc = new Document();
		
		matchDoc.append("schduledOn", new Document()
							.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
							.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
						);
		matchDoc.append("visitStatus", PENDING);

		if(!"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		if(!"ALL".equalsIgnoreCase(subRegion))
			matchDoc.append("subRegion",subRegion);
		
		if(null!=vf.getPendingScaleList())
			matchDoc.append("scale", new Document().append("$in", vf.getPendingScaleList()));
		if(null!=vf.getPendingCategoryList())
			matchDoc.append("category", new Document().append("$in", vf.getPendingCategoryList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", "$scale")
		                        .append("caseCount", new Document()
		                                .append("$sum", 1)
		                        )
		                ),
		        new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("caseType", "$_id")
		                    .append("caseCount", "$caseCount")
		            )
				);
		return pipeline;
	}
	
		
	
	public Map<String,Map<String,List<TileVo>>> getByRegionVisitsData(VisitsFilter cf){
		try {
			logger.info("getByRegionLegalData");
			Map<String, List<String>> daysMap = IDSSUtil.getPastAndFutureDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,Map<String,List<TileVo>>> byRegionMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getByRegionVisitsData : "+days);
            	Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();
            	
            	List<? extends Bson> pipeline = getRegionVisitsPipeline(PENDING,daysMap.get(days).get(0),cf);
	            
	            extractData(collection, regionVisitMap, pipeline,PENDING,REGION_WISE);
            
	            pipeline = getRegionVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf);
	            
	            extractData(collection, regionVisitMap, pipeline,SCHEDULED,REGION_WISE);
	            
	            pipeline = getRegionVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(0),cf);
	            
	            extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,REGION_WISE);

	            pipeline = getRegionVisitsPipeline(COMPLETED,daysMap.get(days).get(0),cf);
	            
	            extractData(collection, regionVisitMap, pipeline,COMPLETED,REGION_WISE);
	            
	            byRegionMap.put(days,regionVisitMap);
            
            }
            return byRegionMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void extractData(MongoCollection<Document> collection, Map<String, List<TileVo>> regionVisitMap,
			List<? extends Bson> pipeline, String type,String extractType) {
		collection.aggregate(pipeline)
		        .allowDiskUse(false)
		        .forEach(new Consumer<Document>() {
		                @Override
		                public void accept(Document document) {
		                    logger.info(type +" ::: "+document.toJson());
							try {
								if(REGION_WISE.equalsIgnoreCase(extractType)) {
									ConcentByRegionVo crVo = new ObjectMapper().readValue(document.toJson(), ConcentByRegionVo.class);
									TileVo tVo = new TileVo(crVo.getRegion(),crVo.getCount());
									List<TileVo> concentStatusList = regionVisitMap.get(crVo.getRegion());
									if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
									concentStatusList.add(tVo);
									regionVisitMap.put(type, concentStatusList);
								}else if(TEAM_WISE.equalsIgnoreCase(extractType)) {
									VisitsTeamVo crVo = new ObjectMapper().readValue(document.toJson(), VisitsTeamVo.class);
									TileVo tVo = new TileVo(type,crVo.getCount());
									List<TileVo> concentStatusList = regionVisitMap.get(crVo.getName()+"~"+crVo.getDesignation());
									if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
									concentStatusList.add(tVo);
									regionVisitMap.put(crVo.getName()+"~"+crVo.getDesignation(), concentStatusList);
								}else if(SUB_REGION_WISE.equalsIgnoreCase(extractType)) {
									VisitsSubRegionVo crVo = new ObjectMapper().readValue(document.toJson(), VisitsSubRegionVo.class);
									TileVo tVo = new TileVo(type,crVo.getCount());
									List<TileVo> concentStatusList = regionVisitMap.get(crVo.getSubRegion());
									if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
									concentStatusList.add(tVo);
									regionVisitMap.put(crVo.getSubRegion(), concentStatusList);
								}
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
		                    
		                }
		            }
		        );
	}
	
	private List<? extends Bson> getRegionVisitsPipeline(String caseType,String days,VisitsFilter cf) throws ParseException {
		
		Document matchDoc = new Document();
		
		applyMatchFilter(caseType, days, matchDoc);
		
		if(null!=cf && null!=cf.getRegionWiseCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getRegionWiseCategoryList()));
		if(null!=cf && null!=cf.getRegionWiseScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getRegionWiseScaleList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
                new Document()
		                .append("$group", new Document()
		                        .append("_id", "$region")
		                        .append("count", new Document()
		                                .append("$sum", 1)
		                        )
		                ), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("region", "$_id")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("region", 1.0)
                        )
        );
		return pipeline;
	}

	private void applyMatchFilter(String caseType, String days, Document matchDoc) throws ParseException {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		if(SCHEDULED.equalsIgnoreCase(caseType)) {
			matchDoc.append("schduledOn", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateToday+" 00:00:00.000+0000"))
					.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
		}
		if(PENDING.equalsIgnoreCase(caseType)) {
			matchDoc.append("schduledOn", new Document()
					.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateToday+" 00:00:00.000+0000"))
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("visitStatus", PENDING);
		}
		
		if(COMPLETED.equalsIgnoreCase(caseType)) {
			matchDoc.append("visitedDate", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("visitStatus",VISITED);
		}
		
		if(VISITED.equalsIgnoreCase(caseType)) {
			matchDoc.append("visitedDate", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("visitStatus",VISITED);
		}
		
		if(LEGAL_NOTICES.equalsIgnoreCase(caseType)) {
			matchDoc.append("legalDirectionIssuedOn", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("legalDirection", new Document().append("$ne", NA));
		}
		
		if(REPORTS.equalsIgnoreCase(caseType)) {
			matchDoc.append("visitedDate", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("visitReportFile",new Document().append("$ne",null));
		}
	}
	
	
	public  Map<String,Map<String,List<TileVo>>> getBySubRegionVisitsData(String region, VisitsFilter cf){
		try {
			logger.info("getBySubRegionVisitData");
			Map<String, List<String>> daysMap = IDSSUtil.getPastAndFutureDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,Map<String,List<TileVo>>> byRegionMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getBySubRegionVisitsData : "+days);
            	Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();
            	
            	List<? extends Bson> pipeline = getSubRegionVisitsPipeline(PENDING,daysMap.get(days).get(0),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,PENDING,SUB_REGION_WISE);
            
	            pipeline = getSubRegionVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,SCHEDULED,SUB_REGION_WISE);
	            
	            pipeline = getSubRegionVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(0),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,SUB_REGION_WISE);

	            pipeline = getSubRegionVisitsPipeline(COMPLETED,daysMap.get(days).get(0),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,COMPLETED,SUB_REGION_WISE);
	            
	            byRegionMap.put(days,regionVisitMap);
            
            }
            return byRegionMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getSubRegionVisitsPipeline(String caseType,String days,VisitsFilter cf,String region) throws ParseException {
		
		Document matchDoc = new Document();
		
		applyMatchFilter(caseType, days, matchDoc);
		
		if(!"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		
		if(null!=cf && null!=cf.getSubRegionWiseCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getSubRegionWiseCategoryList()));
		if(null!=cf && null!=cf.getSubRegionWiseScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getSubRegionWiseScaleList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
                new Document()
		                .append("$group", new Document()
		                        .append("_id", "$subRegion")
		                        .append("count", new Document()
		                                .append("$sum", 1)
		                        )
		                ), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("subRegion", "$_id")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("subRegion", 1.0)
                        )
        );
		return pipeline;
	}
	
	public Map<String,Map<String,List<TileVo>>> getByTeamVisitsData(VisitsFilter cf,User u){
		try {
			logger.info("getByTeamVisitsData");
			Map<String, List<String>> daysMap = IDSSUtil.getPastAndFutureDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,Map<String,List<TileVo>>> byRegionMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getByTeamVisitsData : "+days);
            	Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();
            	if("RO".equalsIgnoreCase(u.getDesignation())) {
            		getROTeamDetails(cf, u, daysMap, collection, days, regionVisitMap);
            	}else {
	            	getSROFOTeamDetails(cf, u, daysMap, collection, days, regionVisitMap);
            	}
	            byRegionMap.put(days,regionVisitMap);
            
            }
            return byRegionMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void getSROFOTeamDetails(VisitsFilter cf, User u, Map<String, List<String>> daysMap,
			MongoCollection<Document> collection, String days, Map<String, List<TileVo>> regionVisitMap)
			throws ParseException {
		List<? extends Bson> pipeline;
		pipeline = getByTeamVisitsPipeline(PENDING,daysMap.get(days).get(0),cf,u,u.getDesignation());
		
		extractData(collection, regionVisitMap, pipeline,PENDING,TEAM_WISE);
         
		pipeline = getByTeamVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,u,u.getDesignation());
		
		extractData(collection, regionVisitMap, pipeline,SCHEDULED,TEAM_WISE);
		
		pipeline = getByTeamVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(0),cf,u,u.getDesignation());
		
		extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,TEAM_WISE);
		
		pipeline = getByTeamVisitsPipeline(VISITED,daysMap.get(days).get(0),cf,u,u.getDesignation());
		
		extractData(collection, regionVisitMap, pipeline,VISITED,TEAM_WISE);
		
		pipeline = getByTeamVisitsPipeline(REPORTS,daysMap.get(days).get(0),cf,u,u.getDesignation());
		
		extractData(collection, regionVisitMap, pipeline,REPORTS,TEAM_WISE);
	}

	private void getROTeamDetails(VisitsFilter cf, User u, Map<String, List<String>> daysMap,
			MongoCollection<Document> collection, String days, Map<String, List<TileVo>> regionVisitMap)
			throws ParseException {
		List<? extends Bson> pipeline;
		pipeline = getByTeamVisitsPipeline(PENDING,daysMap.get(days).get(0),cf,u,"RO~SRO");
		
		extractData(collection, regionVisitMap, pipeline,PENDING,TEAM_WISE);
		
			pipeline = getByTeamVisitsPipeline(PENDING,daysMap.get(days).get(0),cf,u,"RO~FO");
			extractData(collection, regionVisitMap, pipeline,PENDING,TEAM_WISE);
            
		pipeline = getByTeamVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,u,"RO~SRO");
		
		extractData(collection, regionVisitMap, pipeline,SCHEDULED,TEAM_WISE);
		
			pipeline = getByTeamVisitsPipeline(SCHEDULED,daysMap.get(days).get(0),cf,u,"RO~FO");
			extractData(collection, regionVisitMap, pipeline,SCHEDULED,TEAM_WISE);
		
		pipeline = getByTeamVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(0),cf,u,"RO~SRO");
		
		extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,TEAM_WISE);
		
			pipeline = getByTeamVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(0),cf,u,"RO~FO");
			extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(VISITED,daysMap.get(days).get(0),cf,u,"RO~SRO");
		
		extractData(collection, regionVisitMap, pipeline,VISITED,TEAM_WISE);
		
			pipeline = getByTeamVisitsPipeline(VISITED,daysMap.get(days).get(0),cf,u,"RO~FO");
			extractData(collection, regionVisitMap, pipeline,VISITED,TEAM_WISE);
		
		pipeline = getByTeamVisitsPipeline(REPORTS,daysMap.get(days).get(0),cf,u,"RO~SRO");
		
		extractData(collection, regionVisitMap, pipeline,REPORTS,TEAM_WISE);
		
			pipeline = getByTeamVisitsPipeline(REPORTS,daysMap.get(days).get(0),cf,u,"RO~FO");
			extractData(collection, regionVisitMap, pipeline,REPORTS,TEAM_WISE);
	}
	
	private List<? extends Bson> getByTeamVisitsPipeline(String caseType,String days,VisitsFilter cf,User u,String dataLeval) throws ParseException {
		
		Document matchDoc = new Document();
		Document groupDoc = new Document();
		
		applyMatchFilter(caseType, days, matchDoc);
		
		if("RO~FO".equalsIgnoreCase(dataLeval)) {
			matchDoc.append("finalReportingToUserId",u.getUserName());
			matchDoc.append("finalReportingToDesignation","RO");
			
			groupDoc
                    .append("_id", new Document()
                            .append("name", "$reportingToName")
                            .append("designation", "FO")
                    )
                    .append("count", new Document()
                            .append("$sum", 1.0)
                    );
		}
		else if("RO~SRO".equalsIgnoreCase(dataLeval)) {
			matchDoc.append("reportingToUserId",u.getUserName());
			matchDoc.append("reportingToDesignation","RO");
			
			groupDoc
                    .append("_id", new Document()
                            .append("name", "$adminName")
                            .append("designation", "SRO")
                    )
                    .append("count", new Document()
                            .append("$sum", 1.0)
                    );
		}
		else if("SRO".equalsIgnoreCase(dataLeval)) {
			matchDoc.append("reportingToUserId",u.getUserName());
			matchDoc.append("reportingToDesignation","SRO");
			
			groupDoc
                    .append("_id", new Document()
                            .append("name", "$adminName")
                            .append("designation", "FO")
                    )
                    .append("count", new Document()
                            .append("$sum", 1.0)
                    );
		}
		else {
			matchDoc.append("region",u.getRegion());
			matchDoc.append("adminDesignation","FO");
			
			if(!"ALL".equalsIgnoreCase(u.getSubRegion()))
				matchDoc.append("subRegion",u.getSubRegion());
			groupDoc
                    .append("_id", new Document()
                            .append("name", "$adminName")
                            .append("designation", "FO")
                    )
                    .append("count", new Document()
                            .append("$sum", 1.0)
                    );
		}
		
		if(null!=cf && null!=cf.getPendingByTeamCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getPendingByTeamCategoryList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
				new Document().append("$group", groupDoc), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("name", "$_id.name")
                                .append("designation", "$_id.designation")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("name", 1.0)
                        )
        );
		return pipeline;
	}
	
	public List<Visits> getVisitsPaginatedRecords(VisitsDetailsRequest cdr, Pageable page){
		try {
			Query query = new Query().with(page);
			if(null!=cdr) {
				List<String> days = new ArrayList<String>(); 
				LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
				
				if(SCHEDULED.equalsIgnoreCase(cdr.getVisitStatus())) {
					days.add(currentTime.plusDays(Integer.parseInt(cdr.getDuration())).format(DateTimeFormatter.ISO_LOCAL_DATE));
					days.add(currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
				}
				else {
					if(StringUtils.hasText(cdr.getDuration())) {
						String[] d = cdr.getDuration().split("_");
						
						LocalDateTime fromDate = currentTime.minusDays(Integer.parseInt(d[0]));
						String fromDay = fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
						days.add(fromDay);
						if("ALL".equalsIgnoreCase(d[1])) {
							days.add("1970-01-01");
						}else {
							LocalDateTime toDate = currentTime.minusDays(Integer.parseInt(d[1]));
							String toDay = toDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
							days.add(toDay);
						}
					}
					
				}
				
				addCriteriaFilter(cdr.getVisitStatus(), days, query);
				
				if(StringUtils.hasText(cdr.getRegion()))
					query.addCriteria(Criteria.where("region").is(cdr.getRegion()));
				if(null!= cdr.getCategory() && !cdr.getCategory().isEmpty())
					query.addCriteria(Criteria.where("category").in(cdr.getCategory()));
				if(StringUtils.hasText(cdr.getSubRegion()))
					query.addCriteria(Criteria.where("subRegion").is(cdr.getSubRegion()));
				if(null!=cdr.getScale() && !cdr.getScale().isEmpty())
					query.addCriteria(Criteria.where("scale").in(cdr.getScale()));
			}
	
			logger.info(mongoTemplate.count(query, Visits.class));
			
			List<Visits> filteredVisitsList= mongoTemplate.find(query, Visits.class);
			
			filteredVisitsList.stream().forEach(v -> v.setElapsedDays(ChronoUnit.DAYS.between(LocalDate.now(),v.getSchduledOn())));
			
			Page<Visits> cPage = PageableExecutionUtils.getPage(
					filteredVisitsList,
					page,
			        () -> mongoTemplate.count(query, Visits.class));
			
			return cPage.toList();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void addCriteriaFilter(String caseType, List<String> days, Query query) throws ParseException {
		
		
		if(SCHEDULED.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("schduledOn")
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000")));
		}
		if(PENDING.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("schduledOn")
					.lt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("visitStatus").is(PENDING));
		}
		
		if(COMPLETED.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("visitedDate")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("visitStatus").is(VISITED));
		}
		
		if(LEGAL_NOTICES.equalsIgnoreCase(caseType)) {
			
			query.addCriteria(Criteria.where("legalDirectionIssuedOn")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("legalDirection").ne(NA));
		}
	}
	
	public List<Visits> getVisitsSchedulePaginatedRecords(VisitsScheduleDetailsRequest cdr, Pageable pageable,String userName){
		try {
			Query query = new Query().with(pageable);
			if(null!=cdr) {
				String[] my = cdr.getMonth().split("-");

				YearMonth ym = YearMonth.of(Integer.parseInt(my[1]),Integer.parseInt(my[0]));
				LocalDate startDate = ym.atDay(1);
				LocalDate endDate = ym.atEndOfMonth();
				
				if("Historical".equalsIgnoreCase(cdr.getWhen())) {
					Criteria c = new Criteria();
					c.orOperator(Criteria.where("visitedDate")
							.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(startDate+" 00:00:00.000+0000"))
							.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(endDate+" 00:00:00.000+0000")),
							
							Criteria.where("schduledOn")
							.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(startDate+" 00:00:00.000+0000"))
							.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(endDate+" 00:00:00.000+0000"))
							);
						query.addCriteria(c);
						
				}else {
						query.addCriteria(Criteria.where("schduledOn")
								.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(startDate+" 00:00:00.000+0000"))
								.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(endDate+" 00:00:00.000+0000")));
				}
			}
				
			if(StringUtils.hasText(cdr.getCompliance()) && !"ALL".equalsIgnoreCase(cdr.getCompliance())) {
				String[] op = cdr.getCompliance().split("-");
				query.addCriteria(Criteria.where("cscore").gte(Integer.parseInt(op[0])).lte(Integer.parseInt(op[1])));
			}
			
			if(StringUtils.hasText(cdr.getStatus()) && !"ALL".equalsIgnoreCase(cdr.getStatus())) {
				if("Pending".equalsIgnoreCase(cdr.getStatus()) || "Scheduled".equalsIgnoreCase(cdr.getStatus()))
					query.addCriteria(Criteria.where("visitStatus").is(PENDING));
				if(VISITED.equalsIgnoreCase(cdr.getStatus()))
					query.addCriteria(Criteria.where("visitStatus").is(VISITED));
			}
			
			query.addCriteria(Criteria.where("userId").is(userName));
	
			logger.info(mongoTemplate.count(query, Visits.class));
			
			List<Visits> filteredLegalList= mongoTemplate.find(query, Visits.class);
			
			Page<Visits> cPage = PageableExecutionUtils.getPage(
					filteredLegalList,
					pageable,
			        () -> mongoTemplate.count(query, Visits.class));
			
			return cPage.toList();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String,List<TileVo>> getVisitsScheduleByScaleCategory(String userName){
		try {
			logger.info("getVisitsScheduleByScaleCategory");
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            List<? extends Bson> pipeline = getCurrentMonthVisitsPipeline(userName);
	            
            collection.aggregate(pipeline)
                    .allowDiskUse(false)
                    .forEach(new Consumer<Document>() {
	    	                @Override
	    	                public void accept(Document document) {
	    	                    logger.info(document.toJson());
								try {
									VisitsByScaleCategory vVo = new ObjectMapper().readValue(document.toJson(), VisitsByScaleCategory.class);
									List<TileVo> ltvo = tileMap.get(vVo.getScale());
									if(null==ltvo) {
										ltvo = new ArrayList<TileVo>();
									}
									ltvo.add(new TileVo(vVo.getCategory(),vVo.getCount()));
									tileMap.put(vVo.getScale(), ltvo);
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (JsonProcessingException e) {
									e.printStackTrace();
								}
	    	                    
	    	                }
	    	            }
                    );
            
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getCurrentMonthVisitsPipeline(String userName) throws ParseException {
		
		Document matchDoc = new Document();
		
		LocalDate d = LocalDate.now();
		LocalDate fd = d.withDayOfMonth(1);
		LocalDate ld = d.withDayOfMonth(d.lengthOfMonth());
		
		matchDoc.append("schduledOn", new Document()
							.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fd.format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000"))
							.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(ld.format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000"))
						);
		matchDoc.append("userId", userName);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
				new Document()
                .append("$group", new Document()
                        .append("_id", new Document()
                                .append("scale", "$scale")
                                .append("category", "$category")
	                        )
	                        .append("count", new Document()
	                                .append("$sum", 1.0)
	                        )
                		), 
		        new Document()
		                .append("$project", new Document()
		                        .append("_id", false)
		                        .append("scale", "$_id.scale")
		                        .append("category", "$_id.category")
		                        .append("count", "$count")
		                ), 
		        new Document()
		                .append("$sort", new Document()
		                        .append("scale", 1.0)
		                )
				);
		return pipeline;
	}
	
	public VisitDetails getVisitDetailsForOneIndustryOneVisit(long industryId,long visitId) {
		VisitDetails vd = new VisitDetails();
		try {
			Query query = new Query();
			
			query.addCriteria(Criteria.where("industryId").is(industryId));
			
			List<Visits> industryVisitsList= mongoTemplate.find(query, Visits.class);
			
			vd.setVisit(industryVisitsList.stream().filter(v -> v.getVisitId()==visitId).findFirst().get());
			
			vd.setPastVisits(industryVisitsList.stream().filter(v -> v.getVisitStatus().equalsIgnoreCase(VISITED)).count());
			
			Map<String, List<TileVo>> visitStatusDaysMap = new LinkedHashMap<String, List<TileVo>>();
			
			visitStatusDaysMap.put(REPORT_FILED, getVisitStats(vd, industryVisitsList, REPORT_FILED));
			
			visitStatusDaysMap.put("sampleSubmitted", null);
			
			visitStatusDaysMap.put("sampleAnalyzed", null);
			
			visitStatusDaysMap.put("reviewCompleted", null);
			
			visitStatusDaysMap.put(LEGAL_ACTION, getVisitStats(vd, industryVisitsList, LEGAL_ACTION));
			
			vd.setVisitSteps(visitStatusDaysMap);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return vd;
	}

	private List<TileVo> getVisitStats(VisitDetails vd, List<Visits> industryVisitsList,String type) {
		List<TileVo> ltvo = new ArrayList<TileVo>();
		
		if(REPORT_FILED.equalsIgnoreCase(type)) {
			ltvo.add(new TileVo("actual",(int)ChronoUnit.DAYS.between(vd.getVisit().getVisitedDate(),vd.getVisit().getReportCreatedOn())));
			ltvo.add(new TileVo("defined",2));
			ltvo.add(new TileVo("average",(int)industryVisitsList.stream()
					.filter(v -> v.getVisitStatus().equalsIgnoreCase(VISITED))
					.mapToLong(v -> ChronoUnit.DAYS.between(v.getVisitedDate(),v.getReportCreatedOn())).average().getAsDouble()));
		}
		if(LEGAL_ACTION.equalsIgnoreCase(type)) {
			ltvo.add(new TileVo("actual",(int)ChronoUnit.DAYS.between(vd.getVisit().getVisitedDate(),vd.getVisit().getLegalDirectionIssuedOn())));
			ltvo.add(new TileVo("defined",2));
			ltvo.add(new TileVo("average",(int)industryVisitsList.stream()
					.filter(v -> v.getVisitStatus().equalsIgnoreCase(VISITED))
					.mapToLong(v -> ChronoUnit.DAYS.between(v.getVisitedDate(),v.getLegalDirectionIssuedOn())).average().getAsDouble()));
		}
		return ltvo;
	}
	
	public List<Visits> getVisitDetailsForOneIndustry(long industryId,String fromDate,String toDate) {
		try {
			Query query = new Query();
			
			query.addCriteria(Criteria.where("industryId").is(industryId));
			
			query.addCriteria(Criteria.where("visitStatus").is(VISITED));
			
			if(StringUtils.hasText(fromDate) && StringUtils.hasText(fromDate)) {
				query.addCriteria(Criteria.where("visitedDate")
						.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDate+" 00:00:00.000+0000"))
						.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDate+" 00:00:00.000+0000")));
			}
			
			List<Visits> industryVisitsList= mongoTemplate.find(query, Visits.class);
			
			return industryVisitsList;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String,List<VisitsByComplianceVo>> getVisitsByCompliance(String region, String subRegion){
		Map<String,List<VisitsByComplianceVo>> mapVisitsByCompliance = new LinkedHashMap<String, List<VisitsByComplianceVo>>();
		
		try {
			logger.info("getVisitsByCompliance");
			Map<String, String> daysMap = IDSSUtil.get3090120PastDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("C_score_visits_days");
            
            for(String days : daysMap.keySet()) {
            	List<VisitsByComplianceVo> vbcList = new ArrayList<VisitsByComplianceVo>();
            	logger.info("getVisitsByCompliance : "+days);
	            List<? extends Bson> pipeline = getVisitsByCompliancePipeline(days,region, subRegion);
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										VisitsByComplianceVo vbc = new ObjectMapper().readValue(document.toJson(), VisitsByComplianceVo.class);
										vbcList.add(vbc);
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (JsonProcessingException e) {
										e.printStackTrace();
									}
		    	                    
		    	                }
		    	            }
	                    );
	            mapVisitsByCompliance.put(daysMap.get(days), vbcList);
            }
            return mapVisitsByCompliance;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getVisitsByCompliancePipeline(String days,String region, String subRegion) throws ParseException {
		Document matchDoc = new Document().append("lastVisit", new Document()
				.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
		
		if(!"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		if(!"ALL".equalsIgnoreCase(subRegion))
			matchDoc.append("subRegion",subRegion);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("expected", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$lt", Arrays.asList(
                                                                "$expectedFreqDays",
                                                                0.0
                                                            )
                                                        )
                                                )
                                                .append("then", 0.0)
                                                .append("else", "$expectedFreqDays")
                                        )
                                )
                                .append("actual", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$lt", Arrays.asList(
                                                                "$actualFreqDays",
                                                                0.0
                                                            )
                                                        )
                                                )
                                                .append("then", 0.0)
                                                .append("else", "$actualFreqDays")
                                        )
                                )
                                .append("score", new Document()
                                        .append("$concat", Arrays.asList(
                                                new Document()
                                                        .append("$cond", Arrays.asList(
                                                                new Document()
                                                                        .append("$and", Arrays.asList(
                                                                                new Document()
                                                                                        .append("$gte", Arrays.asList(
                                                                                                "$cscore",
                                                                                                0.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                "$cscore",
                                                                                                26.0
                                                                                            )
                                                                                        )
                                                                            )
                                                                        ),
                                                                "0-25",
                                                                ""
                                                            )
                                                        ),
                                                new Document()
                                                        .append("$cond", Arrays.asList(
                                                                new Document()
                                                                        .append("$and", Arrays.asList(
                                                                                new Document()
                                                                                        .append("$gte", Arrays.asList(
                                                                                                "$cscore",
                                                                                                26.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                "$cscore",
                                                                                                51.0
                                                                                            )
                                                                                        )
                                                                            )
                                                                        ),
                                                                "26-50",
                                                                ""
                                                            )
                                                        ),
                                                new Document()
                                                        .append("$cond", Arrays.asList(
                                                                new Document()
                                                                        .append("$and", Arrays.asList(
                                                                                new Document()
                                                                                        .append("$gte", Arrays.asList(
                                                                                                "$cscore",
                                                                                                51.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                "$cscore",
                                                                                                76.0
                                                                                            )
                                                                                        )
                                                                            )
                                                                        ),
                                                                "51-75",
                                                                ""
                                                            )
                                                        ),
                                                new Document()
                                                        .append("$cond", Arrays.asList(
                                                                new Document()
                                                                        .append("$and", Arrays.asList(
                                                                                new Document()
                                                                                        .append("$gte", Arrays.asList(
                                                                                                "$cscore",
                                                                                                76.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                "$cscore",
                                                                                                101.0
                                                                                            )
                                                                                        )
                                                                            )
                                                                        ),
                                                                "76-100",
                                                                ""
                                                            )
                                                        )
                                            )
                                        )
                                )
                        ), 
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$score")
                                .append("expected", new Document()
                                        .append("$avg", "$expected")
                                )
                                .append("actual", new Document()
                                        .append("$avg", "$actual")
                                )
                        ), 
                new Document()
                .append("$project", new Document()
                        .append("_id", false)
                        .append("cScore", "$_id")
                        .append("scheduledFreq", new Document()
                                .append("$ceil", "$expected")
                        )
                        .append("actualFreq", new Document()
                                .append("$ceil", "$actual")
                        )
                )
        );
		
		
		return pipeline;
	}

	public VisitProcessEfficiency getVisitProcessEfficiency(String region) {
		try {
			Query query = new Query();
			
			if(!"ALL".equalsIgnoreCase(region))
				query.addCriteria(Criteria.where("region").is(region));
			
			List<VisitProcessEfficiency> visitProcessEfficiencyList= mongoTemplate.find(query, VisitProcessEfficiency.class);
			
			if(!"ALL".equalsIgnoreCase(region))
				return visitProcessEfficiencyList.get(0);
			else {
				
				VisitProcessEfficiency vpe = new VisitProcessEfficiency();
				vpe.setAvg_legal_action(visitProcessEfficiencyList.stream().mapToDouble(v -> v.getAvg_legal_action()).average().getAsDouble());
				vpe.setAvg_report_filed(visitProcessEfficiencyList.stream().mapToDouble(v -> v.getAvg_report_filed()).average().getAsDouble());
				vpe.setAvg_review_comp((int)visitProcessEfficiencyList.stream().mapToInt(v -> v.getAvg_review_comp()).average().getAsDouble());
				vpe.setAvg_samples_analysed((int)visitProcessEfficiencyList.stream().mapToInt(v -> v.getAvg_samples_analysed()).average().getAsDouble());
				vpe.setAvg_samples_submitted((int)visitProcessEfficiencyList.stream().mapToDouble(v -> v.getAvg_samples_submitted()).average().getAsDouble());
				
				vpe.setDefined_legal_action((int)visitProcessEfficiencyList.stream().mapToDouble(v -> v.getDefined_legal_action()).average().getAsDouble());
				vpe.setDefined_report_filed((int)visitProcessEfficiencyList.stream().mapToDouble(v -> v.getDefined_report_filed()).average().getAsDouble());
				vpe.setDefined_review_comp((int)visitProcessEfficiencyList.stream().mapToDouble(v -> v.getDefined_review_comp()).average().getAsDouble());
				vpe.setDefined_samples_analysed((int)visitProcessEfficiencyList.stream().mapToDouble(v -> v.getDefined_samples_analysed()).average().getAsDouble());
				vpe.setDefined_samples_submitted((int)visitProcessEfficiencyList.stream().mapToDouble(v -> v.getDefined_samples_submitted()).average().getAsDouble());
				return vpe;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<VisitScheduleCurrentMonthResponseVo> getVisitsScheduleByUserName(String userName) {
		try {
			List<VisitScheduleCurrentMonthResponseVo> visitScheduleList = new ArrayList<VisitScheduleCurrentMonthResponseVo>();
			LocalDate d = LocalDate.now();
			LocalDate fd = d.withDayOfMonth(1);
			LocalDate ld = d.withDayOfMonth(d.lengthOfMonth());
			Query query = new Query();

			query.addCriteria(Criteria.where("userId").is(userName));


			query.addCriteria(Criteria.where("schduledOn")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fd+" 00:00:00.000+0000"))
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(ld+" 00:00:00.000+0000")));


			List<Visits> visitScheduleCurrentMonthResponseVoList= mongoTemplate.find(query, Visits.class);
			for(Visits vsd : visitScheduleCurrentMonthResponseVoList) {
				VisitScheduleCurrentMonthResponseVo visitScheduleVo = new VisitScheduleCurrentMonthResponseVo();
				visitScheduleVo.setIndustryName(vsd.getIndustryName());
				visitScheduleVo.setScale(vsd.getScale());
				visitScheduleVo.setType(vsd.getType());
				visitScheduleVo.setCategory(vsd.getCategory());
				visitScheduleVo.setScheduledOn(vsd.getSchduledOn());

				visitScheduleList.add(visitScheduleVo);
			}
			return visitScheduleList;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
