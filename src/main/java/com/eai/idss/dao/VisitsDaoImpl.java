package com.eai.idss.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eai.idss.model.CScoreMaster;
import com.eai.idss.model.DecisionMaking;
import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.User;
import com.eai.idss.model.VisitProcessEfficiency;
import com.eai.idss.model.Visits;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.ConcentByRegionVo;
import com.eai.idss.vo.DecisionMakingParamVo;
import com.eai.idss.vo.DecisionMakingVo;
import com.eai.idss.vo.TileVo;
import com.eai.idss.vo.VisitDetails;
import com.eai.idss.vo.VisitScheduleCurrentMonthResponseVo;
import com.eai.idss.vo.VisitsByComplianceVo;
import com.eai.idss.vo.VisitsByScaleCategory;
import com.eai.idss.vo.VisitsDetailsRequest;
import com.eai.idss.vo.VisitsFilter;
import com.eai.idss.vo.VisitsPaginationResponseVo;
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
	
	@Value("${dbName}")
	private String dbName;
	
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
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
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
		
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		Document matchDoc = new Document();
		
		matchDoc.append("schduledOn", new Document()
							.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateToday+" 00:00:00.000+0000"))
							.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
						);
		matchDoc.append("visitStatus", PENDING);

		if(StringUtils.hasText(region) && !"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		if(StringUtils.hasText(subRegion) && !"ALL".equalsIgnoreCase(subRegion))
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
			logger.info("getByRegionVisitData");
			Map<String, List<String>> daysMap = IDSSUtil.getPastAndFutureDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
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
			for(String days : daysMap.keySet()) {
				logger.info("getByRegionVisitsData-Upcomming : "+days);

				Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();

				List<? extends Bson> pipeline = getRegionVisitsPipeline(PENDING,daysMap.get(days).get(1),cf);

				extractData(collection, regionVisitMap, pipeline,PENDING,REGION_WISE);

				pipeline = getRegionVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf);

				extractData(collection, regionVisitMap, pipeline,SCHEDULED,REGION_WISE);

				pipeline = getRegionVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(1),cf);

				extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,REGION_WISE);

				pipeline = getRegionVisitsPipeline(COMPLETED,daysMap.get(days).get(1),cf);

				extractData(collection, regionVisitMap, pipeline,COMPLETED,REGION_WISE);

				byRegionMap.put("upcoming"+days,regionVisitMap);

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
									List<TileVo> concentStatusList = regionVisitMap.get(type);
									if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
									concentStatusList.add(tVo);
									regionVisitMap.put(type, concentStatusList);
								}else if(TEAM_WISE.equalsIgnoreCase(extractType)) {
									VisitsTeamVo crVo = new ObjectMapper().readValue(document.toJson(), VisitsTeamVo.class);
									TileVo tVo = new TileVo(type,crVo.getCount());
//									List<TileVo> concentStatusList = regionVisitMap.get(crVo.getName()+"~"+crVo.getDesignation()+"~"+crVo.getUserId());
									List<TileVo> concentStatusList = regionVisitMap.get(crVo.getUserId());
									if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
									concentStatusList.add(tVo);
									regionVisitMap.put(crVo.getName()+"~"+crVo.getDesignation()+"~"+crVo.getUserId(), concentStatusList);
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
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
					.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateToday+" 00:00:00.000+0000"))
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
			matchDoc.append("schduledOn", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("visitStatus",VISITED);
		}
		
		if(VISITED.equalsIgnoreCase(caseType)) {
			matchDoc.append("schduledOn", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("visitStatus",VISITED);
		}
		
		if(LEGAL_NOTICES.equalsIgnoreCase(caseType)) {
			matchDoc.append("schduledOn", new Document()
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
	
	
	public  Map<String,Map<String,List<TileVo>>> getBySubRegionVisitsData(List<String> subRegion, VisitsFilter cf){
		try {
			logger.info("getBySubRegionVisitData");
			Map<String, List<String>> daysMap = IDSSUtil.getPastAndFutureDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,Map<String,List<TileVo>>> byRegionMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getBySubRegionVisitsData : "+days);
            	Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();
            	
            	List<? extends Bson> pipeline = getSubRegionVisitsPipeline(PENDING,daysMap.get(days).get(0),cf,subRegion);
	            
	            extractData(collection, regionVisitMap, pipeline,PENDING,SUB_REGION_WISE);
            
	            pipeline = getSubRegionVisitsPipeline(SCHEDULED,daysMap.get(days).get(0),cf,subRegion);
	            
	            extractData(collection, regionVisitMap, pipeline,SCHEDULED,SUB_REGION_WISE);
	            
	            pipeline = getSubRegionVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(0),cf,subRegion);
	            
	            extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,SUB_REGION_WISE);

	            pipeline = getSubRegionVisitsPipeline(COMPLETED,daysMap.get(days).get(0),cf,subRegion);
	            
	            extractData(collection, regionVisitMap, pipeline,COMPLETED,SUB_REGION_WISE);
	            
	            byRegionMap.put(days,regionVisitMap);
            
            }
			for(String days : daysMap.keySet()) {
				logger.info("getBySubRegionVisitsData-upcomming : "+days);
				Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();

				List<? extends Bson> pipeline = getSubRegionVisitsPipeline(PENDING,daysMap.get(days).get(1),cf,subRegion);

				extractData(collection, regionVisitMap, pipeline,PENDING,SUB_REGION_WISE);

				pipeline = getSubRegionVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,subRegion);

				extractData(collection, regionVisitMap, pipeline,SCHEDULED,SUB_REGION_WISE);

				pipeline = getSubRegionVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(1),cf,subRegion);

				extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,SUB_REGION_WISE);

				pipeline = getSubRegionVisitsPipeline(COMPLETED,daysMap.get(days).get(1),cf,subRegion);

				extractData(collection, regionVisitMap, pipeline,COMPLETED,SUB_REGION_WISE);

				byRegionMap.put("upcoming"+days,regionVisitMap);

			}
            return byRegionMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getSubRegionVisitsPipeline(String caseType,String days,VisitsFilter cf,List<String> subRegion) throws ParseException {
		
		Document matchDoc = new Document();
		
		applyMatchFilter(caseType, days, matchDoc);

		matchDoc.append("subRegion", new Document().append("$in", subRegion));
		
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
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
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
			for(String days : daysMap.keySet()) {
				logger.info("getByTeamVisitsData-Upcomming : "+days);
				Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();
				if("RO".equalsIgnoreCase(u.getDesignation())) {
					getSROFOTeamDetailsByUpcomming(cf, u, daysMap, collection, days, regionVisitMap);
				}else {
					getROTeamDetailsByUpcomming(cf, u, daysMap, collection, days, regionVisitMap);
				}
				byRegionMap.put("upcoming"+days,regionVisitMap);

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
	private void getSROFOTeamDetailsByUpcomming(VisitsFilter cf, User u, Map<String, List<String>> daysMap,
									 MongoCollection<Document> collection, String days, Map<String, List<TileVo>> regionVisitMap)
			throws ParseException {
		List<? extends Bson> pipeline;
		pipeline = getByTeamVisitsPipeline(PENDING,daysMap.get(days).get(1),cf,u,u.getDesignation());

		extractData(collection, regionVisitMap, pipeline,PENDING,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,u,u.getDesignation());

		extractData(collection, regionVisitMap, pipeline,SCHEDULED,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(1),cf,u,u.getDesignation());

		extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(VISITED,daysMap.get(days).get(1),cf,u,u.getDesignation());

		extractData(collection, regionVisitMap, pipeline,VISITED,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(REPORTS,daysMap.get(days).get(1),cf,u,u.getDesignation());

		extractData(collection, regionVisitMap, pipeline,REPORTS,TEAM_WISE);
	}

	private void getROTeamDetailsByUpcomming(VisitsFilter cf, User u, Map<String, List<String>> daysMap,
								  MongoCollection<Document> collection, String days, Map<String, List<TileVo>> regionVisitMap)
			throws ParseException {
		List<? extends Bson> pipeline;
		pipeline = getByTeamVisitsPipeline(PENDING,daysMap.get(days).get(1),cf,u,"RO~SRO");

		extractData(collection, regionVisitMap, pipeline,PENDING,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(PENDING,daysMap.get(days).get(1),cf,u,"RO~FO");
		extractData(collection, regionVisitMap, pipeline,PENDING,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,u,"RO~SRO");

		extractData(collection, regionVisitMap, pipeline,SCHEDULED,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,u,"RO~FO");
		extractData(collection, regionVisitMap, pipeline,SCHEDULED,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(1),cf,u,"RO~SRO");

		extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(1),cf,u,"RO~FO");
		extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(VISITED,daysMap.get(days).get(1),cf,u,"RO~SRO");

		extractData(collection, regionVisitMap, pipeline,VISITED,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(VISITED,daysMap.get(days).get(1),cf,u,"RO~FO");
		extractData(collection, regionVisitMap, pipeline,VISITED,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(REPORTS,daysMap.get(days).get(1),cf,u,"RO~SRO");

		extractData(collection, regionVisitMap, pipeline,REPORTS,TEAM_WISE);

		pipeline = getByTeamVisitsPipeline(REPORTS,daysMap.get(days).get(1),cf,u,"RO~FO");
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
                            .append("name", "$adminName")
                            .append("designation", "FO")
                            .append("userId", "$userId")
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
                            .append("userId", "$userId")
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
                            .append("userId", "$userId")
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
                            .append("userId", "$userId")
                    )
                    .append("count", new Document()
                            .append("$sum", 1.0)
                    );
		}
		
		if(null!=cf && null!=cf.getPendingByTeamCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getPendingByTeamCategoryList()));

		if(null!=cf && null!=cf.getPendingByTeamScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getPendingByTeamScaleList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
				new Document().append("$group", groupDoc), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("name", "$_id.name")
                                .append("designation", "$_id.designation")
                                .append("userId", "$_id.userId")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("name", 1.0)
                        )
        );
		return pipeline;
	}
	
	public VisitsPaginationResponseVo getVisitsPaginatedRecords(VisitsDetailsRequest cdr, Pageable page){
		try {
			Query query = new Query().with(page);
			getQueryCriteria(cdr, query);
			
			Query queryCnt = new Query();
			getQueryCriteria(cdr, queryCnt);
	
			VisitsPaginationResponseVo vprv = new VisitsPaginationResponseVo();
			vprv.setTotalRecords(mongoTemplate.count(queryCnt, Visits.class));
			
			List<Visits> filteredVisitsList= mongoTemplate.find(query, Visits.class);
			
			filteredVisitsList.stream().forEach(v -> v.setElapsedDays(ChronoUnit.DAYS.between(LocalDate.now(),v.getSchduledOn())));
			
			Page<Visits> cPage = PageableExecutionUtils.getPage(
					filteredVisitsList,
					page,
			        () -> mongoTemplate.count(query, Visits.class));
			
			vprv.setVisitsList(cPage.toList());
			
			return vprv;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new VisitsPaginationResponseVo(new ArrayList<Visits>(),0);
	}

	private void getQueryCriteria(VisitsDetailsRequest cdr, Query query) throws ParseException {
		if(null!=cdr) {
			List<String> days = new ArrayList<String>(); 
			LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
			
			if(StringUtils.hasText(cdr.getDuration())) {
				if(cdr.getDuration().contains("+")) {
					int futureDays = Integer.parseInt(cdr.getDuration().replace("+", ""));
					days.add(currentTime.plusDays(futureDays).format(DateTimeFormatter.ISO_LOCAL_DATE));
					days.add(currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
				}
				else if(cdr.getDuration().contains("-")) {
					days.add(currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
					int pastDaysCnt = Integer.parseInt(cdr.getDuration().replace("-", ""));
					days.add(currentTime.minusDays(pastDaysCnt).format(DateTimeFormatter.ISO_LOCAL_DATE));
				}else if(cdr.getDuration().contains("_")) {
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
			if(StringUtils.hasText(cdr.getUserId())) {
				Criteria orCriteria = new Criteria();
				orCriteria.orOperator(Criteria.where("userId").is(cdr.getUserId()),Criteria.where("reportingToUserId").is(cdr.getUserId()));
				
				query.addCriteria(orCriteria);
			}
		}
	}
	
	private void addCriteriaFilter(String caseType, List<String> days, Query query) throws ParseException {
		
		
		if(SCHEDULED.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("schduledOn")
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000")));
		}
		if(PENDING.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("schduledOn")
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("visitStatus").is(PENDING));
		}
		
		if(COMPLETED.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("schduledOn")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("visitStatus").is(VISITED));
		}
		
		if(LEGAL_NOTICES.equalsIgnoreCase(caseType)) {
			
			query.addCriteria(Criteria.where("schduledOn")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("legalDirection").ne(NA));
		}
	}
	
	public VisitsPaginationResponseVo getVisitsSchedulePaginatedRecords(VisitsScheduleDetailsRequest cdr, Pageable pageable,String userName){
		try {
			Query query = new Query().with(pageable);
			getQueryCriteria(cdr, userName, query);
			
			Query queryCnt = new Query();
			getQueryCriteria(cdr, userName, queryCnt);
			
			VisitsPaginationResponseVo vprv = new VisitsPaginationResponseVo();
	
//			vprv.setTotalRecords(mongoTemplate.count(queryCnt, Visits.class));
			List<Visits> filteredVisitListTotalCount= mongoTemplate.find(queryCnt, Visits.class);
			applyCScoreFilterTotalCount(cdr,filteredVisitListTotalCount);
			vprv.setTotalRecords(filteredVisitListTotalCount.size());
			List<Visits> filteredVisitList= mongoTemplate.find(query, Visits.class);
			
			applyCScoreFilter(cdr, filteredVisitList);
			
			Page<Visits> cPage = PageableExecutionUtils.getPage(
					filteredVisitList,
					pageable,
			        () -> mongoTemplate.count(query, Visits.class));
			
			List<Visits> finalVisitList = cPage.toList();


			populateCScore(finalVisitList);
			
			vprv.setVisitsList(finalVisitList);
			
			return vprv;
		}	
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void getQueryCriteria(VisitsScheduleDetailsRequest cdr, String userName, Query query)
			throws ParseException {
		query.addCriteria(Criteria.where("schduledOn")
				.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(cdr.getFromDate()+" 00:00:00.000+0000"))
				.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(cdr.getToDate()+" 00:00:00.000+0000")));
			
		if(StringUtils.hasText(cdr.getStatus()) && !"ALL".equalsIgnoreCase(cdr.getStatus())) {
			query.addCriteria(Criteria.where("visitStatus").is(cdr.getStatus()));
		}
		
		query.addCriteria(Criteria.where("userId").is(userName));
	}

	private void populateCScore(List<Visits> finalVisitList) {
		List<Long> indIdList = finalVisitList.stream().map(Visits::getIndustryId).collect(Collectors.toList());
		Query queryIM = new Query();
		
		queryIM.addCriteria(Criteria.where("industryId").in(indIdList));
		List<IndustryMaster> imList = mongoTemplate.find(queryIM, IndustryMaster.class);
		
		for(Visits v : finalVisitList) {
			for(IndustryMaster im : imList) {
				if(im.getIndustryId() == v.getIndustryId()) {
					v.setcScore(im.getCscore());
					break;
				}
			}
		}
	}

	private void applyCScoreFilter(VisitsScheduleDetailsRequest cdr, List<Visits> filteredVisitList) {
		if(StringUtils.hasText(cdr.getCompliance()) && !"ALL".equalsIgnoreCase(cdr.getCompliance())) {
			String[] op = cdr.getCompliance().split("-");
			
			List<Long> indIdList = filteredVisitList.stream().map(Visits::getIndustryId).collect(Collectors.toList());
			Query queryIM = new Query();
			
			queryIM.addCriteria(Criteria.where("industryId").in(indIdList));
			queryIM.addCriteria(Criteria.where("cscore").gte(Integer.parseInt(op[0])).lte(Integer.parseInt(op[1])));
			
			List<IndustryMaster> imList = mongoTemplate.find(queryIM, IndustryMaster.class);
			
			List<Long> ldmIndIdList = imList.stream().map(IndustryMaster::getIndustryId).collect(Collectors.toList());
			
			filteredVisitList.removeIf(i -> !ldmIndIdList.contains(i.getIndustryId()));
		}
	}
	private void applyCScoreFilterTotalCount(VisitsScheduleDetailsRequest cdr, List<Visits> filteredVisitListTotalCount) {
		if(StringUtils.hasText(cdr.getCompliance()) && !"ALL".equalsIgnoreCase(cdr.getCompliance())) {
			String[] op = cdr.getCompliance().split("-");

			List<Long> indIdList = filteredVisitListTotalCount.stream().map(Visits::getIndustryId).collect(Collectors.toList());
			Query queryIM = new Query();

			queryIM.addCriteria(Criteria.where("industryId").in(indIdList));
			queryIM.addCriteria(Criteria.where("cscore").gte(Integer.parseInt(op[0])).lte(Integer.parseInt(op[1])));

			List<IndustryMaster> imList = mongoTemplate.find(queryIM, IndustryMaster.class);

			List<Long> ldmIndIdList = imList.stream().map(IndustryMaster::getIndustryId).collect(Collectors.toList());

			filteredVisitListTotalCount.removeIf(i -> !ldmIndIdList.contains(i.getIndustryId()));
		}
	}
	
	public Map<String,List<TileVo>> getVisitsScheduleByScaleCategory(String userName){
		try {
			logger.info("getVisitsScheduleByScaleCategory");
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
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

			vd.getVisit().setcScore(getCscore(industryId));

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
	public double getCscore(long industryId){

		Query query = new Query();
		query.limit(1);
		query.with(Sort.by(Sort.Direction.DESC,"_id"));
		query.addCriteria(Criteria.where("industryId").is(industryId));
		List<CScoreMaster> cScoreMasters = mongoTemplate.find(query, CScoreMaster.class);
		if(null==cScoreMasters || cScoreMasters.isEmpty()) return 0;
		return cScoreMasters.get(0).getCscore();
	}

	private List<TileVo> getVisitStats(VisitDetails vd, List<Visits> industryVisitsList,String type) {
		List<TileVo> ltvo = new ArrayList<TileVo>();
		
		if(REPORT_FILED.equalsIgnoreCase(type)) {
			if(null!=vd.getVisit().getVisitedDate() && null!=vd.getVisit().getReportCreatedOn()) {
				ltvo.add(new TileVo("actual",(int)ChronoUnit.DAYS.between(vd.getVisit().getVisitedDate(),vd.getVisit().getReportCreatedOn())));
				ltvo.add(new TileVo("defined",2));
				ltvo.add(new TileVo("average",(int)industryVisitsList.stream()
						.filter(v -> v.getVisitStatus().equalsIgnoreCase(VISITED))
						.mapToLong(v -> ChronoUnit.DAYS.between(v.getVisitedDate(),v.getReportCreatedOn())).average().getAsDouble()));
			}
			else {
				ltvo.add(new TileVo("actual",-1));
				ltvo.add(new TileVo("defined",2));
				ltvo.add(new TileVo("average",-1));
			}
		}
		if(LEGAL_ACTION.equalsIgnoreCase(type)) {
			if(null!=vd.getVisit().getVisitedDate() && null!=vd.getVisit().getLegalDirectionIssuedOn()) {
				ltvo.add(new TileVo("actual",(int)ChronoUnit.DAYS.between(vd.getVisit().getVisitedDate(),vd.getVisit().getLegalDirectionIssuedOn())));
				ltvo.add(new TileVo("defined",2));
				ltvo.add(new TileVo("average",(int)industryVisitsList.stream()
						.filter(v -> v.getVisitStatus().equalsIgnoreCase(VISITED))
						.mapToLong(v -> ChronoUnit.DAYS.between(v.getVisitedDate(),v.getLegalDirectionIssuedOn())).average().getAsDouble()));
			}else {
				ltvo.add(new TileVo("actual",-1));
				ltvo.add(new TileVo("defined",2));
				ltvo.add(new TileVo("average",-1));
			}
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
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
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
										if("0-25".equalsIgnoreCase(vbc.getcScore()))
												vbc.setScheduledFreq("30");
										if("26-50".equalsIgnoreCase(vbc.getcScore()))
											vbc.setScheduledFreq("60");
										if("51-75".equalsIgnoreCase(vbc.getcScore()))
											vbc.setScheduledFreq("90");
										if("76-100".equalsIgnoreCase(vbc.getcScore()))
											vbc.setScheduledFreq("120");
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
                                .append("cscore", new Document()
                                        .append("$concat", Arrays.asList(
                                                new Document()
                                                        .append("$cond", Arrays.asList(
                                                                new Document()
                                                                        .append("$and", Arrays.asList(
                                                                                new Document()
                                                                                        .append("$gte", Arrays.asList(
                                                                                        		new Document().append("$ceil","$cscore"),
                                                                                                0.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                new Document().append("$ceil","$cscore"),
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
                                                                                                new Document().append("$ceil","$cscore"),
                                                                                                26.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                new Document().append("$ceil","$cscore"),
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
                                                                                                new Document().append("$ceil","$cscore"),
                                                                                                51.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                new Document().append("$ceil","$cscore"),
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
                                                                                                new Document().append("$ceil","$cscore"),
                                                                                                76.0
                                                                                            )
                                                                                        ),
                                                                                new Document()
                                                                                        .append("$lt", Arrays.asList(
                                                                                                new Document().append("$ceil","$cscore"),
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
                                .append("_id", "$cscore")
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
                ),
                new Document()
                .append("$sort", new Document()
                        .append("cScore", 1.0)
                )
        );
		
		
		return pipeline;
	}

	public VisitProcessEfficiency getVisitProcessEfficiency(String region,String duration) {
		try {
			Query query = new Query();
			
			if(!"ALL".equalsIgnoreCase(region))
				query.addCriteria(Criteria.where("region").is(region));
			
			query.addCriteria(Criteria.where("time").is(duration));
			
			List<VisitProcessEfficiency> visitProcessEfficiencyList= mongoTemplate.find(query, VisitProcessEfficiency.class);
			
			if(!"ALL".equalsIgnoreCase(region))
				return visitProcessEfficiencyList.get(0);
			else {
				
				VisitProcessEfficiency vpe = new VisitProcessEfficiency();
				vpe.setRegion("All");
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
				visitScheduleVo.setLongitude(vsd.getLongitude());
				visitScheduleVo.setLatitude(vsd.getLatitude());
				visitScheduleVo.setPriority(vsd.getPriority());

				visitScheduleList.add(visitScheduleVo);
			}
			return visitScheduleList;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DecisionMakingVo getVisitDetailsDecisionMaking(long industryId,long visitId) {
		DecisionMakingVo dmv = new DecisionMakingVo();
		
		List<DecisionMakingParamVo> dmvList = new ArrayList<DecisionMakingParamVo>();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		query.addCriteria(Criteria.where("visitId").lte(visitId));
		query.with(Sort.by(Sort.Direction.DESC,"visitId"));
		query.limit(5);
					
		List<DecisionMaking> dmList= mongoTemplate.find(query, DecisionMaking.class);
		
		if(dmList.size()>1)
			dmv.setDate1(dmList.get(1).getSchduledOn().format(DateTimeFormatter.ISO_LOCAL_DATE));
		if(dmList.size()>2)
			dmv.setDate2(dmList.get(2).getSchduledOn().format(DateTimeFormatter.ISO_LOCAL_DATE));
		if(dmList.size()>3)
			dmv.setDate3(dmList.get(3).getSchduledOn().format(DateTimeFormatter.ISO_LOCAL_DATE));
		if(dmList.size()>4)
			dmv.setDate4(dmList.get(4).getSchduledOn().format(DateTimeFormatter.ISO_LOCAL_DATE));
		
		dmvList.add(getDisposalDomasticDataVo(dmList));
		dmvList.add(getDisposalIndustrialDataVo(dmList));
		dmvList.add(getBankGurrentyImposedDataVo(dmList));
		dmvList.add(getLegalComplaintsDataVo(dmList));
		dmvList.add(getOMSAirDataVo(dmList));
		dmvList.add(getOMSWaterPropPlacedDataVo(dmList));
		dmvList.add(getOMSWaterInstalledDataVo(dmList));
		dmvList.add(getOMSStackPropPlacedDataVo(dmList));
		dmvList.add(getOMSStackInstallDataVo(dmList));
		dmvList.add(getStackExistDataVo(dmList));
		dmvList.add(getSTPOpDataVo(dmList));
		dmvList.add(getETPOpDataVo(dmList));
		
		int violationCnt = 0;
		for(DecisionMakingParamVo dmVo : dmvList) {
			if(!"-".equals(dmVo.getRequiredValue()) && !dmVo.getCurrentValue().equalsIgnoreCase(dmVo.getRequiredValue()))
				violationCnt++;
		}
		
		DecisionMakingParamVo greenTreeVo = getPercGreenTreesPlantedDataVo(dmList);
		dmvList.add(greenTreeVo);
		
		if(new BigDecimal(greenTreeVo.getCurrentValue()).compareTo(new BigDecimal(greenTreeVo.getRequiredValue()))==-1)
			violationCnt++;
		
		int violationPer = (violationCnt * 100)/13; 
		if(violationPer>=0 && violationPer<=25) {
			dmv.setAction("No Actions");
			dmv.setSuggestion("No Actions");
		}else if(violationPer>=26 && violationPer<=50) {
			dmv.setAction("Show Cause Notice");
			dmv.setSuggestion("Show Cause Notice");
		}else if(violationPer>=51 && violationPer<=75) {
			dmv.setAction("Suggestive Action");
			dmv.setSuggestion("Suggestive Action");
		}else if(violationPer>=76 && violationPer<=100) {
			dmv.setAction("Closure Notice");
			dmv.setSuggestion("Closure Notice");
		}
					
		dmv.setDmpList(dmvList);
		return dmv;
	}
	
	private DecisionMakingParamVo getPercGreenTreesPlantedDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Percentage Green Trees Planted");
		dmv1.setRequiredValue(String.valueOf(dmList.get(0).getRequiredPercentPlantation()==-999999?"-":dmList.get(0).getRequiredPercentPlantation()));
		dmv1.setCurrentValue(String.valueOf(BigDecimal.valueOf(dmList.get(0).getPercentPlantation()).setScale(2, RoundingMode.HALF_UP)));
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(String.valueOf(BigDecimal.valueOf(dmList.get(1).getPercentPlantation()).setScale(2, RoundingMode.HALF_UP)));
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(String.valueOf(BigDecimal.valueOf(dmList.get(2).getPercentPlantation()).setScale(2, RoundingMode.HALF_UP)));
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(String.valueOf(BigDecimal.valueOf(dmList.get(3).getPercentPlantation()).setScale(2, RoundingMode.HALF_UP)));
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(String.valueOf(BigDecimal.valueOf(dmList.get(4).getPercentPlantation()).setScale(2, RoundingMode.HALF_UP)));
		return dmv1;
	}
	
	private DecisionMakingParamVo getETPOpDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("ETP Operational");
		dmv1.setRequiredValue(dmList.get(0).getRequiredEtpOperational()==1?"Yes":dmList.get(0).getRequiredEtpOperational()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getEtpOperational()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getEtpOperational()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getEtpOperational()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getEtpOperational()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getEtpOperational()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getSTPOpDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("STP Operational");
		dmv1.setRequiredValue(dmList.get(0).getRequiredStpOperational()==1?"Yes":dmList.get(0).getRequiredStpOperational()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getStpOperational()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getStpOperational()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getStpOperational()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getStpOperational()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getStpOperational()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getStackExistDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Stack Exists");
		dmv1.setRequiredValue(dmList.get(0).getRequiredStackFacilityExist()==1?"Yes":dmList.get(0).getRequiredStackFacilityExist()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getStackFacilityExist()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getStackFacilityExist()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getStackFacilityExist()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getStackFacilityExist()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getStackFacilityExist()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getOMSStackInstallDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Online Monitoring System Stack Installed");
		dmv1.setRequiredValue(dmList.get(0).getRequiredOmsStackInstalled()==1?"Yes":dmList.get(0).getRequiredOmsStackInstalled()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getOmsStackInstalled()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getOmsStackInstalled()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getOmsStackInstalled()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getOmsStackInstalled()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getOmsStackInstalled()==1?"Yes":"No");
		return dmv1;
	}


	private DecisionMakingParamVo getOMSStackPropPlacedDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Online Monitoring System Stack Properly Placed");
		dmv1.setRequiredValue(dmList.get(0).getRequiredOmsStackProperlyPlaced()==1?"Yes":dmList.get(0).getRequiredOmsStackProperlyPlaced()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getOmsStackProperlyPlaced()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getOmsStackProperlyPlaced()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getOmsStackProperlyPlaced()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getOmsStackProperlyPlaced()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getOmsStackProperlyPlaced()==1?"Yes":"No");
		return dmv1;
	}

	
	private DecisionMakingParamVo getOMSWaterInstalledDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Online Monitoring System Water Installed");
		dmv1.setRequiredValue(dmList.get(0).getRequiredOmswInstalled()==1?"Yes":dmList.get(0).getRequiredOmswInstalled()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getOmswInstalled()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getOmswInstalled()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getOmswInstalled()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getOmswInstalled()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getOmswInstalled()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getOMSWaterPropPlacedDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Online Monitoring System Water Properly Placed");
		dmv1.setRequiredValue(dmList.get(0).getRequiredOmswPlacedProperly()==1?"Yes":dmList.get(0).getRequiredOmswPlacedProperly()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getOmswPlacedProperly()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getOmswPlacedProperly()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getOmswPlacedProperly()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getOmswPlacedProperly()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getOmswPlacedProperly()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getOMSAirDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Online Monitoring System Ambient Air Installed");
		dmv1.setRequiredValue(dmList.get(0).getRequiredOmsAmbientInstalled()==1?"Yes":dmList.get(0).getRequiredOmsAmbientInstalled()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getOmsAmbientInstalled()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getOmsAmbientInstalled()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getOmsAmbientInstalled()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getOmsAmbientInstalled()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getOmsAmbientInstalled()==1?"Yes":"No");
		return dmv1;
	}

	private DecisionMakingParamVo getLegalComplaintsDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Legal Complaints");
		dmv1.setRequiredValue(dmList.get(0).getRequiredLegalAnyComplaint()==1?"Yes":dmList.get(0).getRequiredLegalAnyComplaint()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getLegalAnyComplaint()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getLegalAnyComplaint()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getLegalAnyComplaint()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getLegalAnyComplaint()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getLegalAnyComplaint()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getDisposalDomasticDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Generating Domestic Waste as per consent");
		dmv1.setRequiredValue(dmList.get(0).getRequiredDisposalDomesticAsPerConsent()==1?"Yes":dmList.get(0).getRequiredDisposalDomesticAsPerConsent()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getDisposalDomesticAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getDisposalDomesticAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getDisposalDomesticAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getDisposalDomesticAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getDisposalDomesticAsPerConsent()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getBankGurrentyImposedDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Bank Gurantee Imposed");
		dmv1.setRequiredValue(dmList.get(0).getRequiredBgImposed()==1?"Yes":dmList.get(0).getRequiredBgImposed()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getBgImposed()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getBgImposed()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getBgImposed()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getBgImposed()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getBgImposed()==1?"Yes":"No");
		return dmv1;
	}
	
	private DecisionMakingParamVo getDisposalIndustrialDataVo(List<DecisionMaking> dmList) {
		DecisionMakingParamVo dmv1 = new DecisionMakingParamVo();
		dmv1.setParameter("Disposal Industrial Waste as per Consent");
		dmv1.setRequiredValue(dmList.get(0).getRequiredDisposalIndustrialAsPerConsent()==1?"Yes":dmList.get(0).getRequiredDisposalIndustrialAsPerConsent()==-999999?"-":"No");
		dmv1.setCurrentValue(dmList.get(0).getDisposalIndustrialAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>1) 
			dmv1.setPastValueDate1(dmList.get(1).getDisposalIndustrialAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>2) 
			dmv1.setPastValueDate2(dmList.get(2).getDisposalIndustrialAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>3) 
			dmv1.setPastValueDate3(dmList.get(3).getDisposalIndustrialAsPerConsent()==1?"Yes":"No");
		if(dmList.size()>4) 
			dmv1.setPastValueDate4(dmList.get(4).getDisposalIndustrialAsPerConsent()==1?"Yes":"No");
		return dmv1;
	}
}
