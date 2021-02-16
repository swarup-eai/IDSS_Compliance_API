package com.eai.idss.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.eai.idss.model.OCEMS_data;
import com.mongodb.client.MongoClient;

@Repository
public class OCEMSDataDaoImpl implements OCEMSDataDao {
	
	@Value("${dbName}")
	private String dbName;
	
	@Autowired
	MongoClient mongoClient;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public static final Logger logger = Logger.getLogger(OCEMSDataDaoImpl.class);
	
	

	public boolean isParamValueInLimit(long industryId, String paramValue, String fromDate,String toDate,String thresholdRange) {
		try {
	        
			Query query = new Query();
			query.addCriteria(Criteria.where("industry_mis_id").is(industryId));
			query.addCriteria(Criteria.where("parameter_name").is(paramValue));
			query.addCriteria(Criteria.where("time_stamp")
					.lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDate))
					.gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDate)));
			
			long cnt = mongoTemplate.count(query, OCEMS_data.class);
			logger.info("generateOCEMSAlerts param "+paramValue+" Count="+cnt);
			
			if(cnt==0) return true;
			
			List<OCEMS_data> ol = mongoTemplate.find(query, OCEMS_data.class);
			
			if(ol.stream().filter(p -> p.getValue()<p.getUpper_limit()).findFirst().isPresent())
				return true;
			else
				return false;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
