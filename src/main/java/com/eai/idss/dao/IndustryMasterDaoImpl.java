package com.eai.idss.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;
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

import com.eai.idss.model.Annual_Returns_HW_Comparison;
import com.eai.idss.model.BMW_Annual_Return_Comparison;
import com.eai.idss.model.BMW_Authorization_Comparison;
import com.eai.idss.model.Battery_Dealer_Annual_return_Form_V;
import com.eai.idss.model.CScoreMaster;
import com.eai.idss.model.Consent_EFFLUENT_Comparison;
import com.eai.idss.model.Consent_FUEL_comparison;
import com.eai.idss.model.Consent_HW_Comparison;
import com.eai.idss.model.Consent_RESOURCES_comparison;
import com.eai.idss.model.Consent_SKU_comparison;
import com.eai.idss.model.Consent_STACK_comparison;
import com.eai.idss.model.Consent_WATER_comparison;
import com.eai.idss.model.Consented_Air_Pollution_Comparison;
import com.eai.idss.model.Directions;
import com.eai.idss.model.ESR_Air_Pollution_Comparison;
import com.eai.idss.model.ESR_EFFLUENT_Comparison;
import com.eai.idss.model.ESR_FUEL_comparison;
import com.eai.idss.model.ESR_RESOURCES_comparison;
import com.eai.idss.model.ESR_SKU_comparison;
import com.eai.idss.model.ESR_WATER_comparison;
import com.eai.idss.model.EWasteAnnualAuthorization;
import com.eai.idss.model.EWasteAnnualReturns;
import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.Legal;
import com.eai.idss.model.Plastic_Annual_Report_Local_Body_Form_V;
import com.eai.idss.model.Plastic_Annual_Report_Recycling_Facility_Form_IV;
import com.eai.idss.model.Plastic_Brand_Owner_Authorization;
import com.eai.idss.model.Plastic_Producer_Authorization;
import com.eai.idss.model.Plastic_Raw_Material_Manufacturer_Authorization;
import com.eai.idss.model.Plastic_Recycler_Authorization;
import com.eai.idss.model.Visits;
import com.eai.idss.repository.IndustryMasterRepository;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.AnnualReturnsVo;
import com.eai.idss.vo.BatteriesSoldToVo;
import com.eai.idss.vo.BatteryVo;
import com.eai.idss.vo.BioMedWasteAuthFormVo;
import com.eai.idss.vo.BioMedWasteVo;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.ComparisonTableParamGroupVo;
import com.eai.idss.vo.ComparisonTableResponseVo;
import com.eai.idss.vo.ComplianceScoreResponseVo;
import com.eai.idss.vo.EWasteForm4Vo;
import com.eai.idss.vo.EWasteVo;
import com.eai.idss.vo.Form1AVo;
import com.eai.idss.vo.Form3Vo;
import com.eai.idss.vo.Form5Vo;
import com.eai.idss.vo.IndustryMasterDetailResponseVo;
import com.eai.idss.vo.IndustryMasterRequest;
import com.eai.idss.vo.MandatoryReportsResponseVo;
import com.eai.idss.vo.NewBatteriesSoldVo;
import com.eai.idss.vo.OldUsedBatteriesVo;
import com.eai.idss.vo.ParameterVo;
import com.eai.idss.vo.PlasticAuthorizationFormVo;
import com.eai.idss.vo.PlasticForm4Vo;
import com.eai.idss.vo.PlasticVo;
import com.eai.idss.vo.PollutionScoreFilter;
import com.eai.idss.vo.PollutionScoreResponseVo;
import com.eai.idss.vo.PollutionScoreValueVo;
import com.eai.idss.vo.SKU;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


@Repository
public class IndustryMasterDaoImpl implements IndustryMasterDao {
	

	@Value("${dbName}")
	private String dbName;
	
	private static final String OCEMS = "OCEMS";

	private static final String BIO_MED_WASTE = "BioMedWaste";

	private static final String PLASTIC = "Plastic";

	private static final String BATTERY = "Battery";

	private static final String EWASTE = "EWASTE";

	private static final String HAZ_WASTE = "HAZ_WASTE";

	private static final String ESR = "ESR";

	private static final String CONSENT = "CONSENT";

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoClient mongoClient;

	@Autowired
	IndustryMasterRepository industryMasterRepository;
	
	public static final Logger logger = Logger.getLogger(IndustryMasterDaoImpl.class);
	
	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page){
		
		
		Query query = new Query().with(page);
		setCriteria(imr, query);

		logger.info("Total Count="+mongoTemplate.count(query, IndustryMaster.class));
		
		List<IndustryMaster> filteredIndustryMaster = mongoTemplate.find(query, IndustryMaster.class);
		
		if(null!=filteredIndustryMaster && !filteredIndustryMaster.isEmpty()) {
		
			filterForLegalActions(imr, filteredIndustryMaster);
			
			filterForPendingCases(imr, filteredIndustryMaster);
			
			logger.info("Filter Count="+filteredIndustryMaster.size());
			
			Page<IndustryMaster> imPage = PageableExecutionUtils.getPage(filteredIndustryMaster, page,
			        () -> mongoTemplate.count(query, IndustryMaster.class));
			
			for(IndustryMaster im : imPage) {
				populateLegalActionsPending(im);
				
				populateLastVisited(im);
			}
			
			return imPage.toList();
		}
		return new ArrayList<>();
	}

	private void filterForPendingCases(IndustryMasterRequest imr, List<IndustryMaster> filteredIndustryMaster) {
		if(StringUtils.hasText(imr.getPendingCases()) && !"All".equalsIgnoreCase(imr.getPendingCases())) {
			
			List<Long> indIdList = filteredIndustryMaster.stream().map(IndustryMaster::getIndustryId).collect(Collectors.toList());
			
			List<Long> ldmIndIdList = getDirectionsIndustryId(indIdList,imr.getPendingCases());
			
			filteredIndustryMaster.removeIf(i -> !ldmIndIdList.contains(i.getIndustryId()));
		}
	}
	
	private List<Long> getDirectionsIndustryId(List<Long> indIdList,String directionsCnt){
		try {
			Document matchDocDirCnt =new Document();
			if(directionsCnt.contains("+")) {
				String dc = directionsCnt.replace("+", "");
				matchDocDirCnt.append("count",new Document().append("$gte", Integer.parseInt(dc)));
			}
			else 
				matchDocDirCnt.append("count", Integer.parseInt(directionsCnt));
			
			Document matchDoc = new Document();
			matchDoc.append("industryId", new Document().append("$in", indIdList));
			
			matchDoc.append("statusUpdatedOn", new Document()
	        		.append("$eq", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse("1970-01-01 00:00:00.000+0000")));
	        
			
			List<Long> pList = new ArrayList<Long>();
			List<? extends Bson> pipeline = Arrays.asList(
						new Document().append("$match", matchDoc),  
						new Document()
			            .append("$group", new Document()
			                    .append("_id", "$industryId")
			                    .append("count",  new Document()
		                                .append("$sum", 1)
		                                )
			            ),
			            new Document().append("$match", matchDocDirCnt),
						new Document()
			            .append("$project", new Document()
			                    .append("_id", false)
			                    .append("industryId", "$_id")
			            )
			            
					);
			MongoDatabase database = mongoClient.getDatabase(dbName);
	        MongoCollection<Document> collection = database.getCollection("Directions");
	
	        collection.aggregate(pipeline)
	        .allowDiskUse(false)
	        .forEach(new Consumer<Document>() {
	                @Override
	                public void accept(Document document) {
	                    logger.info(document.toJson());
						try {
							IndustryMaster dc = (new ObjectMapper().readValue(document.toJson(), IndustryMaster.class));
							pList.add(dc.getIndustryId());
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
	                    
	                }
	            }
	        );
			return pList;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setCriteria(IndustryMasterRequest imr, Query query) {
		if(null!=imr) {
			if(StringUtils.hasText(imr.getRegion()) && !"All".equalsIgnoreCase(imr.getRegion()))
				query.addCriteria(Criteria.where("region").is(imr.getRegion()));
			if(StringUtils.hasText(imr.getSubRegion()) && !"All".equalsIgnoreCase(imr.getSubRegion()))
				query.addCriteria(Criteria.where("subRegion").is(imr.getSubRegion()));
			if(StringUtils.hasText(imr.getCategory()) && !"All".equalsIgnoreCase(imr.getCategory()))
				query.addCriteria(Criteria.where("category").is(imr.getCategory()));
			if(StringUtils.hasText(imr.getScale()) && !"All".equalsIgnoreCase(imr.getScale()))
				query.addCriteria(Criteria.where("scale").is(imr.getScale()));
			if(StringUtils.hasText(imr.getType()) && !"All".equalsIgnoreCase(imr.getType()))
				query.addCriteria(Criteria.where("type").is(imr.getType()));
			if(StringUtils.hasText(imr.getComplianceScore()) && !"All".equalsIgnoreCase(imr.getComplianceScore())) {
				if(imr.getComplianceScore().contains("-")) {
					String[] csString = imr.getComplianceScore().split("-");
					query.addCriteria(Criteria.where("cscore")
							.gte(Integer.parseInt(csString[0]))
							.lte(Integer.parseInt(csString[1])));
				}
			}
		}
	}

	private void populateLastVisited(IndustryMaster im) {
		Query queryVisit = new Query();
		queryVisit.addCriteria(Criteria.where("industryId").is(im.getIndustryId()));
		queryVisit.with(Sort.by(Sort.Direction.DESC,"visitId"));
		queryVisit.limit(1);
		List<Visits> visitObj = mongoTemplate.find(queryVisit, Visits.class);
		if(null!=visitObj && visitObj.size()>0)
			im.setLastVisited(visitObj.get(0).getVisitedDate());
	}

	private Query populateLegalActionsPending(IndustryMaster im) {
		Query queryLDM = new Query();
		queryLDM.addCriteria(Criteria.where("industryId").is(im.getIndustryId()));
		List<Legal> ldmObj = mongoTemplate.find(queryLDM, Legal.class);
		if(null!=ldmObj && ldmObj.size()>0) {
			int lap = (int)(ldmObj.get(0).getTotalLegalActionsCreated()-ldmObj.get(0).getTotalDirections());
			im.setLegalActionsPending(lap>0?lap:0);
			im.setTotalLegalActions((int)ldmObj.get(0).getTotalLegalActionsCreated());
		}
		return queryLDM;
	}

	private void filterForLegalActions(IndustryMasterRequest imr, List<IndustryMaster> filteredIndustryMaster) {
		if(StringUtils.hasText(imr.getLegalActions()) && !"All".equalsIgnoreCase(imr.getLegalActions()) && !"0".equalsIgnoreCase(imr.getLegalActions())) {
			List<Long> indIdList = filteredIndustryMaster.stream().map(IndustryMaster::getIndustryId).collect(Collectors.toList());
			Query queryLDM = new Query();
			
			if(imr.getLegalActions().contains("+")) {
				String csString = imr.getLegalActions().replace("+", "");
				queryLDM.addCriteria(Criteria.where("totalLegalActionsCreated").gte(Integer.parseInt(csString)));
			}else {
				queryLDM.addCriteria(Criteria.where("totalLegalActionsCreated").is(Integer.parseInt(imr.getLegalActions())));
			}

			queryLDM.addCriteria(Criteria.where("industryId").in(indIdList));
			List<Legal> ldmList = mongoTemplate.find(queryLDM, Legal.class);
			List<Long> ldmIndIdList = ldmList.stream().map(Legal::getIndustryId).collect(Collectors.toList());
			
			
			filteredIndustryMaster.removeIf(i -> !ldmIndIdList.contains(i.getIndustryId()));
		}
	}

	public List<ComplianceScoreResponseVo> getByIndustryIdComplianceScoreData(ComlianceScoreFilter cf) {
		try {

			logger.info("getComplainceScoreCardData");

			if (null != cf) {
				List<ComplianceScoreResponseVo> csrList = new ArrayList<ComplianceScoreResponseVo>();

					Query query = new Query();

					query.addCriteria(Criteria.where("calculatedDate")
							.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(cf.getFromDate() + " 00:00:00.000+0000"))
							.lt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(cf.getToDate() + " 00:00:00.000+0000")));

					query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

					List<CScoreMaster> csList = mongoTemplate.find(query, CScoreMaster.class);
					
					for(CScoreMaster csm : csList) {
						ComplianceScoreResponseVo csrVo = new ComplianceScoreResponseVo();
						csrVo.setCalculateddate(csm.getCalculatedDate());
						csrVo.setcScore((int)csm.getCscore());
						csrVo.setIndustryId(csm.getIndustryId());
						YearMonth ym = YearMonth.of(csm.getCalculatedDate().getYear(),csm.getCalculatedDate().getMonth());
						DateTimeFormatter f = DateTimeFormatter.ofPattern( "MMM-uuuu" ) ;
						csrVo.setMonthYear(ym.format( f )) ;
						
						List<Visits> vList = getVisitDetails(cf, csm);
						if(null!=vList && !vList.isEmpty()) {
							csrVo.setVisitId(vList.get(0).getVisitId());
							csrVo.setVisitDate(vList.get(0).getVisitedDate());
						}
						
						List<Legal> lList = getLegalActionDetails(csm.getIndustryId(), csm.getCalculatedDate());
						if(null!=lList && !lList.isEmpty()) 
							csrVo.setLegalActionsCnt((int)lList.get(0).getTotalLegalActionsCreated());
						
						csrList.add(csrVo);
					}
					return csrList;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Visits> getVisitDetails(ComlianceScoreFilter cf, CScoreMaster csm) throws ParseException {
		LocalDate fd = csm.getCalculatedDate().withDayOfMonth(1);
		LocalDate ld = csm.getCalculatedDate().withDayOfMonth(csm.getCalculatedDate().lengthOfMonth());
		Query queryVisits = new Query();

		queryVisits.addCriteria(Criteria.where("visitedDate")
				.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fd.format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000"))
				.lt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(ld.format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000")));

		queryVisits.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
		
		queryVisits.addCriteria(Criteria.where("visitStatus").is("Visited"));
		
		List<Visits> vList = mongoTemplate.find(queryVisits, Visits.class);
		return vList;
	}
	
	private List<Legal> getLegalActionDetails(long industryId, LocalDate visitDate) throws ParseException {
		Query queryLegal = new Query();

		LocalDate fd = visitDate.withDayOfMonth(1);
		LocalDate ld = visitDate.withDayOfMonth(visitDate.lengthOfMonth());
		
		queryLegal.addCriteria(Criteria.where("issuedOn")
				.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fd.format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000"))
				.lt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(ld.format(DateTimeFormatter.ISO_LOCAL_DATE)+" 00:00:00.000+0000")));

		queryLegal.addCriteria(Criteria.where("industryId").is(industryId));
		
		List<Legal> lList = mongoTemplate.find(queryLegal, Legal.class);
		return lList;
	}

	@Override
	public  List<Map<String,String>> getPollutionScoreData(PollutionScoreFilter cf) {

		logger.info("getPollutionScoreData..."+cf.getIndustryId());
		List<PollutionScoreResponseVo> responseList = new ArrayList<PollutionScoreResponseVo>();

		
		for(String param : cf.getParametersList()) {
			String[] pa = param.split("~~");
			StringBuilder formType = null;
			StringBuilder paramName = null;
			StringBuilder collectionName = null;
			StringBuilder paramColumnName = null;
			StringBuilder valueColumnName = null;
			if(pa.length==4) {
				formType = new StringBuilder(pa[0]);
				paramName = new StringBuilder(pa[1]);
				collectionName = new StringBuilder(pa[2]);
				valueColumnName = new StringBuilder(pa[3]);
			}else if(pa.length==5) {
				formType = new StringBuilder(pa[0]);
				paramName = new StringBuilder(pa[1]);
				collectionName = new StringBuilder(pa[2]);
				paramColumnName = new StringBuilder(pa[3]);
				valueColumnName = new StringBuilder(pa[4]);
			}
			
			PollutionScoreResponseVo psrVo = new PollutionScoreResponseVo();
			psrVo.setForm(formType.toString());
			psrVo.setParam(paramName.toString());
			List<PollutionScoreValueVo> psvList = new ArrayList<PollutionScoreValueVo>();
			if(OCEMS.equalsIgnoreCase(formType.toString())) {
				psvList = getOCEMSPollutionScoreValue(formType.toString(),"industry_mis_id",cf.getIndustryId(),collectionName.toString(),
						null!=paramColumnName?paramColumnName.toString():null,paramName.toString(),valueColumnName.toString(),cf.getFromDate(),cf.getToDate());
			}else {
				psvList = getPollutionScoreValue(formType.toString(),"industryId",cf.getIndustryId(),collectionName.toString(),
						null!=paramColumnName?paramColumnName.toString():null,paramName.toString(),valueColumnName.toString(),cf.getFromDate(),cf.getToDate());

				Integer fromYear =  Integer.parseInt( cf.getFromDate().substring(0, cf.getFromDate().indexOf("-")));
				Integer toYear =  Integer.parseInt( cf.getToDate().substring(0, cf.getToDate().indexOf("-")));
				while(fromYear<=toYear) {
					StringBuffer fY = new StringBuffer(String.valueOf(fromYear));
					if(!psvList.stream().filter(p -> p.getYear().equalsIgnoreCase(fY.toString())).findAny().isPresent()) {
						PollutionScoreValueVo pvv = new PollutionScoreValueVo(String.valueOf(fromYear),0);
						psvList.add(pvv);
					}
					fromYear++;
				}
			}
			
			psrVo.setPsv(psvList.stream().sorted((p1,p2) -> p1.getYear().compareTo(p2.getYear())).collect(Collectors.toList()));
			
			responseList.add(psrVo);
		}
		
		List<Map<String,String>> lms = new ArrayList<Map<String,String>>();
		for(PollutionScoreResponseVo rv : responseList) {
			List<PollutionScoreValueVo> pvL =   rv.getPsv();
			for(PollutionScoreValueVo pVO : pvL) {
				Map<String,String> msd = new LinkedHashMap<String, String>();
				msd.put("date", pVO.getYear());
				msd.put(rv.getForm()+"~~"+rv.getParam(), String.valueOf(pVO.getValue()));
				lms.add(msd);
			}
		}

		return lms;
	}
	
	private List<PollutionScoreValueVo> getOCEMSPollutionScoreValue(String formType,String industryIdentifier, long industryId,String collectionName,
			String paramField,String paramValue,String valueField,String fromDate,String toDate) {
		try {
			String dateColumnName = IDSSUtil.getDateColumnName(formType);
			
			Document matchDoc = new Document();
			matchDoc.append(industryIdentifier, industryId);
			if(StringUtils.hasText(paramField))
				matchDoc.append(paramField, paramValue);
			
			matchDoc.append(dateColumnName, new Document()
	        		.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDate+" 00:00:00.000+0000"))
	                .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDate+" 00:00:00.000+0000")));
	        
			
			List<PollutionScoreValueVo> pList = new ArrayList<PollutionScoreValueVo>();
			List<? extends Bson> pipeline = Arrays.asList(
						new Document().append("$match", matchDoc),  
		                new Document()
			            .append("$project", new Document()
			                    .append("_id", false)
			                    .append("day", new Document()
				                        .append("$dateToString", new Document()
				                                .append("format", "%Y-%m-%d")
				                                .append("date", "$"+dateColumnName)
				                        )
				                )
			                    .append("value", "$"+valueField)
			            ),
			            new Document()
			            .append("$group", new Document()
			                    .append("_id", "$day")
			                    .append("value",  new Document()
		                                .append("$avg", "$value")
		                                )
			            ),
			            new Document()
			            .append("$sort", new Document()
			                    .append("_id", 1)
			            ),
			            new Document()
			            .append("$project", new Document()
			                    .append("_id", false)
			                    .append( "monthYear","$_id")
			                    .append("value", "$value")
			            )
					);
			MongoDatabase database = mongoClient.getDatabase(dbName);
	        MongoCollection<Document> collection = database.getCollection(collectionName);
	
	        collection.aggregate(pipeline)
	        .allowDiskUse(false)
	        .forEach(new Consumer<Document>() {
	                @Override
	                public void accept(Document document) {
	                    logger.info(document.toJson());
						try {
							PollutionScoreValueVo pVo = (new ObjectMapper().readValue(document.toJson(), PollutionScoreValueVo.class));
							pList.add(pVo);
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
	                    
	                }
	            }
	        );
			return pList;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<PollutionScoreValueVo> getPollutionScoreValue(String formType,String industryIdentifier, long industryId,String collectionName,
			String paramField,String paramValue,String valueField,String fromDate,String toDate) {
		try {
			String dateColumnName = IDSSUtil.getDateColumnName(formType);
			
			Document matchDoc = new Document();
			matchDoc.append(industryIdentifier, industryId);
			if(StringUtils.hasText(paramField))
				matchDoc.append(paramField, paramValue);
			
			matchDoc.append(dateColumnName, new Document()
	        		.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDate+" 00:00:00.000+0000"))
	                .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDate+" 00:00:00.000+0000")));
	        
			
			List<PollutionScoreValueVo> pList = new ArrayList<PollutionScoreValueVo>();
			List<? extends Bson> pipeline = Arrays.asList(
						new Document().append("$match", matchDoc),  
		                new Document()
			            .append("$project", new Document()
			                    .append("_id", false)
			                    .append("year", new Document()
				                        .append("$dateToString", new Document()
				                                .append("format", "%Y")
				                                .append("date", "$"+dateColumnName)
				                        )
				                )
			                    .append("value", "$"+valueField)
			            )
					);
			MongoDatabase database = mongoClient.getDatabase(dbName);
	        MongoCollection<Document> collection = database.getCollection(collectionName);
	
	        collection.aggregate(pipeline)
	        .allowDiskUse(false)
	        .forEach(new Consumer<Document>() {
	                @Override
	                public void accept(Document document) {
	                    logger.info(document.toJson());
						try {
							PollutionScoreValueVo pVo = (new ObjectMapper().readValue(document.toJson(), PollutionScoreValueVo.class));
							pList.add(pVo);
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
	                    
	                }
	            }
	        );
			return pList;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ComparisonTableResponseVo getComparisonData(long industryId,int consentYear,int esrYear,int form4Year) {

		logger.info("getComparisonData..."+industryId+", consentYear: "+consentYear+", esrYear: "+esrYear);
		ComparisonTableResponseVo psrVo = new ComparisonTableResponseVo();
		Map<String,List<ComparisonTableParamGroupVo>> mapPGVo = new LinkedHashMap<String, List<ComparisonTableParamGroupVo>>();
		
		mapPGVo.put("production",getProductionComparisonData(industryId,consentYear,esrYear, form4Year));
		mapPGVo.put("resources",getResourcesComparisonData(industryId,consentYear,esrYear));
		mapPGVo.put("pollution",getPollutionComparisonData(industryId,consentYear,esrYear));
		mapPGVo.put("stack",getStackComparisonData(industryId,consentYear,esrYear));
		
		psrVo.setComparisonTable(mapPGVo);
		return psrVo;
	}
	
	private List<ComparisonTableParamGroupVo> getStackComparisonData(long industryId,int consentYear,int esrYear) {

		List<ComparisonTableParamGroupVo> ppgVoList = new ArrayList<ComparisonTableParamGroupVo>();
		ppgVoList.add(getStackData(industryId,consentYear,esrYear));
		return ppgVoList;
	}
	
	private ComparisonTableParamGroupVo getStackData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_STACK_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_STACK_comparison.class);
		ComparisonTableParamGroupVo ppgVo = new ComparisonTableParamGroupVo();
		ppgVo.setParam("Stack");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_STACK_comparison csc : cscList) {
			SKU sku1 = new SKU("Stack Number",csc.getStackNumber(),"");
			cSKUList.add(sku1);
			SKU sku2 = new SKU("Stack Fuel",csc.getStackFuelType(),"");
			cSKUList.add(sku2);
			SKU sku3 = new SKU("Stack Attached To",csc.getStackAttachedTo(),"");
			cSKUList.add(sku3);
			SKU sku4 = new SKU("Stack Pollutants",csc.getStackNatureOfPollutants(),"");
			cSKUList.add(sku4);
		}
		ppgVo.setConsentSKU(cSKUList);
		
		return ppgVo;
	}


	private List<ComparisonTableParamGroupVo> getPollutionComparisonData(long industryId,int consentYear,int esrYear) {

		List<ComparisonTableParamGroupVo> ppgVoList = new ArrayList<ComparisonTableParamGroupVo>();
		
		ppgVoList.add(getAirData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getWaterData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getFuelData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getHazWasteData(industryId,consentYear,esrYear));
		
//		ppgVoList.add(geteWasteData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getEffluentData(industryId,consentYear,esrYear));
		
		return ppgVoList;
	}
	
	private ComparisonTableParamGroupVo getEffluentData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_EFFLUENT_Comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_EFFLUENT_Comparison.class);
		ComparisonTableParamGroupVo ppgVo = new ComparisonTableParamGroupVo();
		ppgVo.setParam("Effluent");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_EFFLUENT_Comparison csc : cscList) {
			SKU skuE = new SKU("ETP",String.valueOf(csc.getCapacityOfEtp()),csc.getName());
			cSKUList.add(skuE);
			SKU skuS = new SKU("STP",String.valueOf(csc.getCapacityOfStp()),csc.getName());
			cSKUList.add(skuS);
		}
		ppgVo.setConsentSKU(cSKUList);
		
		List<ESR_EFFLUENT_Comparison> escList = mongoTemplate.find(getESRQueryObj(industryId, esrYear), ESR_EFFLUENT_Comparison.class);
		List<SKU> eSKUList = new ArrayList<SKU>();
		for(ESR_EFFLUENT_Comparison esc : escList) {
			SKU sku = new SKU(esc.getEffluentParticulars(),String.valueOf(esc.getEffluentParticularsQuantityActual()),esc.getName());
			eSKUList.add(sku);
		}
		ppgVo.setEsrSKU(eSKUList);
		return ppgVo;
	}
	
//	private PollutionParamGroupVo geteWasteData(long industryId,int consentYear,int esrYear) {
//		
//		List<EWasteAnnualReturns> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), EWasteAnnualReturns.class);
//		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
//		ppgVo.setParam("eWaste");
//		List<SKU> cSKUList = new ArrayList<SKU>();
//		for(EWasteAnnualReturns csc : cscList) {
//			SKU sku = new SKU(csc.geteWasteNameProducer(),String.valueOf(csc.geteWasteQtyProducer()),"");
//			cSKUList.add(sku);
//		}
//		ppgVo.setConsentSKU(cSKUList);
//		
//		return ppgVo;
//	}
	
	private ComparisonTableParamGroupVo getHazWasteData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_HW_Comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_HW_Comparison.class);
		ComparisonTableParamGroupVo ppgVo = new ComparisonTableParamGroupVo();
		ppgVo.setParam("Hazardous Waste");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_HW_Comparison csc : cscList) {
			SKU sku = new SKU(csc.getName(),String.valueOf(csc.getQuantity()),csc.getNewUom());
			cSKUList.add(sku);
		}
		ppgVo.setConsentSKU(cSKUList);
		
		List<ESR_FUEL_comparison> escList = mongoTemplate.find(getESRQueryObj(industryId, esrYear), ESR_FUEL_comparison.class);
		List<SKU> eSKUList = new ArrayList<SKU>();
		for(ESR_FUEL_comparison esc : escList) {
			SKU sku = new SKU(esc.getName(),String.valueOf(esc.getHwQuantityNow()),esc.getNewUom());
			eSKUList.add(sku);
		}
		ppgVo.setEsrSKU(eSKUList);
		return ppgVo;
	}
	
	private ComparisonTableParamGroupVo getFuelData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_FUEL_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_FUEL_comparison.class);
		ComparisonTableParamGroupVo ppgVo = new ComparisonTableParamGroupVo();
		ppgVo.setParam("Fuel");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_FUEL_comparison csc : cscList) {
			SKU sku = new SKU(csc.getFuelName(),String.valueOf(csc.getFuelConsumptions()),csc.getName());
			cSKUList.add(sku);
		}
		ppgVo.setConsentSKU(cSKUList);
		
		List<ESR_FUEL_comparison> escList = mongoTemplate.find(getESRQueryObj(industryId, esrYear), ESR_FUEL_comparison.class);
		List<SKU> eSKUList = new ArrayList<SKU>();
		for(ESR_FUEL_comparison esc : escList) {
			SKU sku = new SKU(esc.getFuelName(),String.valueOf(esc.getFuelQuantityActual()),esc.getName());
			eSKUList.add(sku);
		}
		ppgVo.setEsrSKU(eSKUList);
		return ppgVo;
	}

	private Query getConsentQueryObj(long industryId, int year) {
		try {
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		String yms = LocalDate.of(year, 1,1).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String yme = LocalDate.of(year, 12,31).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		query.addCriteria(Criteria.where("applicationCreatedOn")
				.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yms + " 00:00:00.000+0000"))
				.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yme + " 23:59:59.000+0000")));
		return query;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private Query getForm4QueryObj(long industryId, int year) {
		try {
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		String yms = LocalDate.of(year, 1,1).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String yme = LocalDate.of(year, 12,31).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		query.addCriteria(Criteria.where("createdTime")
				.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yms + " 00:00:00.000+0000"))
				.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yme + " 23:59:59.000+0000")));
		return query;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private Query getESRQueryObj(long industryId, int year) {
		try {
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		String yms = LocalDate.of(year, 1,1).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String yme = LocalDate.of(year, 12,31).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		query.addCriteria(Criteria.where("envStmtCreated")
				.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yms + " 00:00:00.000+0000"))
				.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yme + " 23:59:59.000+0000")));
		return query;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private ComparisonTableParamGroupVo getWaterData(long industryId,int consentYear,int esrYear) {
		List<Consent_WATER_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_WATER_comparison.class);
		ComparisonTableParamGroupVo ppgVo = new ComparisonTableParamGroupVo();
		ppgVo.setParam("Water");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_WATER_comparison csc : cscList) {
			SKU skuBod = new SKU("BOD",String.valueOf(csc.getTreatedEffluentBod()),csc.getName());
			cSKUList.add(skuBod);
			SKU skuCOD = new SKU("COD",String.valueOf(csc.getTreatedEffluentCod()),csc.getName());
			cSKUList.add(skuCOD);
			SKU skuSS = new SKU("SS",String.valueOf(csc.getTreatedEffluentSs()),csc.getName());
			cSKUList.add(skuSS);
			SKU skuTds = new SKU("TDS",String.valueOf(csc.getTreatedEffluentTds()),csc.getName());
			cSKUList.add(skuTds);
			SKU skuPh = new SKU("PH",String.valueOf(csc.getTreatedEffluentPh()),csc.getName());
			cSKUList.add(skuPh);
		}
		ppgVo.setConsentSKU(cSKUList);
		
		List<ESR_WATER_comparison> escList = mongoTemplate.find(getESRQueryObj(industryId, esrYear), ESR_WATER_comparison.class);
		List<SKU> eSKUList = new ArrayList<SKU>();
		for(ESR_WATER_comparison esc : escList) {
			SKU sku = new SKU(esc.getWaterPollutants(),String.valueOf(esc.getWaterPollutantConcentration()),esc.getUomName());
			eSKUList.add(sku);
		}
		ppgVo.setEsrSKU(eSKUList);
		return ppgVo;
	}

	private ComparisonTableParamGroupVo getAirData(long industryId,int consentYear,int esrYear) {
		List<Consented_Air_Pollution_Comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consented_Air_Pollution_Comparison.class);
		ComparisonTableParamGroupVo ppgVo = new ComparisonTableParamGroupVo();
		ppgVo.setParam("Air");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consented_Air_Pollution_Comparison csc : cscList) {
			SKU sku = new SKU(csc.getParameter(),String.valueOf(csc.getConcentration()),csc.getConcentrationUom());
			cSKUList.add(sku);
		}
		ppgVo.setConsentSKU(cSKUList);
		
		List<ESR_Air_Pollution_Comparison> escList = mongoTemplate.find(getESRQueryObj(industryId, esrYear), ESR_Air_Pollution_Comparison.class);
		List<SKU> eSKUList = new ArrayList<SKU>();
		for(ESR_Air_Pollution_Comparison esc : escList) {
			SKU sku = new SKU(esc.getAirPollutants(),String.valueOf(esc.getAirPollutantConcentration()),esc.getConcentrationUom());
			eSKUList.add(sku);
		}
		ppgVo.setEsrSKU(eSKUList);
		return ppgVo;
	}
	
	private List<ComparisonTableParamGroupVo> getProductionComparisonData(long industryId,int consentYear,int esrYear,int form4Year) {

		List<Consent_SKU_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_SKU_comparison.class);
		List<ComparisonTableParamGroupVo> ppgVoList = new ArrayList<ComparisonTableParamGroupVo>();
		ComparisonTableParamGroupVo ppgVo = new ComparisonTableParamGroupVo();
		ppgVo.setParam("SKU");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_SKU_comparison csc : cscList) {
			SKU sku = new SKU(csc.getProductname(),String.valueOf(csc.getTotal()),csc.getName());
			cSKUList.add(sku);
		}
		ppgVo.setConsentSKU(cSKUList);
		
		List<ESR_SKU_comparison> escList = mongoTemplate.find(getESRQueryObj(industryId, esrYear), ESR_SKU_comparison.class);
		List<SKU> eSKUList = new ArrayList<SKU>();
		for(ESR_SKU_comparison esc : escList) {
			SKU sku = new SKU(esc.getProductname(),String.valueOf(esc.getProductQty()),esc.getName());
			eSKUList.add(sku);
		}
		ppgVo.setEsrSKU(eSKUList);
		
		
		List<Annual_Returns_HW_Comparison> f4List = mongoTemplate.find(getForm4QueryObj(industryId, form4Year), Annual_Returns_HW_Comparison.class);
		List<SKU> form4List = new ArrayList<SKU>();
		for(Annual_Returns_HW_Comparison esc : f4List) {
			SKU sku = new SKU(esc.getName(),String.valueOf(esc.getQuantity()),"");
			form4List.add(sku);
		}
		ppgVo.setForm4SKU(form4List);
		
		ppgVoList.add(ppgVo);
		return ppgVoList;
	}
	
	private List<ComparisonTableParamGroupVo> getResourcesComparisonData(long industryId,int consentYear,int esrYear) {

		List<ComparisonTableParamGroupVo> ppgVoList = new ArrayList<ComparisonTableParamGroupVo>();
		
		ComparisonTableParamGroupVo ppgVoRaw = new ComparisonTableParamGroupVo();
		List<Consent_RESOURCES_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_RESOURCES_comparison.class);
		ppgVoRaw.setParam("Raw");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_RESOURCES_comparison csc : cscList) {
			SKU sku = new SKU(csc.getRawMaterialName(),String.valueOf(csc.getQty()),csc.getName());
			cSKUList.add(sku);
		}
		ppgVoRaw.setConsentSKU(cSKUList);
		
		List<ESR_RESOURCES_comparison> escList = mongoTemplate.find(getESRQueryObj(industryId, esrYear), ESR_RESOURCES_comparison.class);
		List<SKU> eSKUList = new ArrayList<SKU>();
		for(ESR_RESOURCES_comparison esc : escList) {
			SKU sku = new SKU(esc.getRawMaterialName(),String.valueOf(esc.getRawMaterialQty()),esc.getName());
			eSKUList.add(sku);
		}
		ppgVoRaw.setEsrSKU(eSKUList);
		
		ppgVoList.add(ppgVoRaw);
		//////////////////////////////////
		
		ComparisonTableParamGroupVo ppgVoWater = new ComparisonTableParamGroupVo();
		ppgVoRaw.setParam("Water");
		List<SKU> wcSKUList = new ArrayList<SKU>();
		List<SKU> infracSKUList = new ArrayList<SKU>();
		for(Consent_RESOURCES_comparison csc : cscList) {
			SKU skuW = new SKU("Water consumption",String.valueOf(csc.getWaterConsumption()),"");
			wcSKUList.add(skuW);
			SKU skuI = new SKU("Capital Investment",String.valueOf(csc.getGrossCapital()),"Lakhs");
			infracSKUList.add(skuI);
			break;
		}
		ppgVoWater.setConsentSKU(wcSKUList);
		
		List<SKU> weSKUList = new ArrayList<SKU>();
		List<SKU> infraeSKUList = new ArrayList<SKU>();
		for(ESR_RESOURCES_comparison esc : escList) {
			SKU skuW = new SKU("Water consumption",String.valueOf(esc.getWaterConsumptionTotalQuantityActual()),"");
			weSKUList.add(skuW);
			SKU skuI = new SKU("Capital Investment",String.valueOf(esc.getCapitalInvestment()),"Lakhs");
			infraeSKUList.add(skuI);
			break;
		}
		ppgVoWater.setEsrSKU(weSKUList);
		
		ppgVoList.add(ppgVoWater);
		
		ComparisonTableParamGroupVo ppgVoInfra = new ComparisonTableParamGroupVo();
		ppgVoInfra.setParam("Infrastructure");
		ppgVoInfra.setConsentSKU(infracSKUList);
		ppgVoInfra.setEsrSKU(infraeSKUList);
		ppgVoList.add(ppgVoInfra);
		////////////////////////////////////////////
		
		return ppgVoList;
	}

	public MandatoryReportsResponseVo getMandatoryReportsData(long industryId,int year) {
		
		MandatoryReportsResponseVo mrVO = new MandatoryReportsResponseVo();

		mrVO.seteWaste(getEWasteVo(industryId, year));
		mrVO.setBattery(getBatteryVo(industryId, year));
		mrVO.setPlastic(getPlasticVo(industryId, year));
		mrVO.setBioMedWaste(getBioMedWasteVo(industryId, year));
		
		return mrVO;
	}
	
	private BioMedWasteVo getBioMedWasteVo(long industryId, int year) {
		BioMedWasteVo bmwVo = new BioMedWasteVo();
		
		AnnualReturnsVo arVo = new AnnualReturnsVo();
		List<BMW_Annual_Return_Comparison> barList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,false), BMW_Annual_Return_Comparison.class);
		for(BMW_Annual_Return_Comparison bar : barList) {
			arVo.setBlueCategory(bar.getBlueCategory());
			arVo.setRedCategory(bar.getRedCategory());
			arVo.setWhiteCategory(bar.getWhiteCategory());
			arVo.setYellowCategory(bar.getYellowCategory());
			break;
		}
		bmwVo.setAnnualReturnsVo(arVo);
		//////////////////////////////////////
		
		BioMedWasteAuthFormVo baVo = new BioMedWasteAuthFormVo();
		List<BMW_Authorization_Comparison> bacList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,false), BMW_Authorization_Comparison.class);
		for(BMW_Authorization_Comparison bac : bacList) {
			baVo.setBiomedicalWasteName(bac.getBioMedicalWasteName());
			baVo.setBioMedicalWasteQuantity(bac.getBioMedicalWasteQuantity());
		}
		bmwVo.setBioMedWasteAuthFormVo(baVo);
		
		return bmwVo;
	}
	
	private PlasticVo getPlasticVo(long industryId, int year) {
		PlasticVo pVo = new PlasticVo();
		
		PlasticAuthorizationFormVo paFormVo = new PlasticAuthorizationFormVo();
		List<Plastic_Producer_Authorization> ppList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,true), Plastic_Producer_Authorization.class);
		for(Plastic_Producer_Authorization ppa : ppList) {
			paFormVo.setWasteProducer(ppa.getGenerationTotal());
			break;
		}
		List<Plastic_Brand_Owner_Authorization> pbList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,true), Plastic_Brand_Owner_Authorization.class);
		for(Plastic_Brand_Owner_Authorization pbo : pbList) {
			paFormVo.setWasteBrandOwner(pbo.getGenerationTotal());
			break;
		}
		List<Plastic_Recycler_Authorization> praList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,true), Plastic_Recycler_Authorization.class);
		for(Plastic_Recycler_Authorization pra : praList) {
			paFormVo.setWasterRecycledProcessed(pra.getWasteQuantumProcess());
			break;
		}
		List<Plastic_Raw_Material_Manufacturer_Authorization> prmList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,true), Plastic_Raw_Material_Manufacturer_Authorization.class);
		for(Plastic_Raw_Material_Manufacturer_Authorization prm : prmList) {
			paFormVo.setWasteRawMaterialProduction(prm.getProduceQty());
			break;
		}
		pVo.setPlasticAuthorizationFormVo(paFormVo);
		/////////////////////////////
		
		PlasticForm4Vo pf4Vo = new PlasticForm4Vo();
		List<Plastic_Annual_Report_Recycling_Facility_Form_IV> pf4List = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,true), Plastic_Annual_Report_Recycling_Facility_Form_IV.class);
		for(Plastic_Annual_Report_Recycling_Facility_Form_IV prm : pf4List) {
			pf4Vo.setQuantityDisposed(prm.getQuantityDisposed());
			pf4Vo.setQuantityInerts(prm.getQuantityInerts());
			pf4Vo.setQuantityReceived(prm.getQuantityReceived());
			pf4Vo.setQuantityRecycled(prm.getQuantityRecycled());
			break;
		}
		pVo.setPlasticForm4Vo(pf4Vo);
		///////////////////////////////
		
		Form5Vo f5Vo = new Form5Vo();
		List<Plastic_Annual_Report_Local_Body_Form_V> pf5List = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,true), Plastic_Annual_Report_Local_Body_Form_V.class);
		for(Plastic_Annual_Report_Local_Body_Form_V prm : pf5List) {
			f5Vo.setPlasticWasteCollectedQuantity(prm.getPlasticWasteCollectedQuantity());
			f5Vo.setPlasticWasteGeneratedQuantity(prm.getPlasticWasteGeneratedQuantity());
			f5Vo.setPlasticWasteProcessedQuantity(prm.getPlasticWasteProcessedQuantity());
			f5Vo.setPlasticWasteRecycledQuantity(prm.getPlasticWasteRecycledQuantity());
			f5Vo.setLandPlasticWasteQuantity(prm.getLandPlasticWasteQuantity());
			break;
		}
		pVo.setForm5Vo(f5Vo);
		
		return pVo;
	}
	
	private BatteryVo getBatteryVo(long industryId, int year) {
		BatteryVo bVo = new BatteryVo();
		
		List<Battery_Dealer_Annual_return_Form_V> bdList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,true), Battery_Dealer_Annual_return_Form_V.class);
		NewBatteriesSoldVo nbsVo = new NewBatteriesSoldVo();
		OldUsedBatteriesVo obsVo = new OldUsedBatteriesVo();
		BatteriesSoldToVo bstVo = new BatteriesSoldToVo();
		
		for(Battery_Dealer_Annual_return_Form_V csc : bdList) {
			nbsVo.setTwoWheeler(csc.getTwoWheelerBaterryCount());
			nbsVo.setFourWheeler(csc.getFourWheelerBaterryCount());
			nbsVo.setMotivePower(csc.getMotivePowerCount());
			nbsVo.setUps(csc.getUpsBatteryCount());
			nbsVo.setMotivePower(csc.getMotivePowerCount());
			nbsVo.setOtherInverters(csc.getOthersCount());
			
			obsVo.setTwoWheelerOld(csc.getTwoWheelerBaterryCountOld());
			obsVo.setFourWheelerOld(csc.getFourWheelerBaterryCountOld());
			obsVo.setMotivePowerOld(csc.getMotivePowerCountOld());
			obsVo.setStandByOld(csc.getStandByCountOld());
			obsVo.setUpsOld(csc.getUpsBatteryCountOld());
			obsVo.setOtherInvertersOld(csc.getOthersCountOld());
			
			bstVo.setBulkConsumersBatteryCount(csc.getBulkConsumersBatteryCount());
			bstVo.setDealersBatteryCount(csc.getDealersBatteryCount());
			bstVo.setOemBatteryCount(csc.getOemBatteryCount());
			bstVo.setAnyOtherParty(csc.getAnyOtherParty());
			
			break;
		}
		bVo.setNewBatteriesSoldVo(nbsVo);
		bVo.setOldUsedBatteriesVo(obsVo);
		bVo.setBatteriesSoldToVo(bstVo);
		
		return bVo;
	}

	private EWasteVo getEWasteVo(long industryId, int year) {
		EWasteVo ewVo = new EWasteVo();
		
		List<EWasteAnnualAuthorization> cscList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,false), EWasteAnnualAuthorization.class);
		Form1AVo f1AVo = new Form1AVo();
		for(EWasteAnnualAuthorization csc : cscList) {
			f1AVo.setEwasteQtyDisposal(csc.geteWasteQtyDisposal());
			f1AVo.setEwasteQtyGenerated(csc.geteWasteQtyGenerated());
			f1AVo.setEwasteQtyRecycling(csc.geteWasteQtyRecycling());
			f1AVo.setEwasteQtyRefurbushing(csc.geteWasteQtyRefurbishing());
			break;
		}
		ewVo.setForm1AVo(f1AVo);
		
		/////////////////////
		EWasteForm4Vo ewf4Vo = new EWasteForm4Vo();
		ewf4Vo.seteWasterProcessedLast3Years(0);
		ewf4Vo.setInstalledCapacity(0);
		ewf4Vo.setWasterGenInProcessWaste(0);
		ewVo.seteWasteForm4Vo(ewf4Vo);
		////////////////////
		
		List<EWasteAnnualReturns> ewaList = mongoTemplate.find(getMandatoryReportQueryObj(industryId, year,false), EWasteAnnualReturns.class);
		Form3Vo form3Vo = new Form3Vo();
		for(EWasteAnnualReturns csc : ewaList) {
			form3Vo.seteWasteQtyConsumer(csc.geteWasteQtyConsumer());
			form3Vo.seteWasteQtyDismantlersProcessed(csc.geteWasteQtyDismantlersProcessed());
			form3Vo.seteWasteQtyDismantlersRecoveredSold(csc.geteWasteQtyDismantlersRecoveredSold());
			form3Vo.seteWasteQtyDismantlersSentRecyclers(csc.geteWasteQtyDismantlersSentRecyclers());
			form3Vo.seteWasteQtyRecyclersSentTsdf(csc.geteWasteQtyRecyclersSentTsdf());
			break;
		}
		ewVo.setForm3Vo(form3Vo);
		
		return ewVo;
	}
	
	private Query getMandatoryReportQueryObj(long industryId, int year,boolean applicationCreatedOnColumn) {
		try {
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		String yms = LocalDate.of(year, 1,1).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String yme = LocalDate.of(year, 12,31).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		if(applicationCreatedOnColumn) {
			query.addCriteria(Criteria.where("applicationCreatedOn")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yms + " 00:00:00.000+0000"))
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yme + " 23:59:59.000+0000")));
		}else {
			query.addCriteria(Criteria.where("createdTime")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yms + " 00:00:00.000+0000"))
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(yme + " 23:59:59.000+0000")));
		}
		return query;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String,String>> getPollutionGraphParam(long industryId, String formTypes){
		List<Map<String,String>> paramList = new ArrayList<Map<String,String>>();
		String[] formArr = formTypes.split(",");
		for(String form : formArr) {
			if(CONSENT.equalsIgnoreCase(form)) {
				paramList.addAll(getDistinctParamListIndustryId(CONSENT,industryId,"Consent_air_pollution_comparison","parameter","concentration"));
				paramList.addAll(getFixedConsentWaterParamList(industryId));
				paramList.addAll(getDistinctParamListIndustryId(CONSENT,industryId,"consent_FUEL_comparison","fuelName","fuelConsumptions"));
				paramList.addAll(getFixedConsentEFFLUENTParamList(industryId));
				paramList.addAll(getDistinctParamListIndustryId(CONSENT,industryId,"Consent_HW_Comparison","name","quantity"));
			}else if(ESR.equalsIgnoreCase(form)) {
				paramList.addAll(getDistinctParamListIndustryId(ESR,industryId,"ESR_air_pollution","airPollutants","airPollutantConcentration"));
				paramList.addAll(getDistinctParamListIndustryId(ESR,industryId,"esr_WATER_comparison","waterPollutants","waterPollutantConcentration"));
				paramList.addAll(getDistinctParamListIndustryId(ESR,industryId,"esr_FUEL_comparison","fuelName","fuelQuantityActual"));
				paramList.addAll(getDistinctParamListIndustryId(ESR,industryId,"esr_EFFLUENT_comparison","effluentParticulars","effluentParticularsQuantityActual"));
				paramList.addAll(getDistinctParamListIndustryId(ESR,industryId,"ESR_HW_Comparison","name","hwQuantityNow"));
			}else if(HAZ_WASTE.equalsIgnoreCase(form)) {
				paramList.addAll(getDistinctParamListIndustryId(HAZ_WASTE,industryId,"Annual_Returns_HW_Comparison","name","quantity"));
			}else if(EWASTE.equalsIgnoreCase(form)) {
				paramList.addAll(IDSSUtil.getEWasteParams());
			}else if(BATTERY.equalsIgnoreCase(form)) {
				paramList.addAll(IDSSUtil.getBatteryParams());
			}else if(PLASTIC.equalsIgnoreCase(form)) {
				paramList.addAll(IDSSUtil.getPlasticParams());
			}else if(BIO_MED_WASTE.equalsIgnoreCase(form)) {
				paramList.addAll(IDSSUtil.getBioMedWasteParams());
			}else if(OCEMS.equalsIgnoreCase(form)) {
				paramList.addAll(getDistinctParamList(OCEMS,"industry_mis_id",industryId,"OCEMS_data","parameter_name","value"));
			}
		}
		return paramList;
		
	}

	@Override
	public IndustryMasterDetailResponseVo getIndustryMasterDetailByIndustryId(long industryId) {
		try {
			IndustryMaster industryMaster =industryMasterRepository.findByIndustryId(industryId);
			IndustryMasterDetailResponseVo industryMasterDetailResponseVo = new IndustryMasterDetailResponseVo();
			industryMasterDetailResponseVo.setRegion(industryMaster.getRegion());
			industryMasterDetailResponseVo.setCategory(industryMaster.getCategory());
			industryMasterDetailResponseVo.setScale(industryMaster.getScale());
			industryMasterDetailResponseVo.setType(industryMaster.getType());
			industryMasterDetailResponseVo.setCscore(industryMaster.getCscore());
			industryMasterDetailResponseVo.setIndustryName(industryMaster.getIndustryName());
			String date = LocalDate.of(1970, 1,1).format(DateTimeFormatter.ISO_LOCAL_DATE);

			Query query = new Query();
			query.addCriteria(Criteria.where("industryId").is(industryId));
			query.addCriteria(Criteria.where("statusUpdatedOn")
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 00:00:00.000+0000"))
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 23:59:59.000+0000")));
			List<Directions> directionsList = mongoTemplate.find(query, Directions.class);

			industryMasterDetailResponseVo.setPendingLegalAction(directionsList.size());
			return industryMasterDetailResponseVo;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private List<Map<String,String>> getFixedConsentEFFLUENTParamList(long industryId) {
		
		List<Map<String,String>> pl = new ArrayList<Map<String,String>>();
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		List<Consent_EFFLUENT_Comparison> cscList = mongoTemplate.find(query, Consent_EFFLUENT_Comparison.class);
		for(Consent_EFFLUENT_Comparison cwc : cscList) {
			if(cwc.getCapacityOfEtp()!=-999999) {
				Map<String,String> paramMap = new LinkedHashMap<String, String>();
				paramMap.putIfAbsent("name","CONSENT-Etp");
				paramMap.putIfAbsent("value","CONSENT~~Etp~~consent_EFFLUENT_comparison~~capacityOfEtp");
				if(!paramMap.isEmpty())
					pl.add(paramMap);
			}
			if(cwc.getCapacityOfStp()!=-999999) {
				Map<String,String> paramMap = new LinkedHashMap<String, String>();
				paramMap.putIfAbsent("name","CONSENT-Stp");
				paramMap.putIfAbsent("value","CONSENT~~Stp~~consent_EFFLUENT_comparison~~capacityOfStp");
				if(!paramMap.isEmpty())
					pl.add(paramMap);
			}
		}
		return pl;
	}
	
	private List<Map<String,String>> getFixedConsentWaterParamList(long industryId) {
		List<Map<String,String>> pl = new ArrayList<Map<String,String>>();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		List<Consent_WATER_comparison> cscList = mongoTemplate.find(query, Consent_WATER_comparison.class);
		for(Consent_WATER_comparison cwc : cscList) {
			if(cwc.getTreatedEffluentBod()!=-999999) {
				Map<String,String> paramMap = new LinkedHashMap<String, String>();
				paramMap.putIfAbsent("name","CONSENT-Bod");
				paramMap.putIfAbsent("value","CONSENT~~Bod~~consent_WATER_comparison~~treatedEffluentBod");
				if(!paramMap.isEmpty())
					pl.add(paramMap);
			}
			if(cwc.getTreatedEffluentCod()!=-999999) {
				Map<String,String> paramMap = new LinkedHashMap<String, String>();
				paramMap.putIfAbsent("name","CONSENT-Cod");
				paramMap.putIfAbsent("value","CONSENT~~Cod~~consent_WATER_comparison~~treatedEffluentCod");
				if(!paramMap.isEmpty())
					pl.add(paramMap);
			}
			if(cwc.getTreatedEffluentSs()!=-999999) {
				Map<String,String> paramMap = new LinkedHashMap<String, String>();
				paramMap.putIfAbsent("name","CONSENT-Ss");
				paramMap.putIfAbsent("value","CONSENT~~Ss~~consent_WATER_comparison~~treatedEffluentSs");
				if(!paramMap.isEmpty())
					pl.add(paramMap);
			}
			if(cwc.getTreatedEffluentTds()!=-999999) {
				Map<String,String> paramMap = new LinkedHashMap<String, String>();
				paramMap.putIfAbsent("name","CONSENT-Tds");
				paramMap.putIfAbsent("value","CONSENT~~Tds~~consent_WATER_comparison~~treatedEffluentTds");
				if(!paramMap.isEmpty())
					pl.add(paramMap);
			}
			if(cwc.getTreatedEffluentPh()!=-999999) {
				Map<String,String> paramMap = new LinkedHashMap<String, String>();
				paramMap.putIfAbsent("name","CONSENT-Ph");
				paramMap.putIfAbsent("value","CONSENT~~Ph~~consent_WATER_comparison~~treatedEffluentPh");
				if(!paramMap.isEmpty())
					pl.add(paramMap);
			}
		}
		 
		
		return pl;
	}
	
	private List<Map<String,String>> getDistinctParamListIndustryId(String formType,long industryId,String collectionName,String paramField,String valueField) {
		return getDistinctParamList(formType,"industryId",industryId,collectionName,paramField,valueField);
	}
	
	private List<Map<String,String>> getDistinctParamList(String formType,String industryIdentifier, long industryId,String collectionName,String paramField,String valueField) {
		Document matchDoc = new Document();
		matchDoc.append(industryIdentifier, industryId);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", null)
		                        .append("param",  new Document()
		                                .append("$addToSet", "$"+paramField)
		                )
		            ),
		                new Document()
			            .append("$project", new Document()
			                    .append("_id", false)
			                    .append("param", "$param")
			            )
				);
		MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        ParameterVo param = new ParameterVo();
		collection.aggregate(pipeline)
        .allowDiskUse(false)
        .forEach(new Consumer<Document>() {
                @Override
                public void accept(Document document) {
                    logger.info(document.toJson());
					try {
						ParameterVo pVo = (new ObjectMapper().readValue(document.toJson(), ParameterVo.class));
						param.setParam(pVo.getParam());
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
                    
                }
            }
        );
		
		List<Map<String,String>> pl = new ArrayList<Map<String,String>>(); 
		for(String s : param.getParam()) {
			Map<String,String> paramMap = new LinkedHashMap<String, String>();
			paramMap.put("name",formType+"-"+s);
			paramMap.put("value",formType+"~~"+s+"~~"+collectionName+"~~"+paramField+"~~"+valueField);
			pl.add(paramMap);
		}
		
		return pl;
	}
}
