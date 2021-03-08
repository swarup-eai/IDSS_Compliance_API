package com.eai.idss.dao;

import java.text.DecimalFormat;
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
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eai.idss.model.CScoreMaster;
import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.IndustryTypes;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.DashboardRequest;
import com.eai.idss.vo.HeatmapResponseVo;
import com.eai.idss.vo.IndustryCscoreResponseVo;
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

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Repository
public class GenericDaoImpl implements GenericDao {


	@Value("${dbName}")
	private String dbName;
	
	public static final Logger logger = Logger.getLogger(GenericDaoImpl.class);
	
	@Autowired
	MongoClient mongoClient;

	@Autowired
	MongoTemplate mongoTemplate;


	public Map<String,List<TileVo>> getConcentTileData(DashboardRequest dbr){
		try {
			logger.info("getConcentTileData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForDashboard();
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
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
										else {
											tNewVo.setCaseCount(tNewVo.getCaseCount() + tVo.getCaseCount());
											List<Integer> ind = tNewVo.getIndustries();
											if(null==ind) ind = new ArrayList<Integer>();
											ind.addAll(tVo.getIndustries());
											tNewVo.setIndustries(ind);
										}
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
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("legalDataMaster");
            
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            for(String days : daysMap.keySet()) {
            	logger.info("getLegalTileData : "+days);
            	List<TileVo> tVoList = new ArrayList<TileVo>();
	            List<? extends Bson> pipeline = getLegalTilePipeline(daysMap.get(days),dbr,"new");
	            
	            getLegalDataCount(collection, pipeline, tVoList);
	            
	            pipeline = getLegalTilePipeline(daysMap.get(days),dbr,"notice");
	            
	            getLegalDataCount(collection, pipeline, tVoList);
	            
	            pipeline = getLegalTilePipeline(daysMap.get(days),dbr,"actions");
	            
	            getLegalDataCount(collection, pipeline, tVoList);
	            
	            pipeline = getLegalTilePipeline(daysMap.get(days),dbr,"complied");
	            
	            getLegalDataCount(collection, pipeline, tVoList);
	            tileMap.put(days, tVoList);
            
            }
            return tileMap;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void getLegalDataCount(MongoCollection<Document> collection, List<? extends Bson> pipeline,
			List<TileVo> tVoList) {
		collection.aggregate(pipeline)
		        .allowDiskUse(false)
		        .forEach(new Consumer<Document>() {
		                @Override
		                public void accept(Document document) {
		                    logger.info(document.toJson());
							try {
								TileVo ltVo = new ObjectMapper().readValue(document.toJson(), TileVo.class);
								tVoList.add(ltVo);
							
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
		                    
		                }
		            }
		        );
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
		                        .append("industries", new Document().append("$addToSet", "$industryId"))
		                ),
		        new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("caseType", "$_id")
		                    .append("caseCount", "$caseCount")
		                    .append("industries", "$industries")
		            )
				);
		return pipeline;
	}
	
	private List<? extends Bson> getLegalTilePipeline(List<String> days,DashboardRequest dbr,String type) throws ParseException {
		Document matchDoc = new Document();
		matchDoc.append("issuedOn", new Document()
                        .append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
                		);
		matchDoc.append(type, new Document().append("$ne", 0));
		
		applyGenericFilter(dbr, matchDoc);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),    
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", null)
		                        .append("caseCount", new Document() .append("$sum", "$"+type))
		                        .append("industries", new Document().append("$addToSet", "$industryId"))
		                ),
		        new Document()
		            .append("$project", new Document()
		                    .append("_id", false)
		                    .append("caseType", type)
		                    .append("caseCount", "$caseCount")
		                    .append("industries", "$industries")
		            )
				);
		return pipeline;
	}

	private void applyGenericFilter(DashboardRequest dbr, Document matchDoc) {
		if(null!=dbr && StringUtils.hasText(dbr.getRegion()) && !"All".equalsIgnoreCase(dbr.getRegion())) 
			matchDoc.append("region", dbr.getRegion());
		if(null!=dbr && StringUtils.hasText(dbr.getSubRegion()) && !"All".equalsIgnoreCase(dbr.getSubRegion())) 
			matchDoc.append("subRegion", dbr.getSubRegion());
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
			
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection("Visit_master");
            
            
            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
            
            populateVisitsDone(dbr, daysMap, collection, tileMap);
            
            populateVisitsPlanned(dbr, futureVisitsMap, collection, tileMap);
            
            populateVisitsReports(dbr, daysMap, collection, tileMap);
            
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
                        .append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
                        .append("$gt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
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
                        .append("industries", new Document().append("$addToSet", "$industryId"))
                ),
        new Document()
            .append("$project", new Document()
                    .append("_id", false)
                    .append("caseType", "planned")
                    .append("caseCount", "$caseCount")
                    .append("industries", "$industries")
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
                        .append("industries", new Document().append("$addToSet", "$industryId"))
                ),
        new Document()
            .append("$project", new Document()
                    .append("_id", false)
                    .append("caseType", "visited")
                    .append("caseCount", "$caseCount")
                    .append("industries", "$industries")
            )
		);
		return pipeline;
	}
	
	private void populateVisitsReports(DashboardRequest dbr, Map<String, List<String>> daysMap, MongoCollection<Document> collection, Map<String, List<TileVo>> tileMap) throws ParseException {
		for(String days : daysMap.keySet()) {
			logger.info("getVisitsTileData : "+days);
		    List<? extends Bson> pipeline = getVisitsReportsTilePipeline(daysMap.get(days),dbr);
		    
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
	
	private List<? extends Bson> getVisitsReportsTilePipeline(List<String> days,DashboardRequest dbr) throws ParseException {
		Document matchDoc = new Document();
		matchDoc.append("visitedDate", new Document()
                        .append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
                		);
		matchDoc.append("visitReportFile",new Document().append("$ne","NA"));
		
		applyGenericFilter(dbr, matchDoc);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),    
				new Document()
                .append("$group", new Document()
                        .append("_id", "null")
                        .append("caseCount", new Document()
                                .append("$sum", 1)
                        )
                        .append("industries", new Document().append("$addToSet", "$industryId"))
                ),
        new Document()
            .append("$project", new Document()
                    .append("_id", false)
                    .append("caseType", "Reports")
                    .append("caseCount", "$caseCount")
                    .append("industries", "$industries")
            )
		);
		return pipeline;
	}
	
	public List<MyVisits> getMyVisitsData(String userName){
			logger.info("getMyVisitsData");
			try {
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
	        MongoCollection<Document> collection = database.getCollection("Visit_master");

				List<MyVisits> tileMap =  new ArrayList<MyVisits>();
	        
	        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
	        LocalDateTime date = currentTime.now().withDayOfMonth(1);
	        LocalDateTime date1 = currentTime.with(lastDayOfMonth());
			String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
			String date7DaysAhead = currentTime.plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
			String today = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
			String visitedDateFromDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
			String scheduledOnToDate = date1.format(DateTimeFormatter.ISO_LOCAL_DATE);

				List<? extends Bson> pipeline = getMyvisitsPipeline(userName, visitedDateFromDate,today, true);
			
		    extractVisitsData(collection, tileMap, pipeline);
		    
		    pipeline = getMyvisitsPipeline(userName, visitedDateFromDate,scheduledOnToDate, false);
			
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
									myVisitsIndustries.setIndustryId(industries.getIndustryId());
									myVisitsIndustries.setIndustryName(industries.getIndustryName());
									myVisitsIndustries.setVisitStatus(industries.getVisitStatus());
									myVisitsIndustries.setcScore((int)getCscore(industries.getIndustryId()));
									myVisitsIndustries.setVisitId(industries.getVisitId());
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
		if(null==cScoreMasters || cScoreMasters.isEmpty()) return 0;
		return cScoreMasters.get(0).getCscore();
	}
	private List<? extends Bson> getMyvisitsPipeline(String userName, String fromDate, String toDate, boolean visited)
			throws ParseException {
		Document matchDoc = null;
		Document groupDoc = null;
		if(visited) {
			matchDoc = new Document()
	        .append("$match", new Document()
	                .append("visitedDate", new Document()
	                		.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDate+" 00:00:00.000+0000"))
	                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDate+" 00:00:00.000+0000"))
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
	                                .append("visitStatus", "$visitStatus")
	                                .append("visitId", "$visitId")
	                        )
	                )
	        );
		}else
		{
			matchDoc = new Document()
			        .append("$match", new Document()
			                .append("schduledOn", new Document()
			                		.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDate+" 00:00:00.000+0000"))
			                        .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDate+" 00:00:00.000+0000"))
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
											.append("visitStatus", "$visitStatus")
											.append("visitId", "$visitId")
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
		 	MongoDatabase database = mongoClient.getDatabase(dbName);
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
	
	public List<String> getIndustryTypes(String category) {
		try {
			Query query = new Query();
			
			if(StringUtils.hasText(category)) {
				String catPrefix = category.substring(0, 1).toUpperCase();
				query.addCriteria(Criteria.where("industryType").regex(catPrefix));
			}
			
			List<IndustryTypes> IndustryTypesList= mongoTemplate.find(query, IndustryTypes.class);
			
			List<String> industryTypes = IndustryTypesList.stream().map(IndustryTypes::getIndustryType).collect(Collectors.toList());
			
			return industryTypes;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Map<String,List<HeatmapResponseVo>> getHeatmapData(DashboardRequest dbr) {
		try {
			logger.info("getHeatmapData");
			Map<String, List<String>> daysMap = IDSSUtil.getDaysMapForDashboard();


			MongoDatabase database = mongoClient.getDatabase(dbName);
			MongoCollection<Document> collection = database.getCollection("industryMaster");
			Map<String,List<HeatmapResponseVo>> heatmapData = new LinkedHashMap<String, List<HeatmapResponseVo>>();

			for(String days : daysMap.keySet()) {
				logger.info("getConcentTileData : " + days);
				List<? extends Bson> pipeline = getHeatmapPipline(daysMap.get(days), dbr);

				List<HeatmapResponseVo> heatmapResponseList = new ArrayList<HeatmapResponseVo>();

				collection.aggregate(pipeline)
						.allowDiskUse(false)
						.forEach(new Consumer<Document>() {
									 @Override
									 public void accept(Document document) {
										 try {
											 ObjectMapper mapper = new ObjectMapper();

											 HeatmapResponseVo tVo = mapper.readValue(document.toJson(), HeatmapResponseVo.class);

											 heatmapResponseList.add(tVo);
										 } catch (JsonMappingException e) {
											 e.printStackTrace();
										 } catch (JsonProcessingException e) {
											 e.printStackTrace();
										 }
									 }
								 }
						);
				if(heatmapResponseList.size()>0)
					heatmapData.put(days, heatmapResponseList);
			}
			return heatmapData;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<HeatmapResponseVo> getHeatmapDataByIndustryIds(List<Integer> industryIds) {
		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("industryId").in(industryIds));

			List<IndustryMaster> industryMasterList= mongoTemplate.find(query, IndustryMaster.class);

			List<HeatmapResponseVo> industryTypes =  industryMasterList.stream().map(data -> {
				HeatmapResponseVo heatmapResponseVo = new HeatmapResponseVo();
				heatmapResponseVo.setIndustryName(data.getIndustryName());
				heatmapResponseVo.setLatitude(data.getLatitudeDegree());
				heatmapResponseVo.setLongitude(data.getLongitudeDegree());
				return heatmapResponseVo;
			}).collect(Collectors.toList());
			return industryTypes;

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<IndustryCscoreResponseVo> getIndustryScore(DashboardRequest dbr) {
		try {
			logger.info("getIndustryScore");
			MongoDatabase database = mongoClient.getDatabase(dbName);
			MongoCollection<Document> collection = database.getCollection("industryMaster");

			List<? extends Bson> pipeline = getIndustryCscorePipline(dbr);

			List<IndustryCscoreResponseVo> industryCscoreResponseVoArrayList = new ArrayList<IndustryCscoreResponseVo>();

			collection.aggregate(pipeline)
					.allowDiskUse(false)
					.forEach(new Consumer<Document>() {
								 @Override
								 public void accept(Document document) {
									 try {
										 ObjectMapper mapper = new ObjectMapper();

										 IndustryCscoreResponseVo industryCscoreResponseVo = mapper.readValue(document.toJson(), IndustryCscoreResponseVo.class);
										 String fill = "#eff3ff";
										if(25 >=industryCscoreResponseVo.getcScoreAverage()){
											fill = "#eff3ff";
										}else if(50 >=industryCscoreResponseVo.getcScoreAverage()){
											fill = "#bdd7e7";
										}else if(75>=industryCscoreResponseVo.getcScoreAverage()){
											fill = "#6baed6";
										}else if(100 >=industryCscoreResponseVo.getcScoreAverage()){
											fill = "#2171b5";
										}
										 industryCscoreResponseVo.setFill(fill);
										 industryCscoreResponseVo.setcScoreAverage(Double.parseDouble(new DecimalFormat("##.##").format(industryCscoreResponseVo.getcScoreAverage())));

										 industryCscoreResponseVoArrayList.add(industryCscoreResponseVo);
									 } catch (JsonMappingException e) {
										 e.printStackTrace();
									 } catch (JsonProcessingException e) {
										 e.printStackTrace();
									 }
								 }
							 }
					);
			return industryCscoreResponseVoArrayList;
		}
		catch(Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		}
		return null;
	}

	private List<IndustryMaster> getIndustryCscoreByRegion(String region){
		Query query = new Query();
		query.addCriteria(Criteria.where("region").is(region));
		return mongoTemplate.find(query, IndustryMaster.class);
	}

	private List<? extends Bson> getHeatmapPipline(List<String> days,DashboardRequest dbr) throws ParseException {
		Document matchDoc = new Document();
		matchDoc.append("applicationCreatedOn", new Document()
				.append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(0)+" 00:00:00.000+0000"))
				.append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days.get(1)+" 00:00:00.000+0000"))
		);
		applyGenericFilter(dbr, matchDoc);
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),
				new Document()
						.append("$project", new Document()
								.append("_id", false)
								.append("industryName", "$industryName")
								.append("latitude", "$latitudeDegree")
								.append("longitude", "$longitudeDegree")
						)
		);
		return pipeline;
	}

	private List<? extends Bson> getIndustryCscorePipline(DashboardRequest dbr) throws ParseException {

		Document matchDoc = new Document();
		matchDoc.append("cscore", new Document()
				.append("$gte", 0)
		);

		if(null!=dbr && StringUtils.hasText(dbr.getRegion()) && !"All".equalsIgnoreCase(dbr.getRegion()))
			matchDoc.append("region", dbr.getRegion());

		Document groupDoc = new Document();
		groupDoc.append("_id", "$region")
				.append("value",  new Document()
						.append("$avg", "$cscore")
				);
		if(null!=dbr && StringUtils.hasText(dbr.getRegion()) && !"All".equalsIgnoreCase(dbr.getRegion()))
			groupDoc.append("_id", "$subRegion")
				.append("value",  new Document()
						.append("$avg", "$cscore")
		);
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),
				new Document().append("$group", groupDoc),

//				new Document()
//						.append("$group", new Document()
//								.append("_id", "$region")
//										.append("value",  new Document()
//												.append("$avg", "$cscore")
//										)
//								.append("value",  new Document()
//										.append("$sum", "$cscore")
//								)
//								.append("count",  new Document()
//										.append("$sum", 1)
//								)
//						),
				new Document()
						.append("$project", new Document()
								.append("_id", false)
								.append( "id","$_id")
								.append("cScoreAverage", "$value")
//								.append("count", "$count")

						)
		);
		return pipeline;
	}
}