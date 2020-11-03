package com.eai.idss.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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

import com.eai.idss.model.Visits;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.ConcentByRegionVo;
import com.eai.idss.vo.LegalSubRegionVo;
import com.eai.idss.vo.TileVo;
import com.eai.idss.vo.VisitsByScaleCategory;
import com.eai.idss.vo.VisitsDetailsRequest;
import com.eai.idss.vo.VisitsFilter;
import com.eai.idss.vo.VisitsScheduleDetailsRequest;
import com.eai.idss.vo.VisitsSubRegionVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class VisitsDaoImpl implements VisitsDao {
	
	private static final String TEAM_WISE = "TeamWise";

	private static final String REGION_WISE = "RegionWise";

	private static final String LEGAL_NOTICES = "legalNotices";

	private static final String COMPLETED = "completed";

	private static final String PENDING = "pending";

	private static final String SCHEDULED = "scheduled";

	public static final Logger logger = Logger.getLogger(VisitsDaoImpl.class);
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoClient mongoClient;

	public Map<String,List<TileVo>> getPendingVisitsData(VisitsFilter vf){
		try {
			logger.info("getPendingVisitsData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForVisits();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getPendingVisitsData : "+days);
	            List<? extends Bson> pipeline = getPendingVisitsPipeline(daysMap.get(days),vf);
	            
	            List<TileVo> tVoList = new ArrayList<TileVo>();
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    System.out.println(document.toJson());
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
	
	private List<? extends Bson> getPendingVisitsPipeline(List<String> days,VisitsFilter vf) throws ParseException {
		
		Document matchDoc = new Document();
		
		matchDoc.append("schduledOn", new Document()
							.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
							.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
						);
		matchDoc.append("visitStatus", "Not visited");
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
		                    System.out.println(type +" ::: "+document.toJson());
							try {
								if(REGION_WISE.equalsIgnoreCase(extractType)) {
									ConcentByRegionVo crVo = new ObjectMapper().readValue(document.toJson(), ConcentByRegionVo.class);
									TileVo tVo = new TileVo(crVo.getRegion(),crVo.getCount());
									List<TileVo> concentStatusList = regionVisitMap.get(crVo.getRegion());
									if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
									concentStatusList.add(tVo);
									regionVisitMap.put(type, concentStatusList);
								}else if(TEAM_WISE.equalsIgnoreCase(extractType)) {
									VisitsSubRegionVo crVo = new ObjectMapper().readValue(document.toJson(), VisitsSubRegionVo.class);
									TileVo tVo = new TileVo(type,crVo.getCount());
									List<TileVo> concentStatusList = regionVisitMap.get(crVo.getSubRegion()+"~"+crVo.getDesignation());
									if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
									concentStatusList.add(tVo);
									regionVisitMap.put(crVo.getSubRegion()+"~"+crVo.getDesignation(), concentStatusList);
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
		LocalDateTime currentTime = LocalDateTime.now();
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
			matchDoc.append("visitStatus", "Not visited");
		}
		
		if(COMPLETED.equalsIgnoreCase(caseType)) {
			matchDoc.append("visitedDate", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("visitStatus","Visited");
		}
		
		if(LEGAL_NOTICES.equalsIgnoreCase(caseType)) {
			matchDoc.append("legalDirectionIssuedOn", new Document()
					.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
				);
			matchDoc.append("legalDirection", new Document().append("$ne", "NA"));
		}
	}
	
	
	public  Map<String,Map<String,List<TileVo>>> getBySubRegionVisitsData(String region, VisitsFilter cf){
		try {
			logger.info("getBySubRegionVisitsData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMapForLegal();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,Map<String,List<TileVo>>> tileMap = new LinkedHashMap<String, Map<String,List<TileVo>>>();
            
            
            for(String days : daysMap.keySet()) {
            	logger.info("getBySubRegionVisitsData : "+days);
	            List<? extends Bson> pipeline = getBySubRegionVisitsPipeline(region,days,cf);
	            Map<String,List<TileVo>> subRegionMap = new LinkedHashMap<String, List<TileVo>>();
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    System.out.println(document.toJson());
									try {
										LegalSubRegionVo lsVo = new ObjectMapper().readValue(document.toJson(), LegalSubRegionVo.class);
										List<TileVo> tVoList = subRegionMap.get(lsVo.getSubRegion());
										if(null==tVoList)
											tVoList = new ArrayList<TileVo>();
										tVoList.add(new TileVo(lsVo.getLegalDirection(),lsVo.getCount()));
										subRegionMap.put(lsVo.getSubRegion(),tVoList);
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (JsonProcessingException e) {
										e.printStackTrace();
									}
		    	                    
		    	                }
		    	            }
	                    );
	            tileMap.put(daysMap.get(days), subRegionMap);
            
            }
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getBySubRegionVisitsPipeline(String region,String days,VisitsFilter cf) throws ParseException {
		Document matchDoc = new Document();
		
		matchDoc.append("issuedOn", new Document()
				.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
			);
		matchDoc.append("region",region);
		
		matchDoc.append("legalDirection", new Document().append("$in", IDSSUtil.getLegalActionsList()));
		
		if(null!=cf && null!=cf.getSubRegionWiseCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getSubRegionWiseCategoryList()));
		if(null!=cf && null!=cf.getSubRegionWiseScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getSubRegionWiseScaleList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
		                		.append("_id", new Document()
                                        .append("legalDirection", "$legalDirection")
                                        .append("subRegion", "$subRegion")
                                )
		                        .append("caseCount", new Document()
		                                .append("$sum", 1)
		                        )
		                ),
		        new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("legalDirection", "$_id.legalDirection")
		                    .append("subRegion", "$_id.subRegion")
		                    .append("count", "$caseCount")
		            )
				);
		return pipeline;
	}
	
	public Map<String,Map<String,List<TileVo>>> getByTeamVisitsData(VisitsFilter cf,String region){
		try {
			logger.info("getByTeamVisitsData");
			Map<String, List<String>> daysMap = IDSSUtil.getPastAndFutureDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            Map<String,Map<String,List<TileVo>>> byRegionMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getByTeamVisitsData : "+days);
            	Map<String,List<TileVo>> regionVisitMap = new LinkedHashMap<String, List<TileVo>>();
            	
            	List<? extends Bson> pipeline = getByTeamVisitsPipeline(PENDING,daysMap.get(days).get(0),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,PENDING,TEAM_WISE);
            
	            pipeline = getByTeamVisitsPipeline(SCHEDULED,daysMap.get(days).get(1),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,SCHEDULED,TEAM_WISE);
	            
	            pipeline = getByTeamVisitsPipeline(LEGAL_NOTICES,daysMap.get(days).get(0),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,LEGAL_NOTICES,TEAM_WISE);

	            pipeline = getByTeamVisitsPipeline(COMPLETED,daysMap.get(days).get(0),cf,region);
	            
	            extractData(collection, regionVisitMap, pipeline,COMPLETED,TEAM_WISE);
	            
	            byRegionMap.put(days,regionVisitMap);
            
            }
            return byRegionMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getByTeamVisitsPipeline(String caseType,String days,VisitsFilter cf,String region) throws ParseException {
		
		Document matchDoc = new Document();
		
		applyMatchFilter(caseType, days, matchDoc);
		
		matchDoc.append("region",region);
		
		if(null!=cf && null!=cf.getPendingByTeamCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getPendingByTeamCategoryList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("team", "$subregion")
                                        .append("designation", "$adminDesignation")
                                )
                                .append("count", new Document()
                                        .append("$sum", 1.0)
                                )
                        ), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("subRegion", "$_id.team")
                                .append("designation", "$_id.designation")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("subRegion", 1.0)
                        )
        );
		return pipeline;
	}
	
	public List<Visits> getVisitsPaginatedRecords(VisitsDetailsRequest cdr, Pageable page){
		try {
			Query query = new Query().with(page);
			if(null!=cdr) {
				
				LocalDateTime currentTime = LocalDateTime.now();
				LocalDateTime date = null;
				if(SCHEDULED.equalsIgnoreCase(cdr.getVisitStatus()))
					date = currentTime.plusDays(cdr.getDuration());
				else
					date = currentTime.minusDays(cdr.getDuration());
				String day = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
				
				addCriteriaFilter(cdr.getVisitStatus(), day, query);
				
				if(StringUtils.hasText(cdr.getRegion()))
					query.addCriteria(Criteria.where("region").is(cdr.getRegion()));
				if(StringUtils.hasText(cdr.getCategory()))
					query.addCriteria(Criteria.where("category").is(cdr.getCategory()));
				if(StringUtils.hasText(cdr.getSubRegion()))
					query.addCriteria(Criteria.where("subregion").is(cdr.getSubRegion()));
				if(StringUtils.hasText(cdr.getScale()))
					query.addCriteria(Criteria.where("scale").is(cdr.getScale()));
				if(StringUtils.hasText(cdr.getIndustryName()))
					query.addCriteria(Criteria.where("industryName").is(cdr.getIndustryName()));
			}
	
			System.out.println(mongoTemplate.count(query, Visits.class));
			
			List<Visits> filteredLegalList= mongoTemplate.find(query, Visits.class);
			
			filteredLegalList.stream().forEach(v -> v.setElapsedDays(ChronoUnit.DAYS.between(LocalDate.now(),v.getSchduledOn())));
			
			Page<Visits> cPage = PageableExecutionUtils.getPage(
					filteredLegalList,
					page,
			        () -> mongoTemplate.count(query, Visits.class));
			
			return cPage.toList();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void addCriteriaFilter(String caseType, String days, Query query) throws ParseException {
		
		LocalDateTime currentTime = LocalDateTime.now();
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		if(SCHEDULED.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("schduledOn")
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateToday+" 00:00:00.000+0000")));
		}
		if(PENDING.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("schduledOn")
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateToday+" 00:00:00.000+0000"))
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("visitStatus").is("Not visited"));
		}
		
		if(COMPLETED.equalsIgnoreCase(caseType)) {
			query.addCriteria(Criteria.where("visitedDate")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("visitStatus").is("Visited"));
		}
		
		if(LEGAL_NOTICES.equalsIgnoreCase(caseType)) {
			
			query.addCriteria(Criteria.where("legalDirectionIssuedOn")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
			
			query.addCriteria(Criteria.where("legalDirection").ne("NA"));
		}
	}
	
	public List<Visits> getVisitsSchedulePaginatedRecords(VisitsScheduleDetailsRequest cdr, Pageable pageable){
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
				
//			if(StringUtils.hasText(cdr.getCompliance())) {
//				String[] op = cdr.getCompliance().split("-");
//				query.addCriteria(Criteria.where("cScore").gte(Integer.parseInt(op[0])).lte(Integer.parseInt(op[1])));
//			}
			
			if(StringUtils.hasText(cdr.getStatus())) {
				if("Pending".equalsIgnoreCase(cdr.getStatus()) || "Scheduled".equalsIgnoreCase(cdr.getStatus()))
					query.addCriteria(Criteria.where("visitStatus").is("Not visited"));
				if("Visited".equalsIgnoreCase(cdr.getStatus()))
					query.addCriteria(Criteria.where("visitStatus").is("Visited"));
			}
	
			System.out.println(mongoTemplate.count(query, Visits.class));
			
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
	    	                    System.out.println(document.toJson());
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
}
