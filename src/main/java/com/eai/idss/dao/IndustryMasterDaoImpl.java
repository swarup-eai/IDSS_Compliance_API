package com.eai.idss.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.LegalDataMaster;
import com.eai.idss.model.OCEMS_data;
import com.eai.idss.model.Consent_RESOURCES_comparison;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.IndustryMasterRequest;
import com.eai.idss.vo.PollutionScoreFilter;
import com.eai.idss.vo.PollutionScoreResponseVo;
import com.eai.idss.model.Consent_FUEL_comparison;
import com.eai.idss.model.Consent_HW_Comparison;
import com.eai.idss.model.Consent_SKU_comparison;
import com.eai.idss.model.Consent_STACK_comparison;
import com.eai.idss.model.Consent_WATER_comparison;
//import com.eai.idss.model.Consent_FUEL_comparison;
//import com.eai.idss.model.Consent_WATER_comparison;
import com.eai.idss.model.Consented_Air_Pollution_Comparison;
import com.eai.idss.model.ESR_Air_Pollution_Comparison;
import com.eai.idss.model.ESR_FUEL_comparison;
import com.eai.idss.model.ESR_RESOURCES_comparison;
import com.eai.idss.model.ESR_SKU_comparison;
import com.eai.idss.model.ESR_WATER_comparison;

//import com.eai.idss.model.ESR_Air_Pollution_Comparison;
//import com.eai.idss.model.ESR_WATER_comparison;
@Repository
public class IndustryMasterDaoImpl implements IndustryMasterDao {

	@Autowired
	MongoTemplate mongoTemplate;
	public static final Logger logger = Logger.getLogger(IndustryMasterDaoImpl.class);
	public static List<PollutionScoreResponseVo> consentedComparisonList = new ArrayList<PollutionScoreResponseVo>();
	public static List<PollutionScoreResponseVo> esrComparisonList = new ArrayList<PollutionScoreResponseVo>();

	public List<IndustryMaster> getIndustryMasterPaginatedRecords(IndustryMasterRequest imr, Pageable page) {

		Query query = new Query().with(page);
		if (null != imr) {
			if (StringUtils.hasText(imr.getRegion()) && !"All".equalsIgnoreCase(imr.getRegion()))
				query.addCriteria(Criteria.where("region").is(imr.getRegion()));
			if (StringUtils.hasText(imr.getCategory()) && !"All".equalsIgnoreCase(imr.getCategory()))
				query.addCriteria(Criteria.where("category").is(imr.getCategory()));
			if (StringUtils.hasText(imr.getScale()) && !"All".equalsIgnoreCase(imr.getScale()))
				query.addCriteria(Criteria.where("scale").is(imr.getScale()));
			if (StringUtils.hasText(imr.getType()) && !"All".equalsIgnoreCase(imr.getType()))
				query.addCriteria(Criteria.where("type").is(imr.getType()));
			if (null != imr.getComplianceScore())
				query.addCriteria(Criteria.where("complianceScore").is(imr.getComplianceScore()));
			if (StringUtils.hasText(imr.getLegalActions()) && !"All".equalsIgnoreCase(imr.getLegalActions()))
				query.addCriteria(Criteria.where("legalActions").is(imr.getLegalActions()));
			if (StringUtils.hasText(imr.getPendingCases()) && !"All".equalsIgnoreCase(imr.getPendingCases()))
				query.addCriteria(Criteria.where("pendingCases").is(imr.getPendingCases()));
		}

		System.out.println(mongoTemplate.count(query, IndustryMaster.class));

		List<IndustryMaster> filteredIndustryMaster = mongoTemplate.find(query, IndustryMaster.class);
		Page<IndustryMaster> imPage = PageableExecutionUtils.getPage(filteredIndustryMaster, page,
				() -> mongoTemplate.count(query, IndustryMaster.class));

		return imPage.toList();
	}

	public boolean saveIndustryMasterFilter(IndustryMasterRequest imr, String filterName) {

		return true;
	}

	public Map<String, List<LegalDataMaster>> getByIndustryNameComplianceScoreData(ComlianceScoreFilter cf,
			Pageable page) {
		try {

			logger.info("getComplainceScoreCardData");

			Map<String, String> daysMap = IDSSUtil.getPastDaysMapForLegal();
			Map<String, List<LegalDataMaster>> tileMap = new LinkedHashMap<String, List<LegalDataMaster>>();

			if (null != cf) {

				for (String days : daysMap.keySet()) {
					Query query = new Query().with(page);
					logger.info("getComplainceScoreCardData : " + days);

					query.addCriteria(Criteria.where("visitedDate")
							.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(days + " 00:00:00.000+0000")));

					if (StringUtils.hasText(cf.getIndustryName()))
						query.addCriteria(Criteria.where("industryName").is(cf.getIndustryName()));

					System.out.println(mongoTemplate.count(query, LegalDataMaster.class));

					List<LegalDataMaster> filteredLegalList = mongoTemplate.find(query, LegalDataMaster.class);
					Page<LegalDataMaster> cPage = PageableExecutionUtils.getPage(filteredLegalList, page,
							() -> mongoTemplate.count(query, LegalDataMaster.class));

					List<LegalDataMaster> ok = new ArrayList<LegalDataMaster>(cPage.toList());

					tileMap.put(daysMap.get(days), ok);

				}
				return tileMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<LegalDataMaster> getDataBetweenDuration(ComlianceScoreFilter cf, Pageable page) {
		try {
			Query query = new Query().with(page);
			if (null != cf) {
				String[] d = cf.getDuration().split(",");

				LocalDateTime currentTime = LocalDateTime.now();

				LocalDate startDate = LocalDate.parse(d[0]);
				long noOfDaysBetweenStartDate = ChronoUnit.DAYS.between(startDate, currentTime);

				LocalDate endDate = LocalDate.parse(d[1]);
				long noOfDaysBetweenEndDate = ChronoUnit.DAYS.between(endDate, currentTime);

				logger.info("getComplainceScoreCardCustomFilterData");

				LocalDateTime fromDate = currentTime.minusDays(noOfDaysBetweenStartDate);
				String fromDay = fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

				LocalDateTime toDate = currentTime.minusDays(noOfDaysBetweenEndDate);
				String toDay = toDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

				query.addCriteria(Criteria.where("visitedDate")
						.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDay + " 00:00:00.000+0000"))
						.lt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDay + " 00:00:00.000+0000")));

				if (StringUtils.hasText(cf.getIndustryName()))
					query.addCriteria(Criteria.where("IndustryName").is(cf.getIndustryName()));

				System.out.println(mongoTemplate.count(query, LegalDataMaster.class));

				List<LegalDataMaster> filteredComplianceScorecardList = mongoTemplate.find(query,
						LegalDataMaster.class);

				Page<LegalDataMaster> cPage = PageableExecutionUtils.getPage(filteredComplianceScorecardList, page,
						() -> mongoTemplate.count(query, LegalDataMaster.class));

				return cPage.toList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Map<String, List<PollutionScoreResponseVo>>> getByIndustryNamePollutionScoreData(
			PollutionScoreFilter cf, Pageable page) {

		Map<String, Map<String, List<PollutionScoreResponseVo>>> responseMap = new LinkedHashMap<String, Map<String, List<PollutionScoreResponseVo>>>();

		try {

			String type = "";
			String param = "";
			logger.info("getPollutionScoreCardData");

			List<Map<String, String>> paramList = new ArrayList<>();

			paramList.addAll(cf.getParamList());

			for (int i = 0; i < paramList.size(); i++) {
				Map<String, String> hm = paramList.get(i);
				for (Entry<String, String> entry : hm.entrySet()) {
					type = entry.getKey();
					param = entry.getValue();

					// paramList.addAll(cf.getParametersList());

					if (type.equalsIgnoreCase("Consented")) {
						List<String> consentedESRAirParamList = IDSSUtil.getConsentedESRAirParam();
						if (consentedESRAirParamList.contains(param)) {
							type = "ConsentedAir";
						}

					}

					if (type.equalsIgnoreCase("ESR")) {
						List<String> consentedESRAirParamList = IDSSUtil.getConsentedESRAirParam();
						if (consentedESRAirParamList.contains(param)) {
							type = "ESRAir";
						}

					}

					if (type.equalsIgnoreCase("Consented")) {
						List<String> consentedESRWaterParamList = IDSSUtil.getConsentedESRWaterParam();

						if (consentedESRWaterParamList.contains(param)) {
							type = "ConsentedWater";
						}

					}
					if (type.equalsIgnoreCase("ESR")) {
						List<String> consentedESRWaterParamList = IDSSUtil.getConsentedESRWaterParam();

						if (consentedESRWaterParamList.contains(param)) {
							type = "ESRWater";
						}

					}

					if (type.equalsIgnoreCase("Consented")) {
						List<String> consentedESRStackParamList = IDSSUtil.getConsentedESRStackParam();
						if (consentedESRStackParamList.contains(param)) {
							type = "ConsentedStack";
						}

					}

					if (type.equalsIgnoreCase("Consented")) {
						List<String> consentedESRFuelParamList = IDSSUtil.getConsentedESRFuelParam();
						if (consentedESRFuelParamList.contains(param)) {
							type = "ConsentedFuel";
						}

					}

					if (type.equalsIgnoreCase("ESR")) {
						List<String> consentedESRFuelParamList = IDSSUtil.getConsentedESRFuelParam();
						if (consentedESRFuelParamList.contains(param)) {
							type = "ESRFuel";
						}

					}

					if (type.equalsIgnoreCase("Consented")) {
						List<String> consentedESRHWParamList = IDSSUtil.getConsentedESRHWParam();
						if (consentedESRHWParamList.contains(param)) {
							type = "ConsentedHW";
						}

					}
					if (type.equalsIgnoreCase("ESR")) {
						List<String> consentedESRHWParamList = IDSSUtil.getConsentedESRHWParam();
						if (consentedESRHWParamList.contains(param)) {
							type = "EsrHW";
						}

					}

					if (type.equalsIgnoreCase("Consented")) {
						List<String> consentedESRResourceParamList = IDSSUtil.getConsentedESRResourcesParam();
						if (consentedESRResourceParamList.contains(param)) {
							type = "ConsentedRESOURCES";
						}

					}
					if (type.equalsIgnoreCase("ESR")) {
						List<String> consentedESRResourceParamList = IDSSUtil.getConsentedESRResourcesParam();
						if (consentedESRResourceParamList.contains(param)) {
							type = "EsrRESOURCES";
						}

					}

					if (type.equalsIgnoreCase("Consented")) {
						List<String> consentedESRSkuParamList = IDSSUtil.getConsentedESRSkuParam();
						if (consentedESRSkuParamList.contains(param)) {
							type = "ConsentedSku";
						}

					}
					if (type.equalsIgnoreCase("ESR")) {
						List<String> consentedESRSkuParamList = IDSSUtil.getConsentedESRSkuParam();
						if (consentedESRSkuParamList.contains(param)) {
							type = "EsrSku";
						}

					}

					if (type.equalsIgnoreCase("OCEMS")) {
						List<String> ocemsParamList = IDSSUtil.getOCEMSParam();
						if (ocemsParamList.contains(param)) {
							type = "OCEMSData";
						}
					}

					if (type.equalsIgnoreCase("ConsentedAir")) {

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						logger.info("getPollutionScoreCard_ConsentedAirData");

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();

						if (null != cf) {

							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);
								List<PollutionScoreResponseVo> consentedList = new ArrayList<PollutionScoreResponseVo>();

								logger.info("getPollutionScoreCard_ConsentedAirData : " + days);
								// query.addCriteria(Criteria.where("created").gte(days));

								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("parameter").is(param));

								System.out
										.println(mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));

								List<Consented_Air_Pollution_Comparison> filteredList = mongoTemplate.find(query,
										Consented_Air_Pollution_Comparison.class);
								Page<Consented_Air_Pollution_Comparison> cPage = PageableExecutionUtils.getPage(
										filteredList, page,
										() -> mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));

								List<Consented_Air_Pollution_Comparison> consentedAirList = new ArrayList<Consented_Air_Pollution_Comparison>(
										cPage.toList());
								int count = 0;
								for (Consented_Air_Pollution_Comparison cAir : consentedAirList) {

									String created = cAir.getCreated();

									Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created);
									Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(days + " 00:00:00");

									logger.info("created date: " + date2);
									logger.info("filter date: " + date1);

									if (date2.after(date1)) {
										count++;
										consentedList.add(new PollutionScoreResponseVo(cAir.get_id(),
												cAir.getIndustryId(), cAir.getIndustryname(), cAir.getConcentration(),
												cAir.getConcentrationUom(), cAir.getCreated(), cAir.getParameter()));

										if (count == 1) {

											consentedComparisonList.add(
													new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryId(),
															cAir.getIndustryname(), cAir.getConcentration(),
															cAir.getConcentrationUom(), cAir.getParameter()));

										}
									}

									resMap.put(daysMap1.get(days), consentedList);

								}

							}

							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("ESRAir")) {

						logger.info("getPollutionScoreCard_ESRAirData");

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);

								count++;
								List<PollutionScoreResponseVo> esrList = new ArrayList<PollutionScoreResponseVo>();

								logger.info("getPollutionScoreCard_ESRAirData : " + days);
								// query.addCriteria(Criteria.where("created").gte(days));

								String[] dateParts = days.split("-");

								String year = dateParts[0];

								int inum = Integer.parseInt(year);

								query.addCriteria(Criteria.where("finantialyear").gte(inum));

								query.addCriteria(Criteria.where("industryid").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("airpollutants").is(param));

								System.out.println(mongoTemplate.count(query, ESR_Air_Pollution_Comparison.class));

								List<ESR_Air_Pollution_Comparison> filteredList = mongoTemplate.find(query,
										ESR_Air_Pollution_Comparison.class);
								Page<ESR_Air_Pollution_Comparison> cPage = PageableExecutionUtils.getPage(filteredList,
										page, () -> mongoTemplate.count(query, ESR_Air_Pollution_Comparison.class));

								List<ESR_Air_Pollution_Comparison> esrAirList = new ArrayList<ESR_Air_Pollution_Comparison>(
										cPage.toList());

								for (ESR_Air_Pollution_Comparison cAir : esrAirList) {

									esrList.add(new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryid(),
											cAir.getCompanyname(), cAir.getAirpollutantquantity(), cAir.getQtyuom(),
											cAir.getFinantialyear()));

									if (count == 1) {

										esrComparisonList
												.add(new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryid(),
														cAir.getCompanyname(), cAir.getAirpollutantquantity(),
														cAir.getQtyuom(), cAir.getFinantialyear()));

									}

									resMap.put(daysMap1.get(days), esrList);

								}

							}

							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("ConsentedWater")) {

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						logger.info("getPollutionScoreCard_ConsentedWaterData");

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();

						if (null != cf) {

							int count = 0;
							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);
								count++;
								List<PollutionScoreResponseVo> consentedList = new ArrayList<PollutionScoreResponseVo>();

								logger.info("getPollutionScoreCard_ConsentedWaterData : " + days);
								// query.addCriteria(Criteria.where("created").gte(days));

								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("parameter").is(param));

								System.out
										.println(mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));

								List<Consent_WATER_comparison> filteredList = mongoTemplate.find(query,
										Consent_WATER_comparison.class);
								Page<Consent_WATER_comparison> cPage = PageableExecutionUtils.getPage(filteredList,
										page,
										() -> mongoTemplate.count(query, Consented_Air_Pollution_Comparison.class));

								List<Consent_WATER_comparison> consentedWaterList = new ArrayList<Consent_WATER_comparison>(
										cPage.toList());

								for (Consent_WATER_comparison cAir : consentedWaterList) {

									/*
									 * String created = cAir.getCreated();
									 * 
									 * Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created); Date
									 * date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(days +
									 * " 00:00:00");
									 * 
									 * logger.info("created date: " + date2); logger.info("filter date: " + date1);
									 */
									// if (date2.after(date1)) {
									consentedList.add(new PollutionScoreResponseVo(cAir.get_id(), cAir.getIndustryId(),
											cAir.getIndustryName(), cAir.getUom(), cAir.getName(),
											cAir.getTreatedEffluentBod(), cAir.getTreatedEffluentCod(),
											cAir.getTreatedEffluentSs(), cAir.getTreatedEffluentTds(),
											cAir.getTreatedEffluentPh()));

									// }

									if (count == 1) {
										consentedComparisonList.add(new PollutionScoreResponseVo(cAir.get_id(),
												cAir.getIndustryId(), cAir.getIndustryName(), cAir.getUom(),
												cAir.getName(), cAir.getTreatedEffluentBod(),
												cAir.getTreatedEffluentCod(), cAir.getTreatedEffluentSs(),
												cAir.getTreatedEffluentTds(), cAir.getTreatedEffluentPh()));

									}

									resMap.put(daysMap1.get(days), consentedList);

								}

							}

							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("ESRWater")) {

						logger.info("getPollutionScoreCard_ESRWaterData");

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);
								count++;
								List<PollutionScoreResponseVo> esrList = new ArrayList<PollutionScoreResponseVo>();

								logger.info("getPollutionScoreCard_ESRWaterData : " + days);
								// query.addCriteria(Criteria.where("created").gte(days));

								String[] dateParts = days.split("-");

								String year = dateParts[0];

								int inDate = Integer.parseInt(year);

								// query.addCriteria(Criteria.where("finantialyear").gte(inum));

								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("waterPollutants").is(param));

								// System.out.println(mongoTemplate.count(query, ESR_WATER_comparison.class));

								List<ESR_WATER_comparison> filteredList = mongoTemplate.find(query,
										ESR_WATER_comparison.class);
								Page<ESR_WATER_comparison> cPage = PageableExecutionUtils.getPage(filteredList, page,
										() -> mongoTemplate.count(query, ESR_WATER_comparison.class));

								List<ESR_WATER_comparison> esrWaterList = new ArrayList<ESR_WATER_comparison>(
										cPage.toList());

								for (ESR_WATER_comparison eWater : esrWaterList) {

									int financialYr = eWater.getFinantialyear();

									if (financialYr >= inDate) {
										esrList.add(new PollutionScoreResponseVo(eWater.get_id(),
												eWater.getIndustryid(), eWater.getCompanyname(),
												eWater.getWaterPollutants(), eWater.getWaterPollutantQuantity(),
												eWater.getUomName(), eWater.getFinantialyear()));

										if (count == 1) {

											esrComparisonList.add(new PollutionScoreResponseVo(eWater.get_id(),
													eWater.getIndustryid(), eWater.getCompanyname(),
													eWater.getWaterPollutants(), eWater.getWaterPollutantQuantity(),
													eWater.getUomName(), eWater.getFinantialyear()));

										}

										resMap.put(daysMap1.get(days), esrList);
									}

								}

							}

							responseMap.put(type, resMap);

						}
					}

					if (type.equalsIgnoreCase("ConsentedFuel")) {

						logger.info("getPollutionScoreCard_ConsentedFuelData");

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
						List<PollutionScoreResponseVo> cFuelList = new ArrayList<PollutionScoreResponseVo>();

						if (null != cf) {

							int count = 0;
							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);
								count++;
								logger.info("getPollutionScoreCard_ConsentedFuelData : " + days);

								query.addCriteria(Criteria.where("industryid").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("fuelName").is(param));

								// System.out.println(mongoTemplate.count(query,
								// Consent_FUEL_comparison.class));

								List<Consent_FUEL_comparison> filteredFuelList = mongoTemplate.find(query,
										Consent_FUEL_comparison.class);
								Page<Consent_FUEL_comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList,
										page, () -> mongoTemplate.count(query, Consent_FUEL_comparison.class));

								List<Consent_FUEL_comparison> consentFuelList = new ArrayList<Consent_FUEL_comparison>(
										cPage.toList());

								for (Consent_FUEL_comparison cFuel : consentFuelList) {

									cFuelList.add(new PollutionScoreResponseVo(cFuel.get_id(), cFuel.getIndustryId(),
											cFuel.getIndustryName(), cFuel.getUom(), cFuel.getName(),
											cFuel.getFuelName()));

									if (count == 1) {

										consentedComparisonList.add(new PollutionScoreResponseVo(cFuel.get_id(),
												cFuel.getIndustryId(), cFuel.getIndustryName(), cFuel.getUom(),
												cFuel.getName(), cFuel.getFuelName()));
									}

									resMap.put(daysMap1.get(days), cFuelList);

								}

							}
							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("ESRFuel")) {

						logger.info("getPollutionScoreCard_ESRFuelData");

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								count++;
								List<PollutionScoreResponseVo> esrFuelList = new ArrayList<PollutionScoreResponseVo>();

								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_ESRFuelData : " + days);

								String[] dateParts = days.split("-");

								String year = dateParts[0];

								int inum = Integer.parseInt(year);

								// query.addCriteria(Criteria.where("finantialYear").gte(inum));

								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("fuelName").is(param));

								// System.out.println(mongoTemplate.count(query,
								// Consent_FUEL_comparison.class));

								List<ESR_FUEL_comparison> filteredFuelList = mongoTemplate.find(query,
										ESR_FUEL_comparison.class);
								Page<ESR_FUEL_comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList, page,
										() -> mongoTemplate.count(query, ESR_FUEL_comparison.class));

								List<ESR_FUEL_comparison> eFuelList = new ArrayList<ESR_FUEL_comparison>(
										cPage.toList());

								for (ESR_FUEL_comparison esrFuel : eFuelList) {

									int fyear = esrFuel.getFinantialYear();

									if (fyear >= inum) {
										esrFuelList.add(new PollutionScoreResponseVo(esrFuel.get_id(),
												esrFuel.getIndustryId(), esrFuel.getFuelName(), esrFuel.getFuelUom(),
												esrFuel.getName(), esrFuel.getFinantialYear()));

										if (count == 1) {

											esrComparisonList.add(new PollutionScoreResponseVo(esrFuel.get_id(),
													esrFuel.getIndustryId(), esrFuel.getFuelName(),
													esrFuel.getFuelUom(), esrFuel.getName(),
													esrFuel.getFinantialYear()));

										}
										resMap.put(daysMap1.get(days), esrFuelList);

									}
								}

							}

							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("ConsentedHW")) {

						logger.info("getPollutionScoreCard_ConsentedHWData");

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								count++;
								List<PollutionScoreResponseVo> cHWList = new ArrayList<PollutionScoreResponseVo>();

								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_ConsentedHWData : " + days);

								query.addCriteria(Criteria.where("Industry_id").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("name").is(param));

								// System.out.println(mongoTemplate.count(query, Consent_HW_Comparison.class));

								List<Consent_HW_Comparison> filteredFuelList = mongoTemplate.find(query,
										Consent_HW_Comparison.class);
								Page<Consent_HW_Comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList,
										page, () -> mongoTemplate.count(query, Consent_HW_Comparison.class));

								List<Consent_HW_Comparison> cHW = new ArrayList<Consent_HW_Comparison>(cPage.toList());

								for (Consent_HW_Comparison chwlst : cHW) {

									cHWList.add(new PollutionScoreResponseVo(chwlst.get_id(), chwlst.getIndustryName(),
											chwlst.getIndustry_id(), chwlst.getUOM(), chwlst.getName(),
											chwlst.getUOM_name()));

									if (count == 1) {

										consentedComparisonList.add(new PollutionScoreResponseVo(chwlst.get_id(),
												chwlst.getIndustryName(), chwlst.getIndustry_id(), chwlst.getUOM(),
												chwlst.getName(), chwlst.getUOM_name()));
									}
									resMap.put(daysMap1.get(days), cHWList);

								}

							}
							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("EsrHW")) {

						logger.info("getPollutionScoreCard_ConsentedHWData");
						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								count++;
								List<PollutionScoreResponseVo> esrHWList = new ArrayList<PollutionScoreResponseVo>();

								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_ConsentedHWData : " + days);

								query.addCriteria(Criteria.where("Industry_id").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("name").is(param));

								// System.out.println(mongoTemplate.count(query, Esr_HW_Comparison.class));

								List<Esr_HW_Comparison> filteredFuelList = mongoTemplate.find(query,
										Esr_HW_Comparison.class);
								Page<Esr_HW_Comparison> cPage = PageableExecutionUtils.getPage(filteredFuelList, page,
										() -> mongoTemplate.count(query, Esr_HW_Comparison.class));

								List<Esr_HW_Comparison> esrHW = new ArrayList<Esr_HW_Comparison>(cPage.toList());

								for (Esr_HW_Comparison esrhw : esrHW) {

									String created = esrhw.getCreated();

									Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created);
									Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(days + " 00:00:00");

									logger.info("created date: " + date2);
									logger.info("filter date: " + date1);

									if (date2.after(date1)) {

										esrHWList.add(new PollutionScoreResponseVo(esrhw.get_id(),
												esrhw.getIndustry_id(), esrhw.getCompany_name(), esrhw.getCreated(),
												esrhw.getName(), esrhw.getUom(), esrhw.getUOM_name()));

										if (count == 1) {
											esrComparisonList.add(new PollutionScoreResponseVo(esrhw.get_id(),
													esrhw.getIndustry_id(), esrhw.getCompany_name(), esrhw.getCreated(),
													esrhw.getName(), esrhw.getUom(), esrhw.getUOM_name()));

										}
									}

									resMap.put(daysMap1.get(days), esrHWList);
								}

								// esrFuelList.clear();
							}

							responseMap.put(type, resMap);

						}
					}

					if (type.equalsIgnoreCase("ConsentedRESOURCES")) {

						logger.info("getPollutionScoreCard_ConsentedRESOURCESData");
						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
						List<PollutionScoreResponseVo> cFuelList = new ArrayList<PollutionScoreResponseVo>();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_ConsentedRESOURCESData : " + days);
								count++;
								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("rawMaterialName").is(param));

								// System.out.println(mongoTemplate.count(query,
								// consent_RESOURCES_comparison.class));

								List<Consent_RESOURCES_comparison> filteredFuelList = mongoTemplate.find(query,
										Consent_RESOURCES_comparison.class);
								Page<Consent_RESOURCES_comparison> cPage = PageableExecutionUtils.getPage(
										filteredFuelList, page,
										() -> mongoTemplate.count(query, Consent_RESOURCES_comparison.class));

								List<Consent_RESOURCES_comparison> consentFuelList = new ArrayList<Consent_RESOURCES_comparison>(
										cPage.toList());

								for (Consent_RESOURCES_comparison cFuel : consentFuelList) {

									cFuelList.add(new PollutionScoreResponseVo(cFuel.get_id(), cFuel.getIndustryId(),
											cFuel.getIndustryName(), cFuel.getRawMaterialName(), cFuel.getUom(),
											cFuel.getName()));

									if (count == 1) {

										consentedComparisonList.add(new PollutionScoreResponseVo(cFuel.get_id(),
												cFuel.getIndustryId(), cFuel.getIndustryName(),
												cFuel.getRawMaterialName(), cFuel.getUom(), cFuel.getName()));
									}

									resMap.put(daysMap1.get(days), cFuelList);

								}

							}

							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("EsrRESOURCES")) {

						logger.info("getPollutionScoreCard_ESRResourcesData");

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {

								List<PollutionScoreResponseVo> eResList = new ArrayList<PollutionScoreResponseVo>();
								count++;
								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_ESRResourcesData : " + days);

								String[] dateParts = days.split("-");

								String year = dateParts[0];

								int inum = Integer.parseInt(year);

								// query.addCriteria(Criteria.where("finantialYear").gte(inum));

								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("rawMaterialName").is(param));

								// System.out.println(mongoTemplate.count(query,
								// ESR_RESOURCES_comparison.class));

								List<ESR_RESOURCES_comparison> filteredResList = mongoTemplate.find(query,
										ESR_RESOURCES_comparison.class);
								Page<ESR_RESOURCES_comparison> cPage = PageableExecutionUtils.getPage(filteredResList,
										page, () -> mongoTemplate.count(query, ESR_RESOURCES_comparison.class));

								List<ESR_RESOURCES_comparison> esrResList = new ArrayList<ESR_RESOURCES_comparison>(
										cPage.toList());

								for (ESR_RESOURCES_comparison esrRes : esrResList) {

									int fyear = esrRes.getFinantialyear();

									if (fyear >= inum) {
										eResList.add(
												new PollutionScoreResponseVo(esrRes.get_id(), esrRes.getCompanyname(),
														esrRes.getIndustryId(), esrRes.getRawMaterialName(),
														esrRes.getName(), esrRes.getUom(), esrRes.getFinantialyear()));
										if (count == 1) {
											esrComparisonList.add(new PollutionScoreResponseVo(esrRes.get_id(),
													esrRes.getCompanyname(), esrRes.getIndustryId(),
													esrRes.getRawMaterialName(), esrRes.getName(), esrRes.getUom(),
													esrRes.getFinantialyear()));

										}
										resMap.put(daysMap1.get(days), eResList);

									}
								}

							}
							responseMap.put(type, resMap);
						}
					}
					if (type.equalsIgnoreCase("ConsentedSku")) {

						logger.info("getPollutionScoreCard_ConsentedSkuData");
						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
						List<PollutionScoreResponseVo> cSkuList = new ArrayList<PollutionScoreResponseVo>();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_ConsentedSkuData : " + days);
								count++;
								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("productname").is(param));

								// System.out.println(mongoTemplate.count(query,
								// Consent_SKU_comparison.class));

								List<Consent_SKU_comparison> filteredList = mongoTemplate.find(query,
										Consent_SKU_comparison.class);
								Page<Consent_SKU_comparison> cPage = PageableExecutionUtils.getPage(filteredList, page,
										() -> mongoTemplate.count(query, Consent_SKU_comparison.class));

								List<Consent_SKU_comparison> consentSkuList = new ArrayList<Consent_SKU_comparison>(
										cPage.toList());

								for (Consent_SKU_comparison cSku : consentSkuList) {

									cSkuList.add(new PollutionScoreResponseVo(cSku.get_id(), cSku.getIndustryId(),
											cSku.getIndustryName(), cSku.getType(), cSku.getRegion(),
											cSku.getSubRegion(), cSku.getProductname(), cSku.getUom(), cSku.getName()));
									if (count == 1) {
										consentedComparisonList.add(new PollutionScoreResponseVo(cSku.get_id(),
												cSku.getIndustryId(), cSku.getIndustryName(), cSku.getType(),
												cSku.getRegion(), cSku.getSubRegion(), cSku.getProductname(),
												cSku.getUom(), cSku.getName()));

									}
									resMap.put(daysMap1.get(days), cSkuList);

								}

							}

							responseMap.put(type, resMap);

						}
					}
					if (type.equalsIgnoreCase("EsrSku")) {

						logger.info("getPollutionScoreCard_EsrSkuData");
						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();
						List<PollutionScoreResponseVo> eSkuList = new ArrayList<PollutionScoreResponseVo>();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {
								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_EsrSkuData : " + days);
								count++;
								String[] dateParts = days.split("-");

								String year = dateParts[0];

								int inum = Integer.parseInt(year);

								query.addCriteria(Criteria.where("finantialYear").gte(inum));

								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								if (StringUtils.hasText(param))
									query.addCriteria(Criteria.where("productName").is(param));

								// System.out.println(mongoTemplate.count(query,
								// ESR_SKU_comparison.class));

								List<ESR_SKU_comparison> filteredList = mongoTemplate.find(query,
										ESR_SKU_comparison.class);
								Page<ESR_SKU_comparison> cPage = PageableExecutionUtils.getPage(filteredList, page,
										() -> mongoTemplate.count(query, ESR_SKU_comparison.class));

								List<ESR_SKU_comparison> esrSkuList = new ArrayList<ESR_SKU_comparison>(cPage.toList());

								for (ESR_SKU_comparison eSku : esrSkuList) {

									eSkuList.add(new PollutionScoreResponseVo(eSku.get_id(), eSku.getIndustryId(),
											eSku.getCompanyName(), eSku.getProductname(), eSku.getUom(), eSku.getName(),
											eSku.getFinantialYear()));
									if (count == 1) {
										esrComparisonList.add(new PollutionScoreResponseVo(eSku.get_id(),
												eSku.getIndustryId(), eSku.getCompanyName(), eSku.getProductname(),
												eSku.getUom(), eSku.getName(), eSku.getFinantialYear()));

									}
									resMap.put(daysMap1.get(days), eSkuList);

								}

							}

							responseMap.put(type, resMap);

						}
					}

					if (type.equalsIgnoreCase("ConsentedStack")) {

						logger.info("getPollutionScoreCard_ConsentedStackData");

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMapForPollutionScoreCard();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {

								List<PollutionScoreResponseVo> cStList = new ArrayList<PollutionScoreResponseVo>();
								count++;
								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_ConsentedStackData : " + days);

								query.addCriteria(Criteria.where("industryId").is(cf.getIndustryId()));

								// System.out.println(mongoTemplate.count(query,Consent_STACK_comparison.class));

								List<Consent_STACK_comparison> filteredStackList = mongoTemplate.find(query,
										Consent_STACK_comparison.class);
								Page<Consent_STACK_comparison> cPage = PageableExecutionUtils.getPage(filteredStackList,
										page, () -> mongoTemplate.count(query, Consent_STACK_comparison.class));

								List<Consent_STACK_comparison> cStackList = new ArrayList<Consent_STACK_comparison>(
										cPage.toList());

								for (Consent_STACK_comparison cStack : cStackList) {

									cStList.add(new PollutionScoreResponseVo(cStack.get_id(), cStack.getIndustryId(),
											cStack.getIndustryName(), cStack.getStackNumber(),
											cStack.getStackAttachedTo(), cStack.getCapacityUom(),
											cStack.getStackFuelType(), cStack.getStackFuelQuantity(),
											cStack.getFuelQtyUom()));

									if (count == 1) {
										consentedComparisonList.add(new PollutionScoreResponseVo(cStack.get_id(),
												cStack.getIndustryId(), cStack.getIndustryName(),
												cStack.getStackNumber(), cStack.getStackAttachedTo(),
												cStack.getCapacityUom(), cStack.getStackFuelType(),
												cStack.getStackFuelQuantity(), cStack.getFuelQtyUom()));
									}

									resMap.put(daysMap1.get(days), cStList);

								}

							}
							responseMap.put(type, resMap);

						}

					}
					if (type.equalsIgnoreCase("OCEMSData")) {

						logger.info("getPollutionScoreCard_OCEMSData");

						Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

						Map<String, String> daysMap1 = IDSSUtil.getPastDaysMap();

						if (null != cf) {
							int count = 0;
							for (String days : daysMap1.keySet()) {

								List<PollutionScoreResponseVo> ocemsList = new ArrayList<PollutionScoreResponseVo>();
								count++;
								Query query = new Query().with(page);
								logger.info("getPollutionScoreCard_OCEMSData : " + days);

								query.addCriteria(Criteria.where("industry_name").is(cf.getIndustryName()));

								// System.out.println(mongoTemplate.count(query,OCEMS_data.class));

								List<OCEMS_data> filteredList = mongoTemplate.find(query, OCEMS_data.class);
								Page<OCEMS_data> cPage = PageableExecutionUtils.getPage(filteredList, page,
										() -> mongoTemplate.count(query, OCEMS_data.class));

								List<OCEMS_data> ocmsList = new ArrayList<OCEMS_data>(cPage.toList());

								for (OCEMS_data ocmsListData : ocmsList) {

									ocemsList.add(new PollutionScoreResponseVo(ocmsListData.getSite_id(),
											ocmsListData.getIndustry_name(), ocmsListData.getIndustry(),
											ocmsListData.getStation_name(), ocmsListData.getParameter_name(),
											ocmsListData.getTime_stamp(), ocmsListData.getValue(),
											ocmsListData.getUnits()));

									if (count == 1) {
										ocemsList.add(new PollutionScoreResponseVo(ocmsListData.getSite_id(),
												ocmsListData.getIndustry_name(), ocmsListData.getIndustry(),
												ocmsListData.getStation_name(), ocmsListData.getParameter_name(),
												ocmsListData.getTime_stamp(), ocmsListData.getValue(),
												ocmsListData.getUnits()));
									}

									resMap.put(daysMap1.get(days), ocemsList);

								}

							}
							responseMap.put(type, resMap);

						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseMap;
	}

	@Override
	public Map<String, List<PollutionScoreResponseVo>> getComparisonData() {

		logger.info("getComparisonData...");
		// Map<String, Map<String, List<PollutionScoreResponseVo>>> responseMap = new
		// LinkedHashMap<String, Map<String, List<PollutionScoreResponseVo>>>();

		Map<String, List<PollutionScoreResponseVo>> resMap = new LinkedHashMap<String, List<PollutionScoreResponseVo>>();

		resMap.put("ConsentedData", consentedComparisonList);
		resMap.put("ESRData", esrComparisonList);

		return resMap;
	}

}
