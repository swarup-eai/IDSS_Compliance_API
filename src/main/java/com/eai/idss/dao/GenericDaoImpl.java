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
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eai.idss.model.CScoreMaster;
import com.eai.idss.model.IndustryTypes;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.DashboardRequest;
import com.eai.idss.vo.LegalTileVo;
import com.eai.idss.vo.MyVisits;
import com.eai.idss.vo.MyVisitsIndustries;
import com.eai.idss.vo.TileVo;
import com.eai.idss.vo.TopPerfVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class GenericDaoImpl implements GenericDao {

	public static final Logger logger = Logger.getLogger(GenericDaoImpl.class);
	
	@Autowired
	MongoClient mongoClient;

	@Autowired
	MongoTemplate mongoTemplate;

	public Map<String,List<TileVo>> getConcentTileData(DashboardRequest dbr){
		try {
			logger.info("getConcentTileData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForDashboard();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Consent");
            
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getConcentTileData : "+days);
	            List<? extends Bson> pipeline = getConcentTilePipeline(daysMap.get(days),dbr);
	            TileVo tNewVo = new TileVo("New", 0);
	            List<TileVo> tVoList = new ArrayList<TileVo>();
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										TileVo tVo = new ObjectMapper().readValue(document.toJson(), TileVo.class);
										if(!"Operate".equalsIgnoreCase(tVo.getCaseType()) && !"New".equalsIgnoreCase(tVo.getCaseType()) )
											tVoList.add(tVo);
										else
											tNewVo.setCaseCount(tNewVo.getCaseCount() + tVo.getCaseCount());
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (JsonProcessingException e) {
										e.printStackTrace();
									}
		    	                    
		    	                }
		    	            }
	                    );
	            if(tNewVo.getCaseCount()>0)
	            	tVoList.add(tNewVo);
	            tileMap.put(days, tVoList);
            
            }
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String,List<TileVo>> getLegalTileData(DashboardRequest dbr){
		try {
			logger.info("getLegalTileData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForDashboard();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getLegalTileData : "+days);
	            List<? extends Bson> pipeline = getLegalTilePipeline(daysMap.get(days),dbr);
	            
	            List<TileVo> tVoList = new ArrayList<TileVo>();
	            collection.aggregate(pipeline)
	                    .allowDiskUse(false)
	                    .forEach(new Consumer<Document>() {
		    	                @Override
		    	                public void accept(Document document) {
		    	                    logger.info(document.toJson());
									try {
										LegalTileVo ltVo = new ObjectMapper().readValue(document.toJson(), LegalTileVo.class);
										TileVo tVoNew = new TileVo("new",ltVo.getNeu());
										tVoList.add(tVoNew);
										
										TileVo tVoNotice = new TileVo("notice",ltVo.getNotice());
										tVoList.add(tVoNotice);
										
										TileVo tVoAction = new TileVo("action",ltVo.getAction());
										tVoList.add(tVoAction);
										
										TileVo tVoComplied = new TileVo("complied",ltVo.getComplied());
										tVoList.add(tVoComplied);
									
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

	private List<? extends Bson> getConcentTilePipeline(List<String> days,DashboardRequest dbr) throws ParseException {
		Document matchDoc = new Document();
		matchDoc.append("created", new Document()
                        .append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
                		);
		applyGenericFilter(dbr, matchDoc);
		
		
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
	
	private List<? extends Bson> getLegalTilePipeline(List<String> days,DashboardRequest dbr) throws ParseException {
		Document matchDoc = new Document();
		matchDoc.append("issuedOn", new Document()
                        .append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
                		);
		applyGenericFilter(dbr, matchDoc);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),    
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", null)
		                        .append("new", new Document() .append("$sum", "$new"))
		                        .append("notice", new Document() .append("$sum", "$notice"))
		                        .append("action", new Document() .append("$sum", "$actions"))
		                        .append("complied", new Document() .append("$sum", "$complied"))
		                ),
		        new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("neu", "$new")
		                    .append("notice", "$notice")
		                    .append("action", "$action")
		                    .append("complied", "$complied")
		            )
				);
		return pipeline;
	}

	private void applyGenericFilter(DashboardRequest dbr, Document matchDoc) {
		if(null!=dbr && StringUtils.hasText(dbr.getRegion()) && !"All".equalsIgnoreCase(dbr.getRegion())) 
			matchDoc.append("region", dbr.getRegion());
		if(null!=dbr && StringUtils.hasText(dbr.getScale()) && !"All".equalsIgnoreCase(dbr.getScale())) 
			matchDoc.append("scale", dbr.getScale());
		if(null!=dbr && StringUtils.hasText(dbr.getType()) && !"All".equalsIgnoreCase(dbr.getType())) 
			matchDoc.append("type", dbr.getType());
		if(null!=dbr && StringUtils.hasText(dbr.getCategory()) && !"All".equalsIgnoreCase(dbr.getCategory())) 
			matchDoc.append("category", dbr.getCategory());
	}

	public Map<String,List<TileVo>> getVisitsTileData(DashboardRequest dbr){
		try {
			logger.info("getVisitsTileData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForDashboard();
			
			Map<String, List<String>> futureVisitsMap = IDSSUtil.getFutureDaysMapForVisits();
			
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            populateVisitsDone(dbr, daysMap, collection, tileMap);
            
            populateVisitsPlanned(dbr, futureVisitsMap, collection, tileMap);
            
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void populateVisitsDone(DashboardRequest dbr, Map<String, List<String>> daysMap, MongoCollection<Document> collection, Map<String, List<TileVo>> tileMap) throws ParseException {
		for(String days : daysMap.keySet()) {
			logger.info("getVisitsTileData : "+days);
		    List<? extends Bson> pipeline = getVisitsDoneTilePipeline(daysMap.get(days),dbr);
		    
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
	}
	
	private void populateVisitsPlanned(DashboardRequest dbr, Map<String, List<String>> daysMap, MongoCollection<Document> collection, Map<String, List<TileVo>> tileMap) throws ParseException {
		for(String days : daysMap.keySet()) {
			logger.info("getVisitsTileData : "+days);
		    List<? extends Bson> pipeline = getVisitsPlannedTilePipeline(daysMap.get(days),dbr);
		    
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
		    List<TileVo> existingTileVoList = tileMap.get(days);
		    existingTileVoList.addAll(tVoList);
		    tileMap.put(days, existingTileVoList);
		
		}
	}
	
	private List<? extends Bson> getVisitsPlannedTilePipeline(List<String> days,DashboardRequest dbr) throws ParseException {
		Document matchDoc = new Document();
		matchDoc.append("schduledOn", new Document()
                        .append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
                		);
		applyGenericFilter(dbr, matchDoc);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),    
				new Document()
                .append("$group", new Document()
                        .append("_id", "null")
                        .append("caseCount", new Document()
                                .append("$sum", 1)
                        )
                ),
        new Document()
            .append("$project", new Document()
                    .append("_id", false)
                    .append("caseType", "planned")
                    .append("caseCount", "$caseCount")
            )
		);
		return pipeline;
	}
	
	private List<? extends Bson> getVisitsDoneTilePipeline(List<String> days,DashboardRequest dbr) throws ParseException {
		Document matchDoc = new Document();
		matchDoc.append("visitedDate", new Document()
                        .append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
                		);
		applyGenericFilter(dbr, matchDoc);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),    
				new Document()
                .append("$group", new Document()
                        .append("_id", "null")
                        .append("caseCount", new Document()
                                .append("$sum", 1)
                        )
                ),
        new Document()
            .append("$project", new Document()
                    .append("_id", false)
                    .append("caseType", "visited")
                    .append("caseCount", "$caseCount")
            )
		);
		return pipeline;
	}
	
	public List<MyVisits> getMyVisitsData(String userName){
			logger.info("getMyVisitsData");
			try {
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
	        MongoCollection<Document> collection = database.getCollection("Visit_master");

				List<MyVisits> tileMap =  new ArrayList<MyVisits>();
	        
	        LocalDateTime currentTime = LocalDateTime.now();
			String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
			String date7DaysAhead = currentTime.plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
	        
			List<? extends Bson> pipeline = getMyvisitsPipeline(userName, date7DaysBack, true);
			
		    extractVisitsData(collection, tileMap, pipeline);
		    
		    pipeline = getMyvisitsPipeline(userName, date7DaysAhead, false);
			
		    extractVisitsData(collection, tileMap, pipeline);
		    
		    return tileMap;
			}catch(Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	private void extractVisitsData(MongoCollection<Document> collection, List<MyVisits> tileMap,
			List<? extends Bson> pipeline) {
		collection.aggregate(pipeline)
		        .allowDiskUse(false)
		        .forEach(new Consumer<Document>() {
		                @Override
		                public void accept(Document document) {
		                    logger.info(document.toJson());
							try {
								MyVisits mvVo= new ObjectMapper().readValue(document.toJson(), MyVisits.class);
								MyVisits myVisitsData=new MyVisits();
								List<MyVisitsIndustries> tVoList = new ArrayList<MyVisitsIndustries>();
								for(MyVisitsIndustries industries : mvVo.getVisitDetail()) {
									MyVisitsIndustries myVisitsIndustries = new MyVisitsIndustries();
									myVisitsIndustries.setIndustryName(industries.getIndustryName());
									myVisitsIndustries.setcScore((int)getCscore(industries.getIndustryId()));
									tVoList.add(myVisitsIndustries);
								}
								myVisitsData.setDateView(mvVo.getDateView());
								myVisitsData.setVisitDetail(tVoList);
								tileMap.add(myVisitsData);
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
		                    
		                }
		            }
		        );
	}
	public double getCscore(int industryId){

		Query query = new Query();
		query.limit(1);
		query.with(Sort.by(Sort.Direction.DESC,"_id"));
		query.addCriteria(Criteria.where("industryId").is(industryId));
		List<CScoreMaster> cScoreMasters = mongoTemplate.find(query, CScoreMaster.class);
		return cScoreMasters.get(0).getCscore();
	}
	private List<? extends Bson> getMyvisitsPipeline(String userName, String date, boolean pastVisits)
			throws ParseException {
		Document matchDoc = null;
		Document groupDoc = null;
		if(pastVisits) {
			matchDoc = new Document()
	        .append("$match", new Document()
	                .append("visitedDate", new Document()
	                		.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000"))
	                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date+" 00:00:00.000+0000"))
	                )
	                .append("userId", userName)
	        );
			groupDoc = new Document()
	        .append("$group", new Document()
	                .append("_id", "$visitedDate")
	                .append("industries", new Document()
	                        .append("$push", new Document()
	                                .append("industryName", "$industryName")
	                                .append("industryId", "$industryId")
	                        )
	                )
	        );
		}else
		{
			matchDoc = new Document()
			        .append("$match", new Document()
			                .append("schduledOn", new Document()
			                		.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date+" 00:00:00.000+0000"))
			                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000"))
			                )
			                .append("userId", userName)
			);
			groupDoc = new Document()
			        .append("$group", new Document()
			                .append("_id", "$schduledOn")
			                .append("industries", new Document()
			                        .append("$push", new Document()
			                                .append("industryName", "$industryName")
											.append("industryId", "$industryId")
			                        )
			                )
			);
		}
		
		List<? extends Bson> pipeline = Arrays.asList(
				matchDoc,    
				groupDoc,
				new Document()
                .append("$sort", new Document()
                        .append("_id", 1.0)
                ),
		        new Document()
		        .append("$project", new Document()
		                .append("_id", false)
		                .append("dateView", new Document()
		                        .append("$dateToString", new Document()
		                                .append("format", "%d")
		                                .append("date", "$_id")
		                        )
		                )
		                .append("visitDetail", "$industries")
		        )
		);
		return pipeline;
	}

	public List<TopPerfVo> getTopPerformer(String region){
		List<TopPerfVo> tVoList = new ArrayList<TopPerfVo>();
		try {
		 	MongoDatabase database = mongoClient.getDatabase("IDSS");
	        MongoCollection<Document> collection = database.getCollection("topPerformance");
			List<? extends Bson> pipeline = Arrays.asList(
					new Document()
	                .append("$sort", new Document()
	                        .append("regionTpScore", -1.0)
	                ),
					new Document().append("$limit", 4),
					new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("region", "$region")
		                    .append("rating", "$regionTpScore")
		            )
			);
			
			collection.aggregate(pipeline)
	        .allowDiskUse(false)
	        .forEach(new Consumer<Document>() {
	                @Override
	                public void accept(Document document) {
	                    logger.info(document.toJson());
						try {
							TopPerfVo tVo = new ObjectMapper().readValue(document.toJson(), TopPerfVo.class);
							double d = tVo.getRating()*5/100;
							double rating = (float)(Math.ceil(d * 4) / 4d);
							tVo.setRating(rating);
							tVoList.add(tVo);
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
	                    
	                }
	            }
	        );
		}catch(Exception e) {
			e.printStackTrace();
		}
		return tVoList;
	}
	
	public List<String> getIndustryTypes() {
		try {
			Query query = new Query();
			
			List<IndustryTypes> IndustryTypesList= mongoTemplate.find(query, IndustryTypes.class);
			
			List<String> industryTypes = IndustryTypesList.stream().map(IndustryTypes::getIndustryType).collect(Collectors.toList());
			
			return industryTypes;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}