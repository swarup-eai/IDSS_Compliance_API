package com.eai.idss.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.eai.idss.model.Consent;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.ConcentByRegionVo;
import com.eai.idss.vo.ConcentByStatusVo;
import com.eai.idss.vo.ConcentFilter;
import com.eai.idss.vo.ConsentDetailsRequest;
import com.eai.idss.vo.TileVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class ConcentDaoImpl implements ConcentDao {
	
	public static final Logger logger = Logger.getLogger(ConcentDaoImpl.class);
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoClient mongoClient;

	public Map<String,List<TileVo>> getPendingRequestConcentData(ConcentFilter cf,String region,String subRegion){
		try {
			logger.info("getPendingRequestConcentData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Consent");
            
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getPendingRequestConcentData : "+days);
	            List<? extends Bson> pipeline = getPendingRequestConcentPipeline(days,cf,region,subRegion);
	            
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
	            tileMap.put(daysMap.get(days), tVoList);
            
            }
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getPendingRequestConcentPipeline(String days,ConcentFilter cf,String region,String subRegion) throws ParseException {
		Document matchDoc = new Document();
		
		matchDoc.append("created", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
		if(null!=cf && null!=cf.getPendingCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getPendingCategoryList()));
		if(null!=cf && null!=cf.getPendingScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getPendingScaleList()));
		if(!"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		if(!"ALL".equalsIgnoreCase(subRegion))
			matchDoc.append("subRegion",subRegion);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", "$consentStatus")
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
	
	public Map<String,Map<String,List<TileVo>>> getUpcomingRenewalConcentData(String region,String subRegion){
		try {
			logger.info("getUpcomingRenewalConcentData");
			Map<String, String> daysMap = IDSSUtil.getFutureDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Consent");
            
            
            Map<String,Map<String,List<TileVo>>> tileMap = new LinkedHashMap<String,Map<String, List<TileVo>>>();
            
            tileMap.put("scale", getConsentRenewalData( daysMap, collection,"scale", region, subRegion));
            
            tileMap.put("category", getConsentRenewalData( daysMap, collection,"category", region, subRegion));
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String,List<TileVo>> getConsentRenewalData( Map<String, String> daysMap, MongoCollection<Document> collection,String aggregateBy,String region,String subRegion) throws ParseException {
		Map<String,List<TileVo>> daysConsentMap = new LinkedHashMap<String, List<TileVo>>();
		for(String days : daysMap.keySet()) {
			logger.info("getUpcomingRenewalConcentData : "+days);
		    List<? extends Bson> pipeline = getUpcomingRenewalConcentPipeline(days,aggregateBy, region, subRegion);
		    
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
		    daysConsentMap.put(daysMap.get(days), tVoList);
		
		}
		return daysConsentMap;
	}
	
	private List<? extends Bson> getUpcomingRenewalConcentPipeline(String days,String aggregateBy,String region,String subRegion) throws ParseException {
		LocalDateTime currentTime = LocalDateTime.now();
		String currentDay = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		Document matchDoc = null;
		
		if(currentDay.equalsIgnoreCase(days)) {
			matchDoc = new Document()
            .append("$match", new Document()
                    .append("status", "Approved")
                    .append("created", new Document()
                            .append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(currentDay+" 00:00:00.000+0000"))
                    )
            ); 
		}else {
			matchDoc = new Document()
		            .append("$match", new Document()
		                    .append("consentStatus", "Renewal")
		                    .append("consentValidityDate", new Document()
		                    		.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
		                    		.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(currentDay+" 00:00:00.000+0000")))
		                    );
		}
		
		if(!"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		if(!"ALL".equalsIgnoreCase(subRegion))
			matchDoc.append("subRegion",subRegion);
		
		List<? extends Bson> pipeline = Arrays.asList(

				matchDoc,
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", "$"+aggregateBy)
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
	
	
	public Map<String,Map<String,List<TileVo>>> getByRegionConcentData(ConcentFilter cf){
		try {
			logger.info("getByRegionConcentData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Consent");
            
            Map<String,Map<String,List<TileVo>>> byRegionMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getByRegionConcentData : "+days);
            	Map<String,List<TileVo>> regionConcentMap = IDSSUtil.getRegionMap();
	            List<? extends Bson> pipeline = getByRegionConcentPipeline(days,cf);
	            
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										ConcentByRegionVo crVo = new ObjectMapper().readValue(document.toJson(), ConcentByRegionVo.class);
										String regionNameCamelCase = crVo.getRegion().replaceAll(" ", "").replaceAll("\"", "");
										regionNameCamelCase = "" +Character.toLowerCase(regionNameCamelCase.charAt(0)) + regionNameCamelCase.substring(1);
										crVo.setRegion(regionNameCamelCase);
										TileVo tVo = new TileVo(crVo.getStatus(),crVo.getCount());
										List<TileVo> concentStatusList = regionConcentMap.get(crVo.getRegion());
										if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
										concentStatusList.add(tVo);
										regionConcentMap.put(crVo.getRegion(), concentStatusList);
									
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (JsonProcessingException e) {
										e.printStackTrace();
									}
		    	                    
		    	                }
		    	            }
	                    );
	            byRegionMap.put(daysMap.get(days),regionConcentMap);
            }
            return byRegionMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getByRegionConcentPipeline(String days,ConcentFilter cf) throws ParseException {
		
		Document matchDoc = new Document();
		
		matchDoc.append("created", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
		if(null!=cf && null!=cf.getPendingCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getRegionWiseCategoryList()));
		if(null!=cf && null!=cf.getPendingScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getRegionWiseScaleList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("region", "$region")
                                        .append("status", "$consentStatus")
                                )
                                .append("count", new Document()
                                        .append("$sum", 1.0)
                                )
                        ), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("region", "$_id.region")
                                .append("status", "$_id.status")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("region", 1.0)
                        )
        );
		return pipeline;
	}
	
	
	public Map<String,Map<String,List<TileVo>>> getBySubRegionConcentData(String region, ConcentFilter cf){
		try {
			logger.info("getPendingRequestConcentData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Consent");


			Map<String,Map<String,List<TileVo>>> tileMap = new LinkedHashMap<String, Map<String,List<TileVo>>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getPendingRequestConcentData : "+days);
	            List<? extends Bson> pipeline = getBySubRegionConcentPipeline(region,days,cf);

//	            List<TileVo> tVoList = new ArrayList<TileVo>();
				Map<String,List<TileVo>> subRegionMap = new LinkedHashMap<String, List<TileVo>>();

				collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										ConcentByRegionVo cVo = new ObjectMapper().readValue(document.toJson(), ConcentByRegionVo.class);
										List<TileVo> tVoList = subRegionMap.get(cVo.getSubRegion());
										if(null==tVoList)
											tVoList = new ArrayList<TileVo>();
										tVoList.add(new TileVo(cVo.getStatus(),cVo.getCount()));
										subRegionMap.put(cVo.getSubRegion(),tVoList);
									
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
	
	private List<? extends Bson> getBySubRegionConcentPipeline(String region,String days,ConcentFilter cf) throws ParseException {
		Document matchDoc = new Document();
		
		matchDoc.append("created", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
		matchDoc.append("region",region);
		if(null!=cf && null!=cf.getSubRegionWiseCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getSubRegionWiseCategoryList()));
		if(null!=cf && null!=cf.getSubRegionWiseScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getSubRegionWiseScaleList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
//		                        .append("_id", "$consentStatus")
								.append("_id", new Document()
										.append("consentStatus", "$consentStatus")
										.append("subRegion", "$subRegion")
								)
		                        .append("caseCount", new Document()
		                                .append("$sum", 1)
		                        )
		                ),
		        new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("status", "$_id.consentStatus")
							.append("subRegion", "$_id.subRegion")
							.append("count", "$caseCount")
		            )
				);
		return pipeline;
	}
	
	public Map<String,Map<String,List<TileVo>>> getByTeamConcentData(ConcentFilter cf,String region){
		try {
			logger.info("getByTeamConcentData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMap();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Consent");
            
            Map<String,Map<String,List<TileVo>>> byTeamMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getByTeamConcentData : "+days);
            	Map<String,List<TileVo>> subRegionConcentMap = new LinkedHashMap<String, List<TileVo>>();
	            List<? extends Bson> pipeline = getByTeamConcentPipeline(days,cf,region);
	            
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										ConcentByStatusVo crVo = new ObjectMapper().readValue(document.toJson(), ConcentByStatusVo.class);
										TileVo tVo = new TileVo(crVo.getStatus(),crVo.getCount());
										List<TileVo> concentStatusList = subRegionConcentMap.get(crVo.getSubRegion()+"~"+crVo.getDesignation());
										if(null==concentStatusList) concentStatusList = new ArrayList<TileVo>();
										concentStatusList.add(tVo);
										subRegionConcentMap.put(crVo.getSubRegion()+"~"+crVo.getDesignation(), concentStatusList);
									
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (JsonProcessingException e) {
										e.printStackTrace();
									}
		    	                    
		    	                }
		    	            }
	                    );
	            byTeamMap.put(daysMap.get(days),subRegionConcentMap);
            }
            return byTeamMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getByTeamConcentPipeline(String days,ConcentFilter cf,String region) throws ParseException {
		
		Document matchDoc = new Document();
		
		matchDoc.append("created", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
		if(null!=cf && null!=cf.getPendingByTeamCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getPendingByTeamCategoryList()));
		if(null!=cf && null!=cf.getPendingByTeamScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getPendingByTeamScaleList()));
		matchDoc.append("region",region);
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("subRegion", "$subRegion")
                                        .append("designation", "$adminDesignation")
                                        .append("status", "$status")
                                )
                                .append("count", new Document()
                                        .append("$sum", 1.0)
                                )
                        ), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("subRegion", "$_id.subRegion")
                                .append("designation", "$_id.designation")
                                .append("status", "$_id.status")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("subRegion", 1.0)
                        )
        );
		return pipeline;
	}
	
	public List<Consent> getConsentPaginatedRecords(ConsentDetailsRequest cdr, Pageable page){
		try {
			Query query = new Query().with(page);
			if(null!=cdr) {
				LocalDateTime currentTime = LocalDateTime.now();
				String today = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
				LocalDateTime futuredate = currentTime.plusDays(cdr.getDuration());
				String pastOrFutureDay = futuredate.format(DateTimeFormatter.ISO_LOCAL_DATE);
				
				if(null!=cdr.getDuration() && cdr.getDuration().intValue()>=0) {
					query.addCriteria(Criteria.where("status").is("Approved"));
					if(cdr.getDuration().intValue() ==0) // Past Due from current day
					{
						query.addCriteria(Criteria.where("consentValidityDate")
													.lt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(today+" 00:00:00.000+0000")));
					}else { // 30/60/90 days in future
						
						query.addCriteria(Criteria.where("consentValidityDate")
													.lt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(pastOrFutureDay+" 00:00:00.000+0000"))
													.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(today+" 00:00:00.000+0000")));
					}
				}else { // -30, -60, -90 days in past
					query.addCriteria(Criteria.where("created")
													.gt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(pastOrFutureDay+" 00:00:00.000+0000"))
													.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(today+" 00:00:00.000+0000")));
					if(StringUtils.hasText(cdr.getRegion()))
						query.addCriteria(Criteria.where("region").is(cdr.getRegion()));
					if(StringUtils.hasText(cdr.getCategory()))
						query.addCriteria(Criteria.where("category").is(cdr.getCategory()));
					if(StringUtils.hasText(cdr.getScale()))
						query.addCriteria(Criteria.where("scale").is(cdr.getScale()));
					if(StringUtils.hasText(cdr.getStatus()))
						query.addCriteria(Criteria.where("status").is(cdr.getStatus()));
					if(StringUtils.hasText(cdr.getConsentStatus()))
						query.addCriteria(Criteria.where("consentStatus").is(cdr.getConsentStatus()));
					if(StringUtils.hasText(cdr.getSubRegion()))
						query.addCriteria(Criteria.where("adminOffice").is(cdr.getSubRegion()));
				}
			}
			
	
			logger.info(mongoTemplate.count(query, Consent.class));
			
			List<Consent> filteredConsentList= 
			mongoTemplate.find(query, Consent.class);
			Page<Consent> cPage = PageableExecutionUtils.getPage(
					filteredConsentList,
					page,
			        () -> mongoTemplate.count(query, Consent.class));
			
			return cPage.toList();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
