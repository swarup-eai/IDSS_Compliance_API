package com.eai.idss.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.eai.idss.vo.PlasticAuthorizationFormVo;
import com.eai.idss.vo.PlasticForm4Vo;
import com.eai.idss.vo.PlasticVo;
import com.eai.idss.vo.PollutionParamGroupVo;
import com.eai.idss.vo.PollutionScoreFilter;
import com.eai.idss.vo.PollutionScoreResponseVo;
import com.eai.idss.vo.SKU;

@Repository
public class IndustryMasterDaoImpl implements IndustryMasterDao {
	
	@Autowired
	MongoTemplate mongoTemplate;
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

	public Map<String, Map<String, List<PollutionScoreResponseVo>>> getByIndustryNamePollutionScoreData(
			PollutionScoreFilter cf) {

		Map<String, Map<String, List<PollutionScoreResponseVo>>> responseMap = new LinkedHashMap<String, Map<String, List<PollutionScoreResponseVo>>>();

//		try {
//
//			String type = "";
//			String param = "";
//			logger.info("getPollutionScoreCardData");
//			List<Map<String, String>> paramList = new ArrayList<>();
//            esrComparisonList.clear();
//            consentedComparisonList.clear();
//            
//			paramList.addAll(cf.getParamList());
//
//			for (int i = 0; i < paramList.size(); i++) {
//				Map<String, String> hm = paramList.get(i);
//				for (Entry<String, String> entry : hm.entrySet()) {
//					type = entry.getKey();
//					param = entry.getValue();
//
//					// paramList.addAll(cf.getParametersList());
//
//					if (type.equalsIgnoreCase("Consented")) {
//						List<String> consentedESRAirParamList = IDSSUtil.getConsentedESRAirParam();
//						if (consentedESRAirParamList.contains(param)) {
//							type = "ConsentedAir";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("ESR")) {
//						List<String> consentedESRAirParamList = IDSSUtil.getConsentedESRAirParam();
//						if (consentedESRAirParamList.contains(param)) {
//							type = "ESRAir";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("Consented")) {
//						List<String> consentedESRWaterParamList = IDSSUtil.getConsentedESRWaterParam();
//
//						if (consentedESRWaterParamList.contains(param)) {
//							type = "ConsentedWater";
//						}
//
//					}
//					if (type.equalsIgnoreCase("ESR")) {
//						List<String> consentedESRWaterParamList = IDSSUtil.getConsentedESRWaterParam();
//
//						if (consentedESRWaterParamList.contains(param)) {
//							type = "ESRWater";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("Consented")) {
//						List<String> consentedESRStackParamList = IDSSUtil.getConsentedESRStackParam();
//						if (consentedESRStackParamList.contains(param)) {
//							type = "ConsentedStack";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("Consented")) {
//						List<String> consentedESRFuelParamList = IDSSUtil.getConsentedESRFuelParam();
//						if (consentedESRFuelParamList.contains(param)) {
//							type = "ConsentedFuel";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("ESR")) {
//						List<String> consentedESRFuelParamList = IDSSUtil.getConsentedESRFuelParam();
//						if (consentedESRFuelParamList.contains(param)) {
//							type = "ESRFuel";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("Consented")) {
//						List<String> consentedESRHWParamList = IDSSUtil.getConsentedESRHWParam();
//						if (consentedESRHWParamList.contains(param)) {
//							type = "ConsentedHW";
//						}
//
//					}
//					if (type.equalsIgnoreCase("ESR")) {
//						List<String> consentedESRHWParamList = IDSSUtil.getConsentedESRHWParam();
//						if (consentedESRHWParamList.contains(param)) {
//							type = "EsrHW";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("Consented")) {
//						List<String> consentedESRResourceParamList = IDSSUtil.getConsentedESRResourcesParam();
//						if (consentedESRResourceParamList.contains(param)) {
//							type = "ConsentedRESOURCES";
//						}
//
//					}
//					if (type.equalsIgnoreCase("ESR")) {
//						List<String> consentedESRResourceParamList = IDSSUtil.getConsentedESRResourcesParam();
//						if (consentedESRResourceParamList.contains(param)) {
//							type = "EsrRESOURCES";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("Consented")) {
//						List<String> consentedESRSkuParamList = IDSSUtil.getConsentedESRSkuParam();
//						if (consentedESRSkuParamList.contains(param)) {
//							type = "ConsentedSku";
//						}
//
//					}
//					if (type.equalsIgnoreCase("ESR")) {
//						List<String> consentedESRSkuParamList = IDSSUtil.getConsentedESRSkuParam();
//						if (consentedESRSkuParamList.contains(param)) {
//							type = "EsrSku";
//						}
//
//					}
//
//					if (type.equalsIgnoreCase("OCEMS")) {
//						List<String> ocemsParamList = IDSSUtil.getOCEMSParam();
//						if (ocemsParamList.contains(param)) {
//							type = "OCEMSData";
//						}
//					}
//
//					if (type.equalsIgnoreCase("ConsentedAir")) {
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						logger.info("getPollutionScoreCard_ConsentedAirData");
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//
//						if (null != cf) {
//
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//								List<PollutionScoreResponseVo> consentedList = new ArrayList<PollutionScoreResponseVo>();
//
//								logger.info("getPollutionScoreCard_ConsentedAirData : " + days);
//								// query.addCriteria(Criteria.where("created").gte(days));
//
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("parameter").is(param));
//
//								System.out
//										.println(mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));
//
//								List<Consented_Air_Pollution_Comparison> filteredList = mongoTemplate.find(query,
//										Consented_Air_Pollution_Comparison.class);
//								Page<Consented_Air_Pollution_Comparison> cPage = PageableExecutionUtils.getPage(
//										filteredList, page,
//										() -> mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));
//
//								List<Consented_Air_Pollution_Comparison> consentedAirList = new ArrayList<Consented_Air_Pollution_Comparison>(
//										cPage.toList());
//								int count = 0;
//								for (Consented_Air_Pollution_Comparison cAir : consentedAirList) {
//
//									String created = cAir.getCreated();
//
//									Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created);
//									Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(days + " 00:00:00");
//
//									logger.info("created date: " + date2);
//									logger.info("filter date: " + date1);
//
//									if (date2.after(date1)) {
//										count++;
//										consentedList.add(new PollutionScoreResponseVo(cAir.get_id(),
//												cAir.getIndustryId(), cAir.getIndustryname(), cAir.getConcentration(),
//												cAir.getConcentrationUom(), cAir.getCreated(), cAir.getParameter()));
//
//										if (count == 1) {
//
//											consentedComparisonList.add(
//													new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryId(),
//															cAir.getIndustryname(), cAir.getConcentration(),
//															cAir.getConcentrationUom(), cAir.getParameter()));
//
//										}
//									}
//
//									resMap.put(daysMap1.get(days), consentedList);
//
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("ESRAir")) {
//
//						logger.info("getPollutionScoreCard_ESRAirData");
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//
//								count++;
//								List<PollutionScoreResponseVo> esrList = new ArrayList<PollutionScoreResponseVo>();
//
//								logger.info("getPollutionScoreCard_ESRAirData : " + days);
//								// query.addCriteria(Criteria.where("created").gte(days));
//
//								String[] dateParts = days.split("-");
//
//								String year = dateParts[0];
//
//								int inum = Integer.parseInt(year);
//
//								query.addCriteria(Criteria.where("finantialyear").gte(inum));
//
//								query.addCriteria(Criteria.where("industryid").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("airpollutants").is(param));
//
//								System.out.println(mongoTemplate.count(query, ESR_Air_Pollution_Comparison.class));
//
//								List<ESR_Air_Pollution_Comparison> filteredList = mongoTemplate.find(query,
//										ESR_Air_Pollution_Comparison.class);
//								Page<ESR_Air_Pollution_Comparison> cPage = PageableExecutionUtils.getPage(filteredList,
//										page, () -> mongoTemplate.count(query, ESR_Air_Pollution_Comparison.class));
//
//								List<ESR_Air_Pollution_Comparison> esrAirList = new ArrayList<ESR_Air_Pollution_Comparison>(
//										cPage.toList());
//
//								for (ESR_Air_Pollution_Comparison cAir : esrAirList) {
//
//									esrList.add(new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryid(),
//											cAir.getCompanyname(), cAir.getAirpollutantquantity(), cAir.getQtyuom(),
//											cAir.getFinantialyear()));
//
//									if (count == 1) {
//
//										esrComparisonList
//												.add(new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryid(),
//														cAir.getCompanyname(), cAir.getAirpollutantquantity(),
//														cAir.getQtyuom(), cAir.getFinantialyear()));
//
//									}
//
//									resMap.put(daysMap1.get(days), esrList);
//
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("ConsentedWater")) {
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						logger.info("getPollutionScoreCard_ConsentedWaterData");
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//
//						if (null != cf) {
//
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//								count++;
//								List<PollutionScoreResponseVo> consentedList = new ArrayList<PollutionScoreResponseVo>();
//
//								logger.info("getPollutionScoreCard_ConsentedWaterData : " + days);
//								// query.addCriteria(Criteria.where("created").gte(days));
//
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("parameter").is(param));
//
//								System.out
//										.println(mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));
//
//								List<Consent_WATER_comparison> filteredList = mongoTemplate.find(query,
//										Consent_WATER_comparison.class);
//								Page<Consent_WATER_comparison> cPage = PageableExecutionUtils.getPage(filteredList,
//										page,
//										() -> mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));
//
//								List<Consent_WATER_comparison> consentedWaterList = new ArrayList<Consent_WATER_comparison>(
//										cPage.toList());
//
//								for (Consent_WATER_comparison cAir : consentedWaterList) {
//
//									/*
//									 * String created = cAir.getCreated();
//									 * 
//									 * Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created); Date
//									 * date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(days +
//									 * " 00:00:00");
//									 * 
//									 * logger.info("created date: " + date2); logger.info("filter date: " + date1);
//									 */
//									// if (date2.after(date1)) {
//									consentedList.add(new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryId(),
//											cAir.getIndustryName(), cAir.getUom(), cAir.getName(),
//											cAir.getTreatedEffluentBod(), cAir.getTreatedEffluentCod(),
//											cAir.getTreatedEffluentSs(), cAir.getTreatedEffluentTds(),
//											cAir.getTreatedEffluentPh()));
//
//									// }
//
//									if (count == 1) {
//										consentedComparisonList.add(new PollutionScoreResponseVo(cAir.get_id(),
//												cAir.getIndustryId(), cAir.getIndustryName(), cAir.getUom(),
//												cAir.getName(), cAir.getTreatedEffluentBod(),
//												cAir.getTreatedEffluentCod(), cAir.getTreatedEffluentSs(),
//												cAir.getTreatedEffluentTds(), cAir.getTreatedEffluentPh()));
//
//									}
//
//									resMap.put(daysMap1.get(days), consentedList);
//
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("ESRWater")) {
//
//						logger.info("getPollutionScoreCard_ESRWaterData");
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//								count++;
//								List<PollutionScoreResponseVo> esrList = new ArrayList<PollutionScoreResponseVo>();
//
//								logger.info("getPollutionScoreCard_ESRWaterData : " + days);
//								// query.addCriteria(Criteria.where("created").gte(days));
//
//								String[] dateParts = days.split("-");
//
//								String year = dateParts[0];
//
//								int inDate = Integer.parseInt(year);
//
//								// query.addCriteria(Criteria.where("finantialyear").gte(inum));
//
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("waterPollutants").is(param));
//
//								// System.out.println(mongoTemplate.count(query, ESR_WATER_comparison.class));
//
//								List<ESR_WATER_comparison> filteredList = mongoTemplate.find(query,
//										ESR_WATER_comparison.class);
//								Page<ESR_WATER_comparison> cPage = PageableExecutionUtils.getPage(filteredList, page,
//										() -> mongoTemplate.count(query, ESR_WATER_comparison.class));
//
//								List<ESR_WATER_comparison> esrWaterList = new ArrayList<ESR_WATER_comparison>(
//										cPage.toList());
//
//								for (ESR_WATER_comparison eWater : esrWaterList) {
//
//									int financialYr = eWater.getFinantialyear();
//
//									if (financialYr >= inDate) {
//										esrList.add(new PollutionScoreResponseVo(eWater.get_id(),
//												eWater.getIndustryid(), eWater.getCompanyname(),
//												eWater.getWaterPollutants(), eWater.getWaterPollutantQuantity(),
//												eWater.getUomName(), eWater.getFinantialyear()));
//
//										if (count == 1) {
//
//											esrComparisonList.add(new PollutionScoreResponseVo(eWater.get_id(),
//													eWater.getIndustryid(), eWater.getCompanyname(),
//													eWater.getWaterPollutants(), eWater.getWaterPollutantQuantity(),
//													eWater.getUomName(), eWater.getFinantialyear()));
//
//										}
//
//										resMap.put(daysMap1.get(days), esrList);
//									}
//
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//
//					if (type.equalsIgnoreCase("ConsentedFuel")) {
//
//						logger.info("getPollutionScoreCard_ConsentedFuelData");
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//						List<PollutionScoreResponseVo> cFuelList = new ArrayList<PollutionScoreResponseVo>();
//
//						if (null != cf) {
//
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//								count++;
//								logger.info("getPollutionScoreCard_ConsentedFuelData : " + days);
//
//								query.addCriteria(Criteria.where("industryid").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("fuelName").is(param));
//
//								// System.out.println(mongoTemplate.count(query,
//								// Consent_FUEL_comparison.class));
//
//								List<Consent_FUEL_comparison> filteredFuelList = mongoTemplate.find(query,
//										Consent_FUEL_comparison.class);
//								Page<Consent_FUEL_comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList,
//										page, () -> mongoTemplate.count(query, Consent_FUEL_comparison.class));
//
//								List<Consent_FUEL_comparison> consentFuelList = new ArrayList<Consent_FUEL_comparison>(
//										cPage.toList());
//
//								for (Consent_FUEL_comparison cFuel : consentFuelList) {
//
//									cFuelList.add(new PollutionScoreResponseVo(cFuel.get_id(), cFuel.getIndustryId(),
//											cFuel.getIndustryName(), cFuel.getUom(), cFuel.getName(),
//											cFuel.getFuelName()));
//
//									if (count == 1) {
//
//										consentedComparisonList.add(new PollutionScoreResponseVo(cFuel.get_id(),
//												cFuel.getIndustryId(), cFuel.getIndustryName(), cFuel.getUom(),
//												cFuel.getName(), cFuel.getFuelName()));
//									}
//
//									resMap.put(daysMap1.get(days), cFuelList);
//
//								}
//
//							}
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("ESRFuel")) {
//
//						logger.info("getPollutionScoreCard_ESRFuelData");
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								count++;
//								List<PollutionScoreResponseVo> esrFuelList = new ArrayList<PollutionScoreResponseVo>();
//
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_ESRFuelData : " + days);
//
//								String[] dateParts = days.split("-");
//
//								String year = dateParts[0];
//
//								int inum = Integer.parseInt(year);
//
//								// query.addCriteria(Criteria.where("finantialYear").gte(inum));
//
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("fuelName").is(param));
//
//								// System.out.println(mongoTemplate.count(query,
//								// Consent_FUEL_comparison.class));
//
//								List<ESR_FUEL_comparison> filteredFuelList = mongoTemplate.find(query,
//										ESR_FUEL_comparison.class);
//								Page<ESR_FUEL_comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList, page,
//										() -> mongoTemplate.count(query, ESR_FUEL_comparison.class));
//
//								List<ESR_FUEL_comparison> eFuelList = new ArrayList<ESR_FUEL_comparison>(
//										cPage.toList());
//
//								for (ESR_FUEL_comparison esrFuel : eFuelList) {
//
//									int fyear = esrFuel.getFinantialYear();
//
//									if (fyear >= inum) {
//										esrFuelList.add(new PollutionScoreResponseVo(esrFuel.get_id(),
//												esrFuel.getIndustryId(), esrFuel.getFuelName(), esrFuel.getFuelUom(),
//												esrFuel.getName(), esrFuel.getFinantialYear()));
//
//										if (count == 1) {
//
//											esrComparisonList.add(new PollutionScoreResponseVo(esrFuel.get_id(),
//													esrFuel.getIndustryId(), esrFuel.getFuelName(),
//													esrFuel.getFuelUom(), esrFuel.getName(),
//													esrFuel.getFinantialYear()));
//
//										}
//										resMap.put(daysMap1.get(days), esrFuelList);
//
//									}
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("ConsentedHW")) {
//
//						logger.info("getPollutionScoreCard_ConsentedHWData");
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								count++;
//								List<PollutionScoreResponseVo> cHWList = new ArrayList<PollutionScoreResponseVo>();
//
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_ConsentedHWData : " + days);
//
//								query.addCriteria(Criteria.where("Industry_id").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("name").is(param));
//
//								// System.out.println(mongoTemplate.count(query, Consent_HW_Comparison.class));
//
//								List<Consent_HW_Comparison> filteredFuelList = mongoTemplate.find(query,
//										Consent_HW_Comparison.class);
//								Page<Consent_HW_Comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList,
//										page, () -> mongoTemplate.count(query, Consent_HW_Comparison.class));
//
//								List<Consent_HW_Comparison> cHW = new ArrayList<Consent_HW_Comparison>(cPage.toList());
//
//								for (Consent_HW_Comparison chwlst : cHW) {
//
//									cHWList.add(new PollutionScoreResponseVo(chwlst.get_id(), chwlst.getIndustryName(),
//											chwlst.getIndustry_id(), chwlst.getUOM(), chwlst.getName(),
//											chwlst.getUOM_name()));
//
//									if (count == 1) {
//
//										consentedComparisonList.add(new PollutionScoreResponseVo(chwlst.get_id(),
//												chwlst.getIndustryName(), chwlst.getIndustry_id(), chwlst.getUOM(),
//												chwlst.getName(), chwlst.getUOM_name()));
//									}
//									resMap.put(daysMap1.get(days), cHWList);
//
//								}
//
//							}
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("EsrHW")) {
//
//						logger.info("getPollutionScoreCard_ConsentedHWData");
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								count++;
//								List<PollutionScoreResponseVo> esrHWList = new ArrayList<PollutionScoreResponseVo>();
//
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_ConsentedHWData : " + days);
//
//								query.addCriteria(Criteria.where("Industry_id").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("name").is(param));
//
//								// System.out.println(mongoTemplate.count(query, Esr_HW_Comparison.class));
//
//								List<Esr_HW_Comparison> filteredFuelList = mongoTemplate.find(query,
//										Esr_HW_Comparison.class);
//								Page<Esr_HW_Comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList, page,
//										() -> mongoTemplate.count(query, Esr_HW_Comparison.class));
//
//								List<Esr_HW_Comparison> esrHW = new ArrayList<Esr_HW_Comparison>(cPage.toList());
//
//								for (Esr_HW_Comparison esrhw : esrHW) {
//
//									String created = esrhw.getCreated();
//
//									Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created);
//									Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(days + " 00:00:00");
//
//									logger.info("created date: " + date2);
//									logger.info("filter date: " + date1);
//
//									if (date2.after(date1)) {
//
//										esrHWList.add(new PollutionScoreResponseVo(esrhw.get_id(),
//												esrhw.getIndustry_id(), esrhw.getCompany_name(), esrhw.getCreated(),
//												esrhw.getName(), esrhw.getUom(), esrhw.getUOM_name()));
//
//										if (count == 1) {
//											esrComparisonList.add(new PollutionScoreResponseVo(esrhw.get_id(),
//													esrhw.getIndustry_id(), esrhw.getCompany_name(), esrhw.getCreated(),
//													esrhw.getName(), esrhw.getUom(), esrhw.getUOM_name()));
//
//										}
//									}
//
//									resMap.put(daysMap1.get(days), esrHWList);
//								}
//
//								// esrFuelList.clear();
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//
//					if (type.equalsIgnoreCase("ConsentedRESOURCES")) {
//
//						logger.info("getPollutionScoreCard_ConsentedRESOURCESData");
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//						List<PollutionScoreResponseVo> cFuelList = new ArrayList<PollutionScoreResponseVo>();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_ConsentedRESOURCESData : " + days);
//								count++;
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//								query.addCriteria(Criteria.where("applicationCreatedOn").gt(days));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("rawMaterialName").is(param));
//
//								// System.out.println(mongoTemplate.count(query,
//								// consent_RESOURCES_comparison.class));
//
//								List<Consent_RESOURCES_comparison> filteredFuelList = mongoTemplate.find(query,
//										Consent_RESOURCES_comparison.class);
//								Page<Consent_RESOURCES_comparison> cPage = PageableExecutionUtils.getPage(
//										filteredFuelList, page,
//										() -> mongoTemplate.count(query, Consent_RESOURCES_comparison.class));
//
//								List<Consent_RESOURCES_comparison> consentFuelList = new ArrayList<Consent_RESOURCES_comparison>(
//										cPage.toList());
//
//								for (Consent_RESOURCES_comparison cFuel : consentFuelList) {
//
//									cFuelList.add(new PollutionScoreResponseVo(cFuel.get_id(), cFuel.getIndustryId(),
//											cFuel.getIndustryName(), cFuel.getRawMaterialName(), cFuel.getUom(),
//											cFuel.getName(),cFuel.getApplicationCreatedOn()));
//
//									if (count == 1) {
//
//										consentedComparisonList.add(new PollutionScoreResponseVo(cFuel.get_id(),
//												cFuel.getIndustryId(), cFuel.getIndustryName(),
//												cFuel.getRawMaterialName(), cFuel.getUom(), cFuel.getName(),cFuel.getApplicationCreatedOn()));
//									}
//
//									resMap.put(daysMap1.get(days), cFuelList);
//
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("EsrRESOURCES")) {
//
//						logger.info("getPollutionScoreCard_ESRResourcesData");
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//
//								List<PollutionScoreResponseVo> eResList = new ArrayList<PollutionScoreResponseVo>();
//								count++;
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_ESRResourcesData : " + days);
//
//								String[] dateParts = days.split("-");
//
//								String year = dateParts[0];
//
//								int inum = Integer.parseInt(year);
//
//								// query.addCriteria(Criteria.where("finantialYear").gte(inum));
//
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("rawMaterialName").is(param));
//
//								// System.out.println(mongoTemplate.count(query,
//								// ESR_RESOURCES_comparison.class));
//
//								List<ESR_RESOURCES_comparison> filteredResList = mongoTemplate.find(query,
//										ESR_RESOURCES_comparison.class);
//								Page<ESR_RESOURCES_comparison> cPage = PageableExecutionUtils.getPage(filteredResList,
//										page, () -> mongoTemplate.count(query, ESR_RESOURCES_comparison.class));
//
//								List<ESR_RESOURCES_comparison> esrResList = new ArrayList<ESR_RESOURCES_comparison>(
//										cPage.toList());
//
//								for (ESR_RESOURCES_comparison esrRes : esrResList) {
//
//									int fyear = esrRes.getFinantialyear();
//
//									if (fyear >= inum) {
//										eResList.add(
//												new PollutionScoreResponseVo(esrRes.get_id(), esrRes.getCompanyname(),
//														esrRes.getIndustryId(), esrRes.getRawMaterialName(),
//														esrRes.getName(), esrRes.getUom(), esrRes.getFinantialyear()));
//										if (count == 1) {
//											esrComparisonList.add(new PollutionScoreResponseVo(esrRes.get_id(),
//													esrRes.getCompanyname(), esrRes.getIndustryId(),
//													esrRes.getRawMaterialName(), esrRes.getName(), esrRes.getUom(),
//													esrRes.getFinantialyear()));
//
//										}
//										resMap.put(daysMap1.get(days), eResList);
//
//									}
//								}
//
//							}
//							responseMap.put(type, resMap);
//						}
//					}
//					if (type.equalsIgnoreCase("ConsentedSku")) {
//
//						logger.info("getPollutionScoreCard_ConsentedSkuData");
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//						List<PollutionScoreResponseVo> cSkuList = new ArrayList<PollutionScoreResponseVo>();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_ConsentedSkuData : " + days);
//								count++;
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("productname").is(param));
//
//								// System.out.println(mongoTemplate.count(query,
//								// Consent_SKU_comparison.class));
//
//								List<Consent_SKU_comparison> filteredList = mongoTemplate.find(query,
//										Consent_SKU_comparison.class);
//								Page<Consent_SKU_comparison> cPage = PageableExecutionUtils.getPage(filteredList, page,
//										() -> mongoTemplate.count(query, Consent_SKU_comparison.class));
//
//								List<Consent_SKU_comparison> consentSkuList = new ArrayList<Consent_SKU_comparison>(
//										cPage.toList());
//
//								for (Consent_SKU_comparison cSku : consentSkuList) {
//
//									cSkuList.add(new PollutionScoreResponseVo(cSku.get_id(), cSku.getIndustryId(),
//											cSku.getIndustryName(), cSku.getType(), cSku.getRegion(),
//											cSku.getSubRegion(), cSku.getProductname(), cSku.getUom(), cSku.getName()));
//									if (count == 1) {
//										consentedComparisonList.add(new PollutionScoreResponseVo(cSku.get_id(),
//												cSku.getIndustryId(), cSku.getIndustryName(), cSku.getType(),
//												cSku.getRegion(), cSku.getSubRegion(), cSku.getProductname(),
//												cSku.getUom(), cSku.getName()));
//
//									}
//									resMap.put(daysMap1.get(days), cSkuList);
//
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//					if (type.equalsIgnoreCase("EsrSku")) {
//
//						logger.info("getPollutionScoreCard_EsrSkuData");
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//						List<PollutionScoreResponseVo> eSkuList = new ArrayList<PollutionScoreResponseVo>();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_EsrSkuData : " + days);
//								count++;
//								String[] dateParts = days.split("-");
//
//								String year = dateParts[0];
//
//								int inum = Integer.parseInt(year);
//
//								query.addCriteria(Criteria.where("finantialYear").gte(inum));
//
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								if (StringUtils.hasText(param))
//									query.addCriteria(Criteria.where("productName").is(param));
//
//								// System.out.println(mongoTemplate.count(query,
//								// ESR_SKU_comparison.class));
//
//								List<ESR_SKU_comparison> filteredList = mongoTemplate.find(query,
//										ESR_SKU_comparison.class);
//								Page<ESR_SKU_comparison> cPage = PageableExecutionUtils.getPage(filteredList, page,
//										() -> mongoTemplate.count(query, ESR_SKU_comparison.class));
//
//								List<ESR_SKU_comparison> esrSkuList = new ArrayList<ESR_SKU_comparison>(cPage.toList());
//
//								for (ESR_SKU_comparison eSku : esrSkuList) {
//
//									eSkuList.add(new PollutionScoreResponseVo(eSku.get_id(), eSku.getIndustryId(),
//											eSku.getCompanyName(), eSku.getProductname(), eSku.getUom(), eSku.getName(),
//											eSku.getFinantialYear()));
//									if (count == 1) {
//										esrComparisonList.add(new PollutionScoreResponseVo(eSku.get_id(),
//												eSku.getIndustryId(), eSku.getCompanyName(), eSku.getProductname(),
//												eSku.getUom(), eSku.getName(), eSku.getFinantialYear()));
//
//									}
//									resMap.put(daysMap1.get(days), eSkuList);
//
//								}
//
//							}
//
//							responseMap.put(type, resMap);
//
//						}
//					}
//
//					if (type.equalsIgnoreCase("ConsentedStack")) {
//
//						logger.info("getPollutionScoreCard_ConsentedStackData");
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//
//								List<PollutionScoreResponseVo> cStList = new ArrayList<PollutionScoreResponseVo>();
//								count++;
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_ConsentedStackData : " + days);
//
//								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));
//
//								// System.out.println(mongoTemplate.count(query,Consent_STACK_comparison.class));
//
//								List<Consent_STACK_comparison> filteredStackList = mongoTemplate.find(query,
//										Consent_STACK_comparison.class);
//								Page<Consent_STACK_comparison> cPage = PageableExecutionUtils.getPage(filteredStackList,
//										page, () -> mongoTemplate.count(query, Consent_STACK_comparison.class));
//
//								List<Consent_STACK_comparison> cStackList = new ArrayList<Consent_STACK_comparison>(
//										cPage.toList());
//
//								for (Consent_STACK_comparison cStack : cStackList) {
//
//									cStList.add(new PollutionScoreResponseVo(cStack.get_id(), cStack.getIndustryId(),
//											cStack.getIndustryName(), cStack.getStackNumber(),
//											cStack.getStackAttachedTo(), cStack.getCapacityUom(),
//											cStack.getStackFuelType(), cStack.getStackFuelQuantity(),
//											cStack.getFuelQtyUom()));
//
//									if (count == 1) {
//										consentedComparisonList.add(new PollutionScoreResponseVo(cStack.get_id(),
//												cStack.getIndustryId(), cStack.getIndustryName(),
//												cStack.getStackNumber(), cStack.getStackAttachedTo(),
//												cStack.getCapacityUom(), cStack.getStackFuelType(),
//												cStack.getStackFuelQuantity(), cStack.getFuelQtyUom()));
//									}
//
//									resMap.put(daysMap1.get(days), cStList);
//
//								}
//
//							}
//							responseMap.put(type, resMap);
//
//						}
//
//					}
//					if (type.equalsIgnoreCase("OCEMSData")) {
//
//						logger.info("getPollutionScoreCard_OCEMSData");
//
//						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();
//
//						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMap();
//
//						if (null != cf) {
//							int count = 0;
//							for (String days : daysMap1.keySet()) {
//
//								List<PollutionScoreResponseVo> ocemsList = new ArrayList<PollutionScoreResponseVo>();
//								count++;
//								Query query = new Query();
//								logger.info("getPollutionScoreCard_OCEMSData : " + days);
//
//								query.addCriteria(Criteria.where("industry_name").is(cf.getIndustryName()));
//
//								// System.out.println(mongoTemplate.count(query,OCEMS_data.class));
//
//								List<OCEMS_data> filteredList = mongoTemplate.find(query, OCEMS_data.class);
//								Page<OCEMS_data> cPage = PageableExecutionUtils.getPage(filteredList, page,
//										() -> mongoTemplate.count(query, OCEMS_data.class));
//
//								List<OCEMS_data> ocmsList = new ArrayList<OCEMS_data>(cPage.toList());
//
//								for (OCEMS_data ocmsListData : ocmsList) {
//
//									ocemsList.add(new PollutionScoreResponseVo(ocmsListData.getSite_id(),
//											ocmsListData.getIndustry_name(), ocmsListData.getIndustry(),
//											ocmsListData.getStation_name(), ocmsListData.getParameter_name(),
//											ocmsListData.getTime_stamp(), ocmsListData.getValue(),
//											ocmsListData.getUnits()));
//
//									if (count == 1) {
//										ocemsList.add(new PollutionScoreResponseVo(ocmsListData.getSite_id(),
//												ocmsListData.getIndustry_name(), ocmsListData.getIndustry(),
//												ocmsListData.getStation_name(), ocmsListData.getParameter_name(),
//												ocmsListData.getTime_stamp(), ocmsListData.getValue(),
//												ocmsListData.getUnits()));
//									}
//
//									resMap.put(daysMap1.get(days), ocemsList);
//
//								}
//
//							}
//							responseMap.put(type, resMap);
//
//						}
//
//					}
//
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
			//List<Consented_Air_Pollution_Comparison> cscList = mongoTemplate.find(getConsentQueryObj(industryId), Consented_Air_Pollution_Comparison.class);
			getConsentQueryObj(industryId);
		}
		return paramList;
		
	}
	
	private Query getConsentQueryObj(long industryId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("industryId").is(industryId));
			List<String> distValues = mongoTemplate.findDistinct("parameter",Consented_Air_Pollution_Comparison.class, String.class);
			return query;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
