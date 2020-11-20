package com.eai.idss.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.IndustryMasterRequest;

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
			if(StringUtils.hasText(imr.getCategory()) && !"All".equalsIgnoreCase(imr.getCategory()))
				query.addCriteria(Criteria.where("category").is(imr.getCategory()));
			if(StringUtils.hasText(imr.getScale()) && !"All".equalsIgnoreCase(imr.getScale()))
				query.addCriteria(Criteria.where("scale").is(imr.getScale()));
			if(StringUtils.hasText(imr.getType()) && !"All".equalsIgnoreCase(imr.getType()))
				query.addCriteria(Criteria.where("type").is(imr.getType()));
			if(null!=imr.getComplianceScore() )
				query.addCriteria(Criteria.where("complianceScore").is(imr.getComplianceScore()));
			if(StringUtils.hasText(imr.getLegalActions()) && !"All".equalsIgnoreCase(imr.getLegalActions()))
				query.addCriteria(Criteria.where("legalActions").is(imr.getLegalActions()));
			if(StringUtils.hasText(imr.getPendingCases()) && !"All".equalsIgnoreCase(imr.getPendingCases()))
				query.addCriteria(Criteria.where("pendingCases").is(imr.getPendingCases()));
		}
		

		System.out.println(mongoTemplate.count(query, IndustryMaster.class));
		
		List<IndustryMaster> filteredIndustryMaster= 
		mongoTemplate.find(query, IndustryMaster.class);
		Page<IndustryMaster> imPage = PageableExecutionUtils.getPage(
				filteredIndustryMaster,
				page,
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


}
