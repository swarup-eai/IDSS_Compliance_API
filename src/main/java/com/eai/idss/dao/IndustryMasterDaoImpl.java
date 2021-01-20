package com.eai.idss.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.AnnualReturnsVo;
import com.eai.idss.vo.BatteriesSoldToVo;
import com.eai.idss.vo.BatteryVo;
import com.eai.idss.vo.BioMedWasteAuthFormVo;
import com.eai.idss.vo.BioMedWasteVo;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.ComplianceScoreResponseVo;
import com.eai.idss.vo.EWasteForm4Vo;
import com.eai.idss.vo.EWasteVo;
import com.eai.idss.vo.Form1AVo;
import com.eai.idss.vo.Form3Vo;
import com.eai.idss.vo.Form5Vo;
import com.eai.idss.vo.IndustryMasterRequest;
import com.eai.idss.vo.MandatoryReportsResponseVo;
import com.eai.idss.vo.NewBatteriesSoldVo;
import com.eai.idss.vo.OldUsedBatteriesVo;
import com.eai.idss.vo.ParameterVo;
import com.eai.idss.vo.PlasticAuthorizationFormVo;
import com.eai.idss.vo.PlasticForm4Vo;
import com.eai.idss.vo.PlasticVo;
import com.eai.idss.vo.PollutionParamGroupVo;
import com.eai.idss.vo.PollutionScoreFilter;
import com.eai.idss.vo.PollutionScoreResponseVo;
import com.eai.idss.vo.SKU;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


@Repository
public class IndustryMasterDaoImpl implements IndustryMasterDao {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	MongoClient mongoClient;
	
	public static final Logger logger = Logger.getLogger(IndustryMasterDaoImpl.class);
	
	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr ,Pageable page){
		
		
		Query query = new Query().with(page);
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
			if(null!=imr.getComplianceScore() && !"All".equalsIgnoreCase(imr.getComplianceScore())) {
				if("-".equalsIgnoreCase(imr.getComplianceScore())) {
					String[] csString = imr.getComplianceScore().split("-");
					query.addCriteria(Criteria.where("complianceScore")
							.gte(csString[0])
							.lte(csString[1]));
				}
			}
			if(StringUtils.hasText(imr.getLegalActions()) && !"All".equalsIgnoreCase(imr.getLegalActions())) {
				if("-".equalsIgnoreCase(imr.getLegalActions())) {
					String[] csString = imr.getLegalActions().split("-");
					query.addCriteria(Criteria.where("legalActions")
							.gte(csString[0])
							.lte(csString[1]));
				}else if("Above".equalsIgnoreCase(imr.getLegalActions())) {
					query.addCriteria(Criteria.where("legalActions").gt(50));
				}
			}
			if(StringUtils.hasText(imr.getPendingCases()) && !"All".equalsIgnoreCase(imr.getPendingCases()))
				query.addCriteria(Criteria.where("pendingCases").is(imr.getPendingCases()));
			
				if("-".equalsIgnoreCase(imr.getPendingCases())) {
					String[] csString = imr.getPendingCases().split("-");
					query.addCriteria(Criteria.where("pendingCases")
							.gte(csString[0])
							.lte(csString[1]));
				}else if("Above".equalsIgnoreCase(imr.getPendingCases())) {
					query.addCriteria(Criteria.where("pendingCases").gt(50));
				}
		}
		

		System.out.println(mongoTemplate.count(query, IndustryMaster.class));
		
		List<IndustryMaster> filteredIndustryMaster = mongoTemplate.find(query, IndustryMaster.class);
		
		Page<IndustryMaster> imPage = PageableExecutionUtils.getPage(filteredIndustryMaster, page,
		        () -> mongoTemplate.count(query, IndustryMaster.class));
		
		for(IndustryMaster im : imPage) {
			Query queryLDM = new Query();
			queryLDM.addCriteria(Criteria.where("industryId").is(im.getIndustryId()));
			List<Legal> ldmObj = mongoTemplate.find(queryLDM, Legal.class);
			if(null!=ldmObj && ldmObj.size()>0) {
				int lap = (int)(ldmObj.get(0).getTotalLegalActionsCreated()-ldmObj.get(0).getTotalDirections());
				im.setLegalActionsPending(lap>0?lap:0);
			}
			
			Query queryVisit = new Query();
			queryVisit.addCriteria(Criteria.where("industryId").is(im.getIndustryId()));
			queryVisit.with(Sort.by(Sort.Direction.DESC,"visitId"));
			List<Visits> visitObj = mongoTemplate.find(queryLDM, Visits.class);
			if(null!=visitObj && visitObj.size()>0)
				im.setLastVisited(visitObj.get(0).getVisitedDate());
		}
		
		return imPage.toList();
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

	public Map<String, Map<String, List<PollutionScoreResponseVo>>> getPollutionScoreData(PollutionScoreFilter cf) {

		Map<String, Map<String, List<PollutionScoreResponseVo>>> responseMap = new LinkedHashMap<String, Map<String, List<PollutionScoreResponseVo>>>();


		return responseMap;
	}

	@Override
	public PollutionScoreResponseVo getComparisonData(long industryId,int consentYear,int esrYear,int form4Year) {

		logger.info("getComparisonData..."+industryId+", consentYear: "+consentYear+", esrYear: "+esrYear);
		PollutionScoreResponseVo psrVo = new PollutionScoreResponseVo();
		Map<String,List<PollutionParamGroupVo>> mapPGVo = new LinkedHashMap<String, List<PollutionParamGroupVo>>();
		
		mapPGVo.put("production",getProductionComparisonData(industryId,consentYear,esrYear, form4Year));
		mapPGVo.put("resources",getResourcesComparisonData(industryId,consentYear,esrYear));
		mapPGVo.put("pollution",getPollutionComparisonData(industryId,consentYear,esrYear));
		mapPGVo.put("stack",getStackComparisonData(industryId,consentYear,esrYear));
		
		psrVo.setPollutionScore(mapPGVo);
		return psrVo;
	}
	
	private List<PollutionParamGroupVo> getStackComparisonData(long industryId,int consentYear,int esrYear) {

		List<PollutionParamGroupVo> ppgVoList = new ArrayList<PollutionParamGroupVo>();
		ppgVoList.add(getStackData(industryId,consentYear,esrYear));
		return ppgVoList;
	}
	
	private PollutionParamGroupVo getStackData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_STACK_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_STACK_comparison.class);
		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
		ppgVo.setParam("stack");
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


	private List<PollutionParamGroupVo> getPollutionComparisonData(long industryId,int consentYear,int esrYear) {

		List<PollutionParamGroupVo> ppgVoList = new ArrayList<PollutionParamGroupVo>();
		
		ppgVoList.add(getAirData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getWaterData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getFuelData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getHazWasteData(industryId,consentYear,esrYear));
		
//		ppgVoList.add(geteWasteData(industryId,consentYear,esrYear));
		
		ppgVoList.add(getEffluentData(industryId,consentYear,esrYear));
		
		return ppgVoList;
	}
	
	private PollutionParamGroupVo getEffluentData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_EFFLUENT_Comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_EFFLUENT_Comparison.class);
		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
		ppgVo.setParam("effluent");
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
			SKU sku = new SKU(esc.getEffluentParticulars(),String.valueOf(esc.getEffluentParticularsQuantityActual()),esc.getEffluentUom());
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
	
	private PollutionParamGroupVo getHazWasteData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_HW_Comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_HW_Comparison.class);
		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
		ppgVo.setParam("hazWaste");
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
	
	private PollutionParamGroupVo getFuelData(long industryId,int consentYear,int esrYear) {
		
		List<Consent_FUEL_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_FUEL_comparison.class);
		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
		ppgVo.setParam("fuel");
		List<SKU> cSKUList = new ArrayList<SKU>();
		for(Consent_FUEL_comparison csc : cscList) {
			SKU sku = new SKU(csc.getFuelType(),String.valueOf(csc.getFuelConsumptions()),csc.getName());
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

	private PollutionParamGroupVo getWaterData(long industryId,int consentYear,int esrYear) {
		List<Consent_WATER_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_WATER_comparison.class);
		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
		ppgVo.setParam("water");
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

	private PollutionParamGroupVo getAirData(long industryId,int consentYear,int esrYear) {
		List<Consented_Air_Pollution_Comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consented_Air_Pollution_Comparison.class);
		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
		ppgVo.setParam("air");
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
	
	private List<PollutionParamGroupVo> getProductionComparisonData(long industryId,int consentYear,int esrYear,int form4Year) {

		List<Consent_SKU_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_SKU_comparison.class);
		List<PollutionParamGroupVo> ppgVoList = new ArrayList<PollutionParamGroupVo>();
		PollutionParamGroupVo ppgVo = new PollutionParamGroupVo();
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
	
	private List<PollutionParamGroupVo> getResourcesComparisonData(long industryId,int consentYear,int esrYear) {

		List<PollutionParamGroupVo> ppgVoList = new ArrayList<PollutionParamGroupVo>();
		
		PollutionParamGroupVo ppgVoRaw = new PollutionParamGroupVo();
		List<Consent_RESOURCES_comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId, consentYear), Consent_RESOURCES_comparison.class);
		ppgVoRaw.setParam("raw");
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
		
		PollutionParamGroupVo ppgVoWater = new PollutionParamGroupVo();
		ppgVoRaw.setParam("water");
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
		
		PollutionParamGroupVo ppgVoInfra = new PollutionParamGroupVo();
		ppgVoInfra.setParam("infra");
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
	
	public List<String> getPollutionGraphParam(long industryId, String form){
		List<String> paramList = new ArrayList<String>();
		if("consent".equalsIgnoreCase(form)) {
			paramList.addAll(getDistinctParamListIndustryId(industryId,"Consent_air_pollution_comparison","parameter"));
			paramList.addAll(getFixedConsentWaterParamList(industryId));
			paramList.addAll(getDistinctParamListIndustryId(industryId,"consent_FUEL_comparison","fuelName"));
			paramList.addAll(getFixedConsentEFFLUENTParamList(industryId));
			paramList.addAll(getDistinctParamListIndustryId(industryId,"Consent_HW_Comparison","name"));
		}else if("esr".equalsIgnoreCase(form)) {
			paramList.addAll(getDistinctParamListIndustryId(industryId,"ESR_air_pollution","airPollutants"));
			paramList.addAll(getDistinctParamListIndustryId(industryId,"esr_WATER_comparison","waterPollutants"));
			paramList.addAll(getDistinctParamListIndustryId(industryId,"esr_FUEL_comparison","fuelName"));
			paramList.addAll(getDistinctParamListIndustryId(industryId,"esr_EFFLUENT_comparison","effluentParticulars"));
			paramList.addAll(getDistinctParamListIndustryId(industryId,"ESR_HW_Comparison","name"));
		}else if("HAZ_WASTE".equalsIgnoreCase(form)) {
			paramList.addAll(getDistinctParamListIndustryId(industryId,"Annual_Returns_HW_Comparison","name"));
		}else if("EWASTE".equalsIgnoreCase(form)) {
			paramList.addAll(IDSSUtil.getEWasteParams());
		}else if("Battery".equalsIgnoreCase(form)) {
			paramList.addAll(IDSSUtil.getBatteryParams());
		}else if("Plastic".equalsIgnoreCase(form)) {
			paramList.addAll(IDSSUtil.getPlasticParams());
		}else if("BioMedWaste".equalsIgnoreCase(form)) {
			paramList.addAll(IDSSUtil.getBioMedWasteParams());
		}else if("OCEMS".equalsIgnoreCase(form)) {
			paramList.addAll(getDistinctParamList("industry_mis_id",industryId,"OCEMS_data","parameter_name"));
		}
		
		return paramList;
		
	}
	
	private List<String> getFixedConsentEFFLUENTParamList(long industryId) {
		Set<String> paramSet = new LinkedHashSet<String>();
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		List<Consent_EFFLUENT_Comparison> cscList = mongoTemplate.find(query, Consent_EFFLUENT_Comparison.class);
		for(Consent_EFFLUENT_Comparison cwc : cscList) {
			if(cwc.getCapacityOfEtp()!=-999999)
				paramSet.add("Etp");
			if(cwc.getCapacityOfStp()!=-999999)
				paramSet.add("Stp");
		}
		if(!paramSet.isEmpty())
			return paramSet.stream().collect(Collectors.toList());
		else			
			return null;
	}
	
	private List<String> getFixedConsentWaterParamList(long industryId) {
		Set<String> paramSet = new LinkedHashSet<String>();
		Query query = new Query();
		query.addCriteria(Criteria.where("industryId").is(industryId));
		List<Consent_WATER_comparison> cscList = mongoTemplate.find(query, Consent_WATER_comparison.class);
		for(Consent_WATER_comparison cwc : cscList) {
			if(cwc.getTreatedEffluentBod()!=-999999)
				paramSet.add("Bod");
			if(cwc.getTreatedEffluentCod()!=-999999)
				paramSet.add("Cod");
			if(cwc.getTreatedEffluentSs()!=-999999)
				paramSet.add("Ss");
			if(cwc.getTreatedEffluentTds()!=-999999)
				paramSet.add("Tds");
			if(cwc.getTreatedEffluentPh()!=-999999)
				paramSet.add("Ph");
		}
		if(!paramSet.isEmpty())
			return paramSet.stream().collect(Collectors.toList());
		else			
			return new ArrayList<String>();
	}
	
	private List<String> getDistinctParamListIndustryId(long industryId,String collectionName,String field) {
		return getDistinctParamList("industryId",industryId,collectionName,field);
	}
	
	private List<String> getDistinctParamList(String industryIdentifier, long industryId,String collectionName,String field) {
		Document matchDoc = new Document();
		matchDoc.append(industryIdentifier, industryId);
		
		List<? extends Bson> pipeline = Arrays.asList(
				new Document().append("$match", matchDoc),  
		        new Document()
		                .append("$group", new Document()
		                        .append("_id", null)
		                        .append("param",  new Document()
		                                .append("$addToSet", "$"+field)
		                )
		            ),
		                new Document()
			            .append("$project", new Document()
			                    .append("_id", false)
			                    .append("param", "$param")
			            )
				);
		MongoDatabase database = mongoClient.getDatabase("IDSS");
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
		
		return param.getParam();
	}
}
