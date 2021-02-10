package com.eai.idss.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.eai.idss.vo.PollutionScoreValueVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class OCEMSDataDaoImpl implements OCEMSDataDao {
	
	@Value("${dbName}")
	private String dbName;
	
	@Autowired
	MongoClient mongoClient;
	
	public static final Logger logger = Logger.getLogger(OCEMSDataDaoImpl.class);

	public List<PollutionScoreValueVo> getOCEMSPollutionScoreValue(long industryId, String paramValue, String fromDate,String toDate) {
		try {
			Document matchDoc = new Document();
			matchDoc.append("industry_mis_id", industryId);
			matchDoc.append("parameter_name", paramValue);
			
			matchDoc.append("time_stamp", new Document()
	        		.append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(toDate))
	                .append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(fromDate)));
	        
			
			List<PollutionScoreValueVo> pList = new ArrayList<PollutionScoreValueVo>();
			List<? extends Bson> pipeline = Arrays.asList(
						new Document().append("$match", matchDoc),  
		                new Document()
			            .append("$project", new Document()
			                    .append("_id", false)
			                    .append("day", new Document()
				                        .append("$dateToString", new Document()
				                                .append("format", "%Y-%m-%d")
				                                .append("date", "$time_stamp")
				                        )
				                )
			            )
					);
			MongoDatabase database = mongoClient.getDatabase(dbName);
	        MongoCollection<Document> collection = database.getCollection("OCEMS_data");
	
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
}
