package com.eai.idss.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eai.idss.vo.TileVo;

public class IDSSUtil {

	public static List<String> getRegionList(){
		List<String> regionList = new ArrayList<String>();
		regionList.add("ALL");
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
	
	public static List<String> getSubRegion(String region){
		List<String> subRegionList = new ArrayList<String>();

		switch(region) {
			case "ALL" :
				getAllSubRegions(subRegionList);
				break;
			case "Mumbai":
				subRegionList.add("ALL");
				subRegionList.add("Mumbai I");
				subRegionList.add("Mumbai II");
				subRegionList.add("Mumbai III");
				subRegionList.add("Mumbai IV");
				break;
			case "Kalyan":
				subRegionList.add("ALL");
				subRegionList.add("Kalyan I");
				subRegionList.add("Kalyan II");
				subRegionList.add("Kalyan III");
				subRegionList.add("Bhiwandi");
				break;
			case "Kolhapur":
				subRegionList.add("ALL");
				subRegionList.add("Kolhapur");
				subRegionList.add("Sangli");
				subRegionList.add("Ratnagiri");
				subRegionList.add("Chiplun");
				break;
			case "Nagpur":
				subRegionList.add("ALL");
				subRegionList.add("Nagpur I");
				subRegionList.add("Nagpur II");
				subRegionList.add("Bhandara");
				break;
			case "Aurangabad":
				subRegionList.add("ALL");
				subRegionList.add("Aurangabad I");
				subRegionList.add("Jalna");
				subRegionList.add("Latur");
				subRegionList.add("Nanded");
				subRegionList.add("Parbhani");
				break;
			case "Nashik":
				subRegionList.add("ALL");
				subRegionList.add("Nashik");
				subRegionList.add("Jalgaon");
				subRegionList.add("Dhule");
				subRegionList.add("Ahmednagar");
				break;
			case "Navi Mumbai":
				subRegionList.add("ALL");
				subRegionList.add("Navi Mumbai I");
				subRegionList.add("Navi Mumbai II");
				subRegionList.add("Taloja");
				break;
			case "Pune":
				subRegionList.add("ALL");
				subRegionList.add("Pune I");
				subRegionList.add("Pune II");
				subRegionList.add("Pimpri-Chichwad");
				subRegionList.add("Satara");
				subRegionList.add("Solapur");
				break;
			case "Raigad":
				subRegionList.add("ALL");
				subRegionList.add("Raigad I");
				subRegionList.add("Raigad II");
				subRegionList.add("Mahad");
				break;
			case "Thane":
				subRegionList.add("ALL");
				subRegionList.add("Thane I");
				subRegionList.add("Thane II");
				subRegionList.add("Tarapur");
				subRegionList.add("Tarapur II");
				break;
			case "Chandrapur":
				subRegionList.add("ALL");
				subRegionList.add("Chandrapur-I");
				break;
			case "Amravati":
				subRegionList.add("ALL");
				subRegionList.add("Amravati I");
				subRegionList.add("Amravati II");
				subRegionList.add("Akola");
				break;
			default:
				getAllSubRegions(subRegionList);
				break;
		}
		return subRegionList;
	}

	private static void getAllSubRegions(List<String> subRegionList) {
		subRegionList.add("ALL");
		subRegionList.add("Mumbai I");
		subRegionList.add("Mumbai II");
		subRegionList.add("Mumbai III");
		subRegionList.add("Mumbai IV");
		subRegionList.add("Kalyan I");
		subRegionList.add("Kalyan II");
		subRegionList.add("Kalyan III");
		subRegionList.add("Bhiwandi");
		subRegionList.add("Kolhapur");
		subRegionList.add("Sangli");
		subRegionList.add("Ratnagiri");
		subRegionList.add("Chiplun");
		subRegionList.add("Nagpur I");
		subRegionList.add("Nagpur II");
		subRegionList.add("Bhandara");
		subRegionList.add("Aurangabad I");
		subRegionList.add("Jalna");
		subRegionList.add("Latur");
		subRegionList.add("Nanded");
		subRegionList.add("Parbhani");
		subRegionList.add("Nashik");
		subRegionList.add("Jalgaon");
		subRegionList.add("Dhule");
		subRegionList.add("Ahmednagar");
		subRegionList.add("Navi Mumbai I");
		subRegionList.add("Navi Mumbai II");
		subRegionList.add("Taloja");
		subRegionList.add("Pune I");
		subRegionList.add("Pune II");
		subRegionList.add("Pimpri-Chichwad");
		subRegionList.add("Satara");
		subRegionList.add("Solapur");
		subRegionList.add("Raigad I");
		subRegionList.add("Raigad II");
		subRegionList.add("Mahad");
		subRegionList.add("Thane I");
		subRegionList.add("Thane II");
		subRegionList.add("Tarapur");
		subRegionList.add("Tarapur II");
		subRegionList.add("Chandrapur-I");
		subRegionList.add("Amravati I");
		subRegionList.add("Amravati II");
		subRegionList.add("Akola");
	}
	
	public static List<String> getRegionListForJson(){
		List<String> regionList = new ArrayList<String>();
		regionList.add("mumbai");
		regionList.add("kalyan");
		regionList.add("kolhapur");
		regionList.add("nagpur");
		regionList.add("aurangabad");
		regionList.add("nashik");
		regionList.add("naviMumbai");
		regionList.add("pune");
		regionList.add("raigad");
		regionList.add("thane");
		regionList.add("chandrapur");
		regionList.add("amravati");
		return regionList;
	}
	
	public static Map<String,List<TileVo>> getRegionMap(){
		Map<String,List<TileVo>> regionMap = new LinkedHashMap<String, List<TileVo>>();
		for(String region : IDSSUtil.getRegionListForJson()) {
			regionMap.put(region, new ArrayList<TileVo>());
		}
		return regionMap;
	}
	
	public static List<String> getLegalActionsList(){
		List<String> legalActionsList = new ArrayList<String>();
		legalActionsList.add("WN");
		legalActionsList.add("SCN");
		legalActionsList.add("PD");
		legalActionsList.add("CD");
		return legalActionsList;
	}
	
	public static List<String> getScaleList(){
		List<String> scaleList = new ArrayList<String>();
		scaleList.add("LSI");
		scaleList.add("MSI");
		scaleList.add("SSI");
		return scaleList;
	}
	
	public static List<String> getTypeList(){
		List<String> typeList = new ArrayList<String>();
		typeList.add("Chemical");
		typeList.add("Metal");
		typeList.add("Paper");
		typeList.add("Ceramic");
		return typeList;
	}
	
	public static List<String> getCategoryList(){
		List<String> categoryList = new ArrayList<String>();
		categoryList.add("Red");
		categoryList.add("Orange");
		categoryList.add("Green");
		categoryList.add("White");
		return categoryList;
	}
	
	public static List<String> getComplianceScoreList(){
		List<String> complianceScoreList = new ArrayList<String>();
		complianceScoreList.add("0-25");
		complianceScoreList.add("26-50");
		complianceScoreList.add("51-75");
		complianceScoreList.add("76-100");
		return complianceScoreList;
	}
	
	public static List<String> getLegalActionsDropdownList(){
		List<String> legalActionsList = new ArrayList<String>();
		legalActionsList.add("0-10");
		legalActionsList.add("11-20");
		legalActionsList.add("21-30");
		legalActionsList.add("31-40");
		legalActionsList.add("41-50");
		legalActionsList.add("Above 50");
		return legalActionsList;
	}
	
	public static List<String> getPendingCasesList(){
		List<String> pendingCasesList = new ArrayList<String>();
		pendingCasesList.add("0-10");
		pendingCasesList.add("11-20");
		pendingCasesList.add("21-30");
		pendingCasesList.add("31-40");
		pendingCasesList.add("41-50");
		pendingCasesList.add("Above 50");
		return pendingCasesList;
	}
	
	public static Map<String, String> getFutureDaysMap() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String currentDay = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.plusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60DaysBack = currentTime.plusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.plusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.plusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		Map<String,String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(currentDay, "_pastDue");
		daysMap.put(date30DaysBack, "_30Days");
		daysMap.put(date60DaysBack, "_60Days");
		daysMap.put(date90DaysBack, "_90Days");
		daysMap.put(date120DaysBack, "_120Days");
		return daysMap;
	}

	public static Map<String, String> getPastDaysMap() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60DaysBack = currentTime.minusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		Map<String,String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(date7DaysBack, "_7Days");
		daysMap.put(date30DaysBack, "_30Days");
		daysMap.put(date60DaysBack, "_60Days");
		daysMap.put(date90DaysBack, "_90Days");
		daysMap.put(date120DaysBack, "_120Days");
		return daysMap;
	}
	
	public static Map<String, String> get3090120PastDaysMap() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		Map<String,String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(date30DaysBack, "_30Days");
		daysMap.put(date90DaysBack, "_90Days");
		daysMap.put(date120DaysBack, "_120Days");
		return daysMap;
	}
	
	public static Map<String, List<String>> getPastAndFutureDaysMap() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		String date30Days = currentTime.plusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90Days = currentTime.plusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120Days = currentTime.plusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String,List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		
		daysMap.put("_30Days", Arrays.asList(date30DaysBack, date30Days));
		daysMap.put("_90Days", Arrays.asList(date90DaysBack, date90Days));
		daysMap.put("_120Days",Arrays.asList(date120DaysBack,date120Days));
		return daysMap;
	}
	
	public static Map<String, String> getPastDaysMapForLegal() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		Map<String,String> daysMap = new LinkedHashMap<String, String>();
		daysMap.put(date30DaysBack, "_30Days");
		daysMap.put(date90DaysBack, "_90Days");
		daysMap.put(date120DaysBack, "_120Days");
		daysMap.put("1970-01-01", "_allDays");
		return daysMap;
	}
	
	public static Map<String, List<String>> getFutureDaysMapForVisits() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date7Days = currentTime.plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date14Days= currentTime.plusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30Days = currentTime.plusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60Days = currentTime.plusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90Days = currentTime.plusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date180Days = currentTime.plusDays(180).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String,List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();
		
		days.add(date7Days);
		days.add(dateToday);
		daysMap.put("_7Days",days);
		days = new ArrayList<String>();
		days.add(date14Days);
		days.add(date7Days);
		daysMap.put("_7DaysOverDays",days);
		
		days = new ArrayList<String>();
		days.add(date30Days);
		days.add(dateToday);
		daysMap.put("_30Days",days);
		days = new ArrayList<String>();
		days.add(date60Days);
		days.add(date30Days);
		daysMap.put("_30DaysOverDays",days);
		
		days = new ArrayList<String>();
		days.add(date90Days);
		days.add(dateToday);
		daysMap.put("_90Days",days);
		days = new ArrayList<String>();
		days.add(date180Days);
		days.add(date90Days);
		daysMap.put("_90DaysOverDays",days);
		
		return daysMap;
	}
	
	public static Map<String, List<String>> getDaysMapForDashboard() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date14DaysBack = currentTime.minusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date60DaysBack = currentTime.minusDays(60).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date180DaysBack = currentTime.minusDays(180).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String,List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();
		
		days.add(dateToday);
		days.add(date7DaysBack);
		daysMap.put("_7Days",days);
		days = new ArrayList<String>();
		days.add(date7DaysBack);
		days.add(date14DaysBack);
		daysMap.put("_7DaysOverDays",days);
		
		days = new ArrayList<String>();
		days.add(dateToday);
		days.add(date30DaysBack);
		daysMap.put("_30Days",days);
		days = new ArrayList<String>();
		days.add(date30DaysBack);
		days.add(date60DaysBack);
		daysMap.put("_30DaysOverDays",days);
		
		days = new ArrayList<String>();
		days.add(dateToday);
		days.add(date90DaysBack);
		daysMap.put("_90Days",days);
		days = new ArrayList<String>();
		days.add(date90DaysBack);
		days.add(date180DaysBack);
		daysMap.put("_90DaysOverDays",days);
		
		return daysMap;
	}
	
	
	public static Map<String, List<String>> getDaysMapForLegal() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date5DaysBack = currentTime.minusDays(5).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date15DaysBack = currentTime.minusDays(15).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String,List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();
		
		days.add(dateToday);
		days.add(date5DaysBack);
		daysMap.put("_0To5Days",days);
		
		days = new ArrayList<String>();
		days.add(dateToday);
		days.add(date7DaysBack);
		daysMap.put("_0To7Days",days);
		
		days = new ArrayList<String>();
		days.add(date5DaysBack);
		days.add(date15DaysBack);
		daysMap.put("_5To15Days",days);
		
		days = new ArrayList<String>();
		days.add(date15DaysBack);
		days.add(date30DaysBack);
		daysMap.put("_15To30Days",days);

		days = new ArrayList<String>();
		days.add(date30DaysBack);
		days.add("1970-01-01");
		daysMap.put("_30ToAllDays",days);
		
		days = new ArrayList<String>();
		days.add(date90DaysBack);
		days.add("1970-01-01");
		daysMap.put("_90ToAllDays",days);
		
		days = new ArrayList<String>();
		days.add(date120DaysBack);
		days.add("1970-01-01");
		daysMap.put("_120ToAllDays",days);
		
		return daysMap;
	}
	
	public static Map<String, List<String>> getDaysMapForVisits() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String dateToday = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date7DaysBack = currentTime.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date15DaysBack = currentTime.minusDays(15).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date30DaysBack = currentTime.minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date90DaysBack = currentTime.minusDays(90).format(DateTimeFormatter.ISO_LOCAL_DATE);
		String date120DaysBack = currentTime.minusDays(120).format(DateTimeFormatter.ISO_LOCAL_DATE);

		Map<String,List<String>> daysMap = new LinkedHashMap<String, List<String>>();
		List<String> days = new ArrayList<String>();
		
		days.add(dateToday);
		days.add(date7DaysBack);
		daysMap.put("_0To7Days",days);
		
		days = new ArrayList<String>();
		days.add(date7DaysBack);
		days.add(date15DaysBack);
		daysMap.put("_7To15Days",days);
		
		days = new ArrayList<String>();
		days.add(date15DaysBack);
		days.add(date30DaysBack);
		daysMap.put("_15To30Days",days);

		days = new ArrayList<String>();
		days.add(date30DaysBack);
		days.add("1970-01-01");
		daysMap.put("_30ToAllDays",days);
		
		days = new ArrayList<String>();
		days.add(date90DaysBack);
		days.add("1970-01-01");
		daysMap.put("_90ToAllDays",days);
		
		days = new ArrayList<String>();
		days.add(date120DaysBack);
		days.add("1970-01-01");
		daysMap.put("_120ToAllDays",days);
		
		return daysMap;
	}
	
	public static Map<String, String> getPastDaysMapForPollutionScoreCard() {
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
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
	

	public static List<Map<String,String>> getEWasteParams(){
		List<Map<String,String>> pl = new ArrayList<Map<String,String>>();
		Map<String,String> ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 1A - Qty e-waste generated MT/A");
		ewasteParamMap.put("value","EWASTE~~ewaste_annual_authorization~~ewasteQtyGenerated");
		pl.add(ewasteParamMap);
		
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 1A - Qty refurbished MT/A");
		ewasteParamMap.put("value","EWASTE~~ewaste_annual_authorization~~ewasteQtyRefurbushing");
		pl.add(ewasteParamMap);
		
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 1A - Qty sent for recycling MT/A");
		ewasteParamMap.put("value","EWASTE~~ewaste_annual_authorization~~ewasteQtyRecycling");
		pl.add(ewasteParamMap);
		
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 1A - Qty sent for disposal MT/A");
		ewasteParamMap.put("value","EWASTE~~ewaste_annual_authorization~~ewasteQtyDisposal");
		pl.add(ewasteParamMap);
//		ewasteParamMap.put("Form 4 - Installed capacity in MT/A","");
//		ewasteParamMap.put("Form 4 - e-waste processed in the last 3 years","");
//		ewasteParamMap.put("Form 4 - Waste generation in processing waste MTA","");
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 3 - Qty of waste MT");
		ewasteParamMap.put("value","EWASTE~~e_waste_annual_returns~~eWasteQtyConsumer");
		pl.add(ewasteParamMap);
		
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 3 - Qty of e-waste processed MT");
		ewasteParamMap.put("value","EWASTE~~e_waste_annual_returns~~eWasteQtyDismantlersProcessed");
		pl.add(ewasteParamMap);
		
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 3 - Qty of materials recovered and sold MT");
		ewasteParamMap.put("value","EWASTE~~e_waste_annual_returns~~eWasteQtyDismantlersRecoveredSold");
		pl.add(ewasteParamMap);
		
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 3 - Qty sent to Treatment, Storage and Disposal facility");
		ewasteParamMap.put("value","EWASTE~~e_waste_annual_returns~~eWasteQtyRecyclersSentTsdf");
		pl.add(ewasteParamMap);
		
		ewasteParamMap = new LinkedHashMap<String, String>();
		ewasteParamMap.put("name","Form 3 - Qty sent to recycler (for dismantlers only)");
		ewasteParamMap.put("value","EWASTE~~e_waste_annual_returns~~eWasteQtyDismantlersSentRecyclers");
		pl.add(ewasteParamMap);
		
		return pl;
	}
	
	public static List<Map<String,String>> getBatteryParams(){
		List<Map<String,String>> pl = new ArrayList<Map<String,String>>();
		
		Map<String,String> batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","New Batteries Sold - 2 wheerler");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~twoWheelerBaterryCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","New Batteries Sold - 4 wheerler");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~fourWheelerBaterryCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","New Batteries Sold - UPS");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~upsBatteryCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","New Batteries Sold - Motive Power");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~motivePowerCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","New Batteries Sold - Stand By");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~standByCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","New Batteries Sold - Others(Inverters)");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~othersCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Old/Used Batteries - 2 wheerler");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~twoWheelerBaterryCountOld");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Old/Used Batteries - 4 wheerler");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~fourWheelerBaterryCountOld");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Old/Used Batteries - UPS");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~upsBatteryCountOld");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Old/Used Batteries - Motive Power");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~motivePowerCountOld");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Old/Used Batteries - Stand By");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~standByCountOld");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Old/Used Batteries - Others(Inverters)");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~othersCountOld");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Batteries Sold To - Dealers");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~dealersBatteryCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Batteries Sold To - Bulk Consumers");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~bulkConsumersBatteryCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Batteries Sold To - OEM");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~oemBatteryCount");
		pl.add(batteryParamMap);
		
		batteryParamMap = new LinkedHashMap<String, String>();
		batteryParamMap.put("name","Batteries Sold To - Others");
		batteryParamMap.put("value","Battery~~Battery_Dealer_Annual_return_Form_V~~anyOtherParty");
		pl.add(batteryParamMap);
		return pl;
	}
	
	public static List<Map<String,String>> getPlasticParams(){
		List<Map<String,String>> pl = new ArrayList<Map<String,String>>();
		
		Map<String,String> plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Authorization Form - Producer - Waste/year");
		plasticParamMap.put("value","Plastic~~Plastic_producer_authorization~~generationTotal");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Authorization Form - BrandOwner - Waste/year");
		plasticParamMap.put("value","Plastic~~Plastic_brand_owner_authorization~~generationTotal");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Authorization Form - Recycler - WasteProcessed/year");
		plasticParamMap.put("value","Plastic~~Plastic_recycler_authorization~~wasteQuantityProcessed");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Authorization Form - Raw Material Production/MTA");
		plasticParamMap.put("value","Plastic~~Plastic_raw_material_manufacturer_authorization~~produceQty");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 4 - Waste Received");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_recycling_facility_form_IV~~quantityReceived");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 4 - Waste Recycled");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_recycling_facility_form_IV~~quantityRecycled");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 4 - Waste Disposed");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_recycling_facility_form_IV~~quantityDisposed");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 4 - Inert Produced");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_recycling_facility_form_IV~~quantityInerts");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 5 - Waste Gen.");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_local_body_form_V~~plasticWasteGeneratedQuantity");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 5 - Waste Collected");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_local_body_form_V~~plasticWasteCollectedQuantity");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 5 - Waste Recycled");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_local_body_form_V~~plasticWasteRecycledQuantity");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 5 - Waste Processed");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_local_body_form_V~~plasticWasteProcessedQuantity");
		pl.add(plasticParamMap);
		
		plasticParamMap = new LinkedHashMap<String, String>();
		plasticParamMap.put("name","Form 5 - Waste sent to Landfill");
		plasticParamMap.put("value","Plastic~~Plastic_annual_report_local_body_form_V~~landPlasticWasteQuantity");
		pl.add(plasticParamMap);
		
		return pl;
	}
	
	public static List<Map<String,String>> getBioMedWasteParams(){
		List<Map<String,String>> pl = new ArrayList<Map<String,String>>();
		
		Map<String,String> bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Yellow");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Annual_return_Comparison~~yellowCategory");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Red");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Annual_return_Comparison~~redCategory");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Blue");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Annual_return_Comparison~~blueCategory");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","White");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Annual_return_Comparison~~whiteCategory");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 01");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 02");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 03");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 04");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 05");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 06");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 07");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 08");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 09");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		bioMedWasteParamMap = new LinkedHashMap<String, String>();
		bioMedWasteParamMap.put("name","Category 10");
		bioMedWasteParamMap.put("value","BioMedWaste~~BMW_Authorization_comparison~~bioMedicalWasteName~~bioMedicalWasteQuantity");
		pl.add(bioMedWasteParamMap);
		
		return pl;
	}
	
	public static String getDateColumnName(String reportType){

		switch(reportType.toUpperCase()) {
			case "CONSENT" :
				return "applicationCreatedOn";
			case "ESR":
				return "envStmtCreated";
			case "EWASTE":
				return "createdTime";
			case "BATTERY":
				return "applicationCreatedOn";
			case "PLASTIC":
				return "applicationCreatedOn";
			case "BIOMEDWASTE":
				return "createdTime";
			case "OCEMS":
				return "time_stamp";
			case "HAZ_WASTE":
				return "createdTime";
		}
		return null;
	}
}
