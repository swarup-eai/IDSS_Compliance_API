package com.eai.idss.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eai.idss.model.Legal;
import com.eai.idss.model.User;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.ConcentByRegionVo;
import com.eai.idss.vo.LegalByTeamVo;
import com.eai.idss.vo.LegalDetailsRequest;
import com.eai.idss.vo.LegalFilter;
import com.eai.idss.vo.LegalGroupByVo;
import com.eai.idss.vo.LegalPaginationResponseVo;
import com.eai.idss.vo.LegalSubRegionVo;
import com.eai.idss.vo.TileVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class LegalDaoImpl implements LegalDao {
	

	@Value("${dbName}")
	private String dbName;
	
	public static final Logger logger = Logger.getLogger(LegalDaoImpl.class);
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoClient mongoClient;

	public Map<String,List<TileVo>> getPendingLegalActionsData(LegalFilter cf,String region,String subRegion){
		try {
			logger.info("getPendingLegalActionsData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForLegal();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getPendingLegalActionsData : "+days);
            	List<? extends Bson> pipeline = getPendingLegalActionsPipeline(cf,daysMap.get(days),region,subRegion);
	            
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
	
	private List<? extends Bson> getPendingLegalActionsPipeline(LegalFilter cf,List<String> days,String region,String subRegion) throws ParseException {
		
		Document matchDoc = new Document();
		
		matchDoc.append("issuedOn", new Document()
							.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
							.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
						);
		matchDoc.append("legalDirection", new Document().append("$in", IDSSUtil.getLegalActionsList()));
		
		matchDoc.append("complied",0);
		
		if(null!=cf && null!=cf.getPendingResponseByIndustryCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getPendingResponseByIndustryCategoryList()));
		if(null!=cf && null!=cf.getPendingResponseByIndustryScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getPendingResponseByIndustryScaleList()));


		if(StringUtils.hasText(region) && !"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		if(StringUtils.hasText(subRegion) && !"ALL".equalsIgnoreCase(subRegion))
			matchDoc.append("subRegion",subRegion);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", "$legalDirection")
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
	
	public Map<String,Map<String,List<TileVo>>> getLegalActionsByIndustryScaleCategoryData(String region,String subRegion){
		try {
			logger.info("getLegalActionsByIndustryScaleTypeData");
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            
            Map<String,Map<String,List<TileVo>>> tileMap = new LinkedHashMap<String,Map<String, List<TileVo>>>();
            
            tileMap.put("scale", getDataByIndustryScaleCategoryData(  collection,"scale",region,subRegion));
            
            tileMap.put("category", getDataByIndustryScaleCategoryData(  collection,"category",region,subRegion));
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String,List<TileVo>> getDataByIndustryScaleCategoryData(MongoCollection<Document> collection,String aggregateBy,String region,String subRegion) throws ParseException {
			Map<String,List<TileVo>> legalMap = new LinkedHashMap<String, List<TileVo>>();
			logger.info("getDataByIndustryScaleCategoryData : "+aggregateBy);
		    List<? extends Bson> pipeline = getLegalActionsByCategoryScalePipeline(aggregateBy,region,subRegion);
		    
		    collection.aggregate(pipeline)
		            .allowDiskUse(false)
		            .forEach(new Consumer<Document>() {
			                @Override
			                public void accept(Document document) {
			                    logger.info(document.toJson());
								try {
									LegalGroupByVo lVo = new ObjectMapper().readValue(document.toJson(), LegalGroupByVo.class);
									List<TileVo> lVoList = legalMap.get(lVo.getGroupBy());
									if(null==lVoList) {
										lVoList = new ArrayList<TileVo>();
										lVoList.add(new TileVo(lVo.getAction(),lVo.getCount()));
									}else {
										lVoList.add(new TileVo(lVo.getAction(),lVo.getCount()));
									}
									legalMap.put(lVo.getGroupBy(), lVoList);
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (JsonProcessingException e) {
									e.printStackTrace();
								}
			                    
			                }
			            }
		            );
		return legalMap;
	}
	
	private List<? extends Bson> getLegalActionsByCategoryScalePipeline(String aggregateBy,String region,String subRegion) throws ParseException {
		List<String> legalActionsList = IDSSUtil.getLegalActionsList();
		Document matchDoc = new Document();
		Document groupDoc = null;
		
		if("scale".equalsIgnoreCase(aggregateBy)) {
			matchDoc 
            		.append("legalDirection", new Document().append("$in", legalActionsList))
                    .append("scale", new Document().append("$in", IDSSUtil.getScaleList())
                    
		            );
			groupDoc = new Document()
			        .append("$group", new Document()
	                        .append("_id", new Document()
	                                .append("groupBy", "$scale")
	                                .append("legalDirection", "$legalDirection")
	                        )
	                        .append("count", new Document()
	                                .append("$sum", 1.0)
	                        )
	                );
		}
		else if("category".equalsIgnoreCase(aggregateBy)) {
			matchDoc 
	            	.append("legalDirection", new Document().append("$in", legalActionsList))
	                .append("category", new Document().append("$in", IDSSUtil.getCategoryList())
	                    
		            );
			
			groupDoc = new Document()
			        .append("$group", new Document()
	                        .append("_id", new Document()
	                                .append("groupBy", "$category")
	                                .append("legalDirection", "$legalDirection")
	                        )
	                        .append("count", new Document()
	                                .append("$sum", 1.0)
	                        )
	                );
		}
		
		if(!"ALL".equalsIgnoreCase(region))
			matchDoc.append("region",region);
		if(!"ALL".equalsIgnoreCase(subRegion))
			matchDoc.append("subRegion",subRegion);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),
				 groupDoc,
		        new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("groupBy", "$_id.groupBy")
		                    .append("action", "$_id.legalDirection")
		                    .append("count", "$count")
		            )
				);
		return pipeline;
	}
	
	
	public Map<String,Map<String,List<TileVo>>> getByRegionLegalData(LegalFilter cf){
		try {
			logger.info("getByRegionLegalData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMapForLegal();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            Map<String,Map<String,List<TileVo>>> byRegionMap = new LinkedHashMap<String, Map<String,List<TileVo>>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getByRegionLegalData : "+days);
            	Map<String,List<TileVo>> regionConcentMap = IDSSUtil.getRegionMap();
	            List<? extends Bson> pipeline = getByRegionLegalPipeline(days,cf);
	            
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
	
	private List<? extends Bson> getByRegionLegalPipeline(String days,LegalFilter cf) throws ParseException {
		
		Document matchDoc = new Document();
		
		matchDoc.append("issuedOn", new Document()
				.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
			);
		matchDoc.append("legalDirection", new Document().append("$in", IDSSUtil.getLegalActionsList()));
		if(null!=cf && null!=cf.getRegionWiseCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getRegionWiseCategoryList()));
		if(null!=cf && null!=cf.getRegionWiseScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getRegionWiseScaleList()));
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("region", "$region")
                                        .append("action", "$legalDirection")
                                )
                                .append("count", new Document()
                                        .append("$sum", 1.0)
                                )
                        ), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("region", "$_id.region")
                                .append("status", "$_id.action")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("region", 1.0)
                        )
        );
		return pipeline;
	}
	
	
	
	public  Map<String,Map<String,List<TileVo>>> getBySubRegionLegalData(List<String> subRegion, LegalFilter cf){
		try {
			logger.info("getBySubRegionLegalData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMapForLegal();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            Map<String,Map<String,List<TileVo>>> tileMap = new LinkedHashMap<String, Map<String,List<TileVo>>>();
            
            
            for(String days : daysMap.keySet()) {
            	logger.info("getBySubRegionLegalData : "+days);
	            List<? extends Bson> pipeline = getBySubRegionLegalPipeline(subRegion,days,cf);
	            Map<String,List<TileVo>> subRegionMap = new LinkedHashMap<String, List<TileVo>>();
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
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
	
	private List<? extends Bson> getBySubRegionLegalPipeline(List<String> subRegion,String days,LegalFilter cf) throws ParseException {
		Document matchDoc = new Document();
		
		matchDoc.append("issuedOn", new Document()
				.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000"))
			);
		matchDoc.append("subRegion", new Document().append("$in", subRegion));

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
	
	public Map<String,List<TileVo>> getByTeamLegalData(LegalFilter cf,User u){
		try {
			logger.info("getByTeamLegalData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForLegal();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            Map<String,List<TileVo>> byTeamMap = new LinkedHashMap<String, List<TileVo>>(); 
            
            for(String days : daysMap.keySet()) {
            	logger.info("getByTeamLegalData : "+days);
	            List<? extends Bson> pipeline = getByTeamLegalPipeline(daysMap.get(days),cf,u);
	            
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										LegalByTeamVo crVo = new ObjectMapper().readValue(document.toJson(), LegalByTeamVo.class);
										
										TileVo tVo = new TileVo(days,crVo.getCount());
										
										List<TileVo> lbtList = byTeamMap.get(crVo.getName()+"~"+crVo.getDesignation()+"~"+crVo.getUserId());
										//List<TileVo> lbtList = byTeamMap.get(crVo.getUserId());
										if(null==lbtList) lbtList = new ArrayList<TileVo>();
										lbtList.add(tVo);
										byTeamMap.put(crVo.getName()+"~"+crVo.getDesignation()+"~"+crVo.getUserId(), lbtList);
									
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (JsonProcessingException e) {
										e.printStackTrace();
									}
		    	                    
		    	                }
		    	            }
	                    );
            }
            return byTeamMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<? extends Bson> getByTeamLegalPipeline(List<String> days,LegalFilter cf,User u) throws ParseException {
		
		Document matchDoc = new Document();
		
		matchDoc.append("issuedOn", new Document()
							.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
							.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
						);
		matchDoc.append("complied",0);

		if(null!=cf && null!=cf.getPendingByTeamCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getPendingByTeamCategoryList()));
		if(null!=cf && null!=cf.getPendingByTeamActionList() ) 
			matchDoc.append("legalDirection", new Document().append("$in", cf.getPendingByTeamActionList()));
		else
			matchDoc.append("legalDirection", new Document().append("$in", IDSSUtil.getLegalActionsList()));
		
		if("RO".equalsIgnoreCase(u.getDesignation()))
			matchDoc.append("reportingToUserId",u.getUserName());
		else
			matchDoc.append("region",u.getRegion());
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
                new Document()
                    		.append("$group", new Document()
    		                		.append("_id", new Document()
                                            .append("name", "$sroName")
                                            .append("userId", "$userId")
                                    )
                                .append("count", new Document()
                                        .append("$sum", 1.0)
                                )
                        ), 
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("name", "$_id.name")
                                .append("userId", "$_id.userId")
                                .append("designation", "SRO")
                                .append("count", "$count")
                        ), 
                new Document()
                        .append("$sort", new Document()
                                .append("name", 1.0)
                        )
        );
		return pipeline;
	}
	
	public LegalPaginationResponseVo getLegalPaginatedRecords(LegalDetailsRequest cdr, Pageable page){
		try {
			Query query = new Query().with(page);
			getQueryCriteria(cdr, query);
			
			Query queryCnt = new Query().with(page);
			getQueryCriteria(cdr, queryCnt);
			
			LegalPaginationResponseVo lprv = new LegalPaginationResponseVo(); 
			lprv.setTotalRecords(mongoTemplate.count(queryCnt, Legal.class));
			
			List<Legal> filteredLegalList=  mongoTemplate.find(query, Legal.class);
			
			Page<Legal> cPage = PageableExecutionUtils.getPage(
					filteredLegalList,
					page,
			        () -> mongoTemplate.count(query, Legal.class));
			
			lprv.setLegalList(cPage.toList());
			return lprv;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new LegalPaginationResponseVo(new ArrayList<Legal>(),0);
	}

	private void getQueryCriteria(LegalDetailsRequest cdr, Query query) throws ParseException {
		if(null!=cdr) {
			if(StringUtils.hasText(cdr.getDuration())) {
				String[] d = cdr.getDuration().split("_");
				
				LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
				LocalDateTime fromDate = currentTime.minusDays(Integer.parseInt(d[0]));
				String fromDay = fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
				String toDay = null;
				if("ALL".equalsIgnoreCase(d[1])) {
					toDay = "1970-01-01";
				}else {
					LocalDateTime toDate = currentTime.minusDays(Integer.parseInt(d[1]));
					toDay = toDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
				}
				
				query.addCriteria(Criteria.where("issuedOn")
												.gt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDay+" 00:00:00.000+0000"))
												.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDay+" 00:00:00.000+0000")));
			}
			if(StringUtils.hasText(cdr.getComplied()))
				query.addCriteria(Criteria.where("complied").is(cdr.getComplied()));
			if(StringUtils.hasText(cdr.getRegion()))
					query.addCriteria(Criteria.where("region").is(cdr.getRegion()));
			if(null!=cdr.getCategory() && !cdr.getCategory().isEmpty())
					query.addCriteria(Criteria.where("category").in(cdr.getCategory()));
			if(StringUtils.hasText(cdr.getSubRegion()))
					query.addCriteria(Criteria.where("subRegion").is(cdr.getSubRegion()));
			if(null!=cdr.getScale() && !cdr.getScale().isEmpty() )
				query.addCriteria(Criteria.where("scale").in(cdr.getScale()));
			if(StringUtils.hasText(cdr.getUserId()))
				query.addCriteria(Criteria.where("userId").is(cdr.getUserId()));
			if(StringUtils.hasText(cdr.getAction()))
				query.addCriteria(Criteria.where("legalDirection").is(cdr.getAction()));
			else
				query.addCriteria(Criteria.where("legalDirection").in(IDSSUtil.getLegalActionsList()));
			
		}
	}
	
	public Map<String,List<TileVo>> getLegalActionsByIndustryData(LegalFilter cf,String region,String subRegion){
		try {
			logger.info("getLegalActionsByIndustryData");
			Map<String, String> daysMap = IDSSUtil.getPastDaysMap();//IDSSUtil.getDaysMapForLegal();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getLegalActionsByIndustryData : "+days);
            	List<? extends Bson> pipeline = getLegalActionsByIndustryPipeline(cf,days,region,subRegion);
	            
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
private List<? extends Bson> getLegalActionsByIndustryPipeline(LegalFilter cf,String days,String region,String subRegion) throws ParseException {
		
		Document matchDoc = new Document();
		
//		matchDoc.append("issuedOn", new Document()
//							.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
//							.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
//						);
		matchDoc.append("issuedOn", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days+" 00:00:00.000+0000")));
		matchDoc.append("legalDirection", new Document().append("$in", IDSSUtil.getLegalActionsList()));
		
//		matchDoc.append("complied",1);
		
		if(null!=cf && null!=cf.getLegalActionsByIndustryCategoryList() ) 
			matchDoc.append("category", new Document().append("$in", cf.getLegalActionsByIndustryCategoryList()));
		if(null!=cf && null!=cf.getLegalActionsByIndustryScaleList() ) 
			matchDoc.append("scale", new Document().append("$in", cf.getLegalActionsByIndustryScaleList()));

	if(StringUtils.hasText(region) && !"ALL".equalsIgnoreCase(region))
		matchDoc.append("region",region);
	if(StringUtils.hasText(subRegion) && !"ALL".equalsIgnoreCase(subRegion))
		matchDoc.append("subRegion",subRegion);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", "$legalDirection")
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
	
}
