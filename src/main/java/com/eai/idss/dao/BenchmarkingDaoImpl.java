package com.eai.idss.dao;

import com.eai.idss.model.BenchmarkingDestinationData;
import com.eai.idss.model.BenchmarkingSourceData;
import com.eai.idss.repository.BenchmarkingDestinationDataRepository;
import com.eai.idss.repository.BenchmarkingSourceDataRepository;
import com.eai.idss.vo.*;
import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BenchmarkingDaoImpl implements BenchmarkingDao{
    public static final Logger logger = Logger.getLogger(BenchmarkingDaoImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    BenchmarkingSourceDataRepository benchmarkingSourceDataRepository;

    @Autowired
    BenchmarkingDestinationDataRepository benchmarkingDestinationDataRepository;

    @Override
    public List<SkuDetailVo> getSkuDetail(DashboardRequest dr) {
        try {
            logger.info("getSkuDetail");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(dr.getType()));
            List<Double> benchmarkingSourceData=mongoTemplate.findDistinct(query,"categoryCode",BenchmarkingSourceData.class,Double.class); // benchmarkingSourceDataRepository.findDistinctCategoryCodeByIndustryType(dr.getType());//mongoTemplate.find(query,BenchmarkingSourceData.class);

            List<SkuDetailVo> skuDetailVos = benchmarkingSourceData.stream().map(benchmarkingData -> {
                SkuDetailVo skuDetailVo = new SkuDetailVo();
                List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByCategoryCode(benchmarkingData); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                double productQty = benchmarkingSourceDataList.stream().mapToDouble(BenchmarkingSourceData::getProductionQty).sum();
                double average = productQty / benchmarkingSourceDataList.size();
                skuDetailVo.setProductQuantity(average);
                skuDetailVo.setProductCategory(benchmarkingSourceDataList.get(0).getProductCategory());
                List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByCategoryCodeAndIndustryType(benchmarkingData,dr.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                double benchmarkingValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgQty).sum();
                skuDetailVo.setBenchmarkValues(benchmarkingValue);
                return skuDetailVo;
            }).collect(Collectors.toList());
return skuDetailVos;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
//        try {
//            logger.info("getBenchmarkingdata");
//
//            MongoDatabase database = mongoClient.getDatabase("IDSS");
//            MongoCollection<Document> collection = database.getCollection("benchmarkingSourceData");
//
//            Map<String,List<TileVo>> tileMap = new LinkedHashMap<String, List<TileVo>>();
//
//            List<? extends Bson> pipeline = getSkuDetailPipeline(dr);
//
//            collection.aggregate(pipeline)
//                    .allowDiskUse(false)
//                    .forEach(new Consumer<Document>() {
//                                 @Override
//                                 public void accept(Document document) {
//                                     logger.info(document.toJson());
//                                     try {
//                                         SkuDetailVo vVo = new ObjectMapper().readValue(document.toJson(), SkuDetailVo.class);
//                                         System.out.println(vVo);
//
//                                     } catch (JsonMappingException e) {
//                                         e.printStackTrace();
//                                     } catch (JsonProcessingException e) {
//                                         e.printStackTrace();
//                                     }
//
//                                 }
//                             }
//                    );
//
//            return null;
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }
    @Override
    public List<SkuDetailVo> getAirPollutant(DashboardRequest dr) {
        try {
            logger.info("getAirPollutant");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(dr.getType()));
            List<Double> benchmarkingSourceData=mongoTemplate.findDistinct(query,"categoryCode",BenchmarkingSourceData.class,Double.class); // benchmarkingSourceDataRepository.findDistinctCategoryCodeByIndustryType(dr.getType());//mongoTemplate.find(query,BenchmarkingSourceData.class);

            List<SkuDetailVo> skuDetailVos = benchmarkingSourceData.stream().map(benchmarkingData -> {
                SkuDetailVo skuDetailVo = new SkuDetailVo();
                List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByCategoryCode(benchmarkingData); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                double productQty = benchmarkingSourceDataList.stream().mapToDouble(BenchmarkingSourceData::getProductionQty).sum();
                double average = productQty / benchmarkingSourceDataList.size();
                skuDetailVo.setProductQuantity(average);
                skuDetailVo.setProductCategory(benchmarkingSourceDataList.get(0).getProductCategory());
                List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByCategoryCodeAndIndustryType(benchmarkingData,dr.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                double benchmarkingValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgQty).sum();
                skuDetailVo.setBenchmarkValues(benchmarkingValue);
                return skuDetailVo;
            }).collect(Collectors.toList());
            return skuDetailVos;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SkuDetailVo> getWaterPollutant(DashboardRequest dr) {
        try {
            logger.info("getWaterPollutant");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(dr.getType()));
            List<Double> benchmarkingSourceData=mongoTemplate.findDistinct(query,"categoryCode",BenchmarkingSourceData.class,Double.class);

            List<SkuDetailVo> skuDetailVos = benchmarkingSourceData.stream().map(benchmarkingData -> {
                SkuDetailVo skuDetailVo = new SkuDetailVo();
                List<BenchmarkingSourceData> benchmarkingSourceDataList = benchmarkingSourceDataRepository.findByCategoryCode(benchmarkingData);
               if(benchmarkingSourceDataList.size() >0) {
                   double capacitySum = benchmarkingSourceDataList.get(0).getCapacityOfEtp() + benchmarkingSourceDataList.get(0).getCapacityOfStp();
                   double average = capacitySum / 2;
                   skuDetailVo.setProductQuantity(average);
                   skuDetailVo.setProductCategory(benchmarkingSourceDataList.get(0).getProductCategory());
               }
                List<BenchmarkingDestinationData> benchmarkingDestinationData = benchmarkingDestinationDataRepository.findByCategoryCodeAndIndustryType(benchmarkingData, dr.getType());
                if(benchmarkingDestinationData.size() >0){
                double benchmarkingValue = benchmarkingDestinationData.get(0).getAvgEtp() + benchmarkingDestinationData.get(0).getAvgStp();
                skuDetailVo.setBenchmarkValues(benchmarkingValue);
            }
                return skuDetailVo;
            }).collect(Collectors.toList());
            return skuDetailVos;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SkuDetailVo> getEffluents(DashboardRequest dr) {
        try {
            logger.info("getEffluents");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(dr.getType()));
            List<Double> benchmarkingSourceData=mongoTemplate.findDistinct(query,"categoryCode",BenchmarkingSourceData.class,Double.class);

            List<SkuDetailVo> skuDetailVos = benchmarkingSourceData.stream().map(benchmarkingData -> {
                SkuDetailVo skuDetailVo = new SkuDetailVo();
                List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByCategoryCode(benchmarkingData);
               if(benchmarkingSourceDataList.size() >0) {
                   double sum = benchmarkingSourceDataList.get(0).getTreatedEffluentBod() + benchmarkingSourceDataList.get(0).getTreatedEffluentCod() +
                           benchmarkingSourceDataList.get(0).getTreatedEffluentPh() + benchmarkingSourceDataList.get(0).getTreatedEffluentSs() +
                           benchmarkingSourceDataList.get(0).getTreatedEffluentTds();
                   double average = sum / 5;
                   skuDetailVo.setProductQuantity(average);
                   skuDetailVo.setProductCategory(benchmarkingSourceDataList.get(0).getProductCategory());
               }
                List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByCategoryCodeAndIndustryType(benchmarkingData,dr.getType());
                if(benchmarkingDestinationData.size() >0) {
                    double benchmarkingValue = benchmarkingDestinationData.get(0).getAvgBod() + benchmarkingDestinationData.get(0).getAvgCod()+
                            benchmarkingDestinationData.get(0).getAvgTds() + benchmarkingDestinationData.get(0).getAvgTss();
                    skuDetailVo.setBenchmarkValues(benchmarkingValue);
                }
                return skuDetailVo;
            }).collect(Collectors.toList());
            return skuDetailVos;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<? extends Bson> getSkuDetailPipeline(DashboardRequest dr) throws ParseException {
        Document matchDoc = new Document();

        matchDoc.append("industryType", dr.getType());

        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append("$match", matchDoc),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("productName", "$productName")
                                        .append("categoryCode", "$categoryCode")
                                        .append("productionQty", "$productionQty")

                                )
                                .append("count", new Document()
                                        .append("$sum", 1)
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("_id", false)
                                .append("productName", "$_id.productName")
                                .append("quantity", "$_id.productionQty")
                                .append("categoryCode", "$_id.categoryCode")
                                .append("count", "$count")
                        )
//                new Document()
//                        .append("$sort", new Document()
//                                .append("scale", 1.0)
//                        )
        );
        return pipeline;
    }

}
