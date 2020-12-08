package com.eai.idss.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eai.idss.vo.TileVo;

public class IDSSUtil {

	public static List<String> getRegionList() {
		List<String> regionList = new ArrayList<String>();
		regionList.add("Mumbai");
		regionList.add("Kalyan");
		regionList.add("Kolhapur");
		regionList.add("Nagpur");
		regionList.add("Aurangabad");
		regionList.add("Nashik");
		regionList.add("Navi Mumbai");
		regionList.add("Pune");
		regionList.add("Raigad");
		regionList.add("Thane");
		regionList.add("Chandrapur");
		regionList.add("Amravati");
		return regionList;
	}

	public static Map<String, List<TileVo>> getRegionMap() {
		Map<String, List<TileVo>> regionMap = new LinkedHashMap<String, List<TileVo>>();
		for (String region : IDSSUtil.getRegionList()) {
			regionMap.put(region, new ArrayList<TileVo>());
		}
		return regionMap;
	}

	public static List<String> getLegalActionsList() {
		List<String> legalActionsList = new ArrayList<String>();
		legalActionsList.add("WN");
		legalActionsList.add("SCN");
		legalActionsList.add("PD");
		legalActionsList.add("CD");
		return legalActionsList;
	}

	public static List<String> getScaleList() {
		List<String> scaleList = new ArrayList<String>();
		scaleList.add("LSI");
		scaleList.add("MSI");
		scaleList.add("SSI");
		return scaleList;
	}

	public static List<String> getTypeList() {
		List<String> typeList = new ArrayList<String>();
		typeList.add("Chemical");
		typeList.add("Metal");
		typeList.add("Paper");
		typeList.add("Ceramic");
		return typeList;
	}

	public static List<String> getCategoryList() {
		List<String> categoryList = new ArrayList<String>();
		categoryList.add("Red");
		categoryList.add("Orange");
		categoryList.add("Green");
		categoryList.add("White");
		return categoryList;
	}

	public static List<String> getComplianceScoreList() {
		List<String> complianceScoreList = new ArrayList<String>();
		complianceScoreList.add("10");
		complianceScoreList.add("20");
		complianceScoreList.add("30");
		complianceScoreList.add("40");
		return complianceScoreList;
	}

	public static List<String> getLegalActionsDropdownList() {
		List<String> legalActionsList = new ArrayList<String>();
		legalActionsList.add("10-20");
		legalActionsList.add("21-30");
		legalActionsList.add("31-40");
		legalActionsList.add("41-50");
		return legalActionsList;
	}

	public static List<String> getPendingCasesList() {
		List<String> pendingCasesList = new ArrayList<String>();
		pendingCasesList.add("1-10");
		pendingCasesList.add("11-20");
		pendingCasesList.add("21-30");
		pendingCasesList.add("31-40");
		return pendingCasesList;
	}

	public static Map<String, String> getFutureDaysMap() {
		LocalDateTime currentTime = LocalDateTime.now();
		String currentDay = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.plusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60DaysBack = currentTime.plusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.plusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.plusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(currentDay, "_pastDue");
		daysMap.put(date30DaysBack, "_30Days");
		daysMap.put(date60DaysBack, "_60Days");
		daysMap.put(date90DaysBack, "_90Days");
		daysMap.put(date120DaysBack, "_120Days");
		return daysMap;
	}

	public static Map<String, String> getPastDaysMap() {
		LocalDateTime currentTime = LocalDateTime.now();
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60DaysBack = currentTime.minusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(date30DaysBack, "_30Days");
		daysMap.put(date60DaysBack, "_60Days");
		daysMap.put(date90DaysBack, "_90Days");
		daysMap.put(date120DaysBack, "_120Days");
		return daysMap;
	}

	public static Map<String, List<String>> getPastAndFutureDaysMap() {
		LocalDateTime currentTime = LocalDateTime.now();
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		String date30Days = currentTime.plusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90Days = currentTime.plusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120Days = currentTime.plusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, List<String>> daysMap = new LinkedHashMap<String, List<String>>();

		daysMap.put("_30Days", Arrays.asList(date30DaysBack, date30Days));
		daysMap.put("_90Days", Arrays.asList(date90DaysBack, date90Days));
		daysMap.put("_120Days", Arrays.asList(date120DaysBack, date120Days));
		return daysMap;
	}

	public static Map<String, String> getPastDaysMapForLegal() {
		LocalDateTime currentTime = LocalDateTime.now();
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(date30DaysBack, "_30Days");
		daysMap.put(date90DaysBack, "_90Days");
		daysMap.put(date120DaysBack, "120Days");
		daysMap.put("1970-01-01", "_allDays");
		return daysMap;
	}

	public static Map<String, List<String>> getFutureDaysMapForVisits() {
		LocalDateTime currentTime = LocalDateTime.now();
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date7Days = currentTime.plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date14Days = currentTime.plusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30Days = currentTime.plusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60Days = currentTime.plusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90Days = currentTime.plusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date180Days = currentTime.plusDays(180).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();

		days.add(date7Days);
		days.add(dateToday);
		daysMap.put("_7Days", days);
		days = new ArrayList<String>();
		days.add(date14Days);
		days.add(date7Days);
		daysMap.put("_7DaysOverDays", days);

		days = new ArrayList<String>();
		days.add(date30Days);
		days.add(dateToday);
		daysMap.put("_30Days", days);
		days = new ArrayList<String>();
		days.add(date60Days);
		days.add(date30Days);
		daysMap.put("_30DaysOverDays", days);

		days = new ArrayList<String>();
		days.add(date90Days);
		days.add(dateToday);
		daysMap.put("_90Days", days);
		days = new ArrayList<String>();
		days.add(date180Days);
		days.add(date90Days);
		daysMap.put("_90DaysOverDays", days);

		return daysMap;
	}

	public static Map<String, List<String>> getDaysMapForDashboard() {
		LocalDateTime currentTime = LocalDateTime.now();
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date14DaysBack = currentTime.minusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60DaysBack = currentTime.minusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date180DaysBack = currentTime.minusDays(180).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();

		days.add(dateToday);
		days.add(date7DaysBack);
		daysMap.put("_7Days", days);
		days = new ArrayList<String>();
		days.add(date7DaysBack);
		days.add(date14DaysBack);
		daysMap.put("_7DaysOverDays", days);

		days = new ArrayList<String>();
		days.add(dateToday);
		days.add(date30DaysBack);
		daysMap.put("_30Days", days);
		days = new ArrayList<String>();
		days.add(date30DaysBack);
		days.add(date60DaysBack);
		daysMap.put("_30DaysOverDays", days);

		days = new ArrayList<String>();
		days.add(dateToday);
		days.add(date90DaysBack);
		daysMap.put("_90Days", days);
		days = new ArrayList<String>();
		days.add(date90DaysBack);
		days.add(date180DaysBack);
		daysMap.put("_90DaysOverDays", days);

		return daysMap;
	}

	public static Map<String, List<String>> getDaysMapForLegal() {
		LocalDateTime currentTime = LocalDateTime.now();
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date5DaysBack = currentTime.minusDays(5).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date15DaysBack = currentTime.minusDays(15).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();

		days.add(dateToday);
		days.add(date5DaysBack);
		daysMap.put("_0-5Days", days);

		days = new ArrayList<String>();
		days.add(date5DaysBack);
		days.add(date15DaysBack);
		daysMap.put("_5-15Days", days);

		days = new ArrayList<String>();
		days.add(date15DaysBack);
		days.add(date30DaysBack);
		daysMap.put("_15-30Days", days);

		days = new ArrayList<String>();
		days.add(date30DaysBack);
		days.add("1970-01-01");
		daysMap.put("_30-allDays", days);

		days = new ArrayList<String>();
		days.add(date90DaysBack);
		days.add("1970-01-01");
		daysMap.put("_90-allDays", days);

		days = new ArrayList<String>();
		days.add(date120DaysBack);
		days.add("1970-01-01");
		daysMap.put("_120-allDays", days);

		return daysMap;
	}

	public static Map<String, List<String>> getDaysMapForVisits() {
		LocalDateTime currentTime = LocalDateTime.now();
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date15DaysBack = currentTime.minusDays(15).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();

		days.add(dateToday);
		days.add(date7DaysBack);
		daysMap.put("_0-7Days", days);

		days = new ArrayList<String>();
		days.add(date7DaysBack);
		days.add(date15DaysBack);
		daysMap.put("_7-15Days", days);

		days = new ArrayList<String>();
		days.add(date15DaysBack);
		days.add(date30DaysBack);
		daysMap.put("_15-30Days", days);

		days = new ArrayList<String>();
		days.add(date30DaysBack);
		days.add("1970-01-01");
		daysMap.put("_30-allDays", days);

		days = new ArrayList<String>();
		days.add(date90DaysBack);
		days.add("1970-01-01");
		daysMap.put("_90-allDays", days);

		days = new ArrayList<String>();
		days.add(date120DaysBack);
		days.add("1970-01-01");
		daysMap.put("_120-allDays", days);

		return daysMap;
	}

	public static Map<String, String> getPastDaysMapForPollutionScoreCard() {
		LocalDateTime currentTime = LocalDateTime.now();
		String date1yearDaysBack = currentTime.minusDays(365).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date2yearBack = currentTime.minusDays(730).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date3yearBack = currentTime.minusDays(1095).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date4yearBack = currentTime.minusDays(1460).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String, String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(date1yearDaysBack, "1_Year");
		daysMap.put(date2yearBack, "2_Years");
		daysMap.put(date3yearBack, "3_Years");
		daysMap.put(date4yearBack, "4_Years");
		daysMap.put("1970-01-01", "_allDays");
		return daysMap;
	}

	public static List<String> getConsentedESRAirParam() {
		List<String> consentedESRAirParamList = new ArrayList<String>();
		consentedESRAirParamList.add("SO2");
		consentedESRAirParamList.add("NO2");
		consentedESRAirParamList.add("PM");

		return consentedESRAirParamList;
	}

	public static List<String> getConsentedESRWaterParam() {
		List<String> consentedESRWaterParamList = new ArrayList<String>();
		consentedESRWaterParamList.add("treatedEffluentBod");
		consentedESRWaterParamList.add("treatedEffluentCod");
		consentedESRWaterParamList.add("EFFLUENT");
		consentedESRWaterParamList.add("CHEMICAL SLUDGE");
		consentedESRWaterParamList.add("BOD");
		consentedESRWaterParamList.add("COD");
		consentedESRWaterParamList.add("TDS");
		consentedESRWaterParamList.add("TSS");
		consentedESRWaterParamList.add("SS");
		consentedESRWaterParamList.add("CHLORIDES");

		return consentedESRWaterParamList;
	}

	public static List<String> getConsentedESRStackParam() {
		List<String> consentedESRStackParamList = new ArrayList<String>();
		consentedESRStackParamList.add("Stack");

		return consentedESRStackParamList;
	}

	public static List<String> getConsentedESRFuelParam() {
		List<String> consentedESRFuelParamList = new ArrayList<String>();
		consentedESRFuelParamList.add("Electricity");
		consentedESRFuelParamList.add("PNG");
		consentedESRFuelParamList.add("HSD");
		consentedESRFuelParamList.add("Petrol");
		consentedESRFuelParamList.add("Coal");

		return consentedESRFuelParamList;
	}

	public static List<String> getConsentedESRHWParam() {
		List<String> consentedESRHWParamList = new ArrayList<String>();
		consentedESRHWParamList.add("28.1 Residues and wastes*");
		consentedESRHWParamList.add("34.3 Chemical sludge from waste water treatment");
		consentedESRHWParamList.add("1.9 ETP sludge containing hazardous constituent");
		consentedESRHWParamList.add("12.1 Acid residue");

		return consentedESRHWParamList;
	}

	public static List<String> getConsentedESRResourcesParam() {
		List<String> consentedESRResourceParamList = new ArrayList<String>();
		consentedESRResourceParamList.add("M.S INGOTS,BILLETS,SCRAP, M.S SPONGE IRON");
		consentedESRResourceParamList.add("MS PLATE");
		consentedESRResourceParamList.add("NITRO BENZENE, IMPORTED MATERIAL, AMINO ACID POWDER");
		consentedESRResourceParamList.add("MS PIPE");
		consentedESRResourceParamList.add("MS ANGEL");

		return consentedESRResourceParamList;
	}

	public static List<String> getConsentedESRSkuParam() {
		List<String> consentedESRSkuParamList = new ArrayList<String>();
		consentedESRSkuParamList.add("M.S. TMT BARS &,ROUND BARS, CTD BARS ,M.S. INGOT, BILLETS, RUNNERS, RI...");
		consentedESRSkuParamList.add("MICRONUTRIENTS & BIO FERTILIZERS");
		consentedESRSkuParamList.add("FABRICATION JOB");
		consentedESRSkuParamList.add("STONE CRUSHED");
		consentedESRSkuParamList.add("AUTOMOBILE COMPONENT, CAST COMPONENT, GENERAL FABRICATION");
		consentedESRSkuParamList.add("PRIMER");
		consentedESRSkuParamList.add("EMULSION PAINT");
		consentedESRSkuParamList.add("ACRYLIC DISTEMPER");
		consentedESRSkuParamList.add("CEMENT BASED PUTTY");
		consentedESRSkuParamList.add("BRASS,COPPER CIRCLE/SHEET/UTENSIL ,RODS AND PIPES");
		consentedESRSkuParamList.add("GRILL, GATE & OTHER FABRICATION");
		consentedESRSkuParamList.add("S.S. UTENCILS");
		consentedESRSkuParamList.add("ENGINEERING & FABRICATION JOB WORK");
		consentedESRSkuParamList.add("SUGAR");
		
		
		return consentedESRSkuParamList;
	}
	public static List<String> getOCEMSParam() {
		List<String> ocemsParamList = new ArrayList<String>();
		ocemsParamList.add("Flow");
		ocemsParamList.add("PM");

		return ocemsParamList;
	}

}
