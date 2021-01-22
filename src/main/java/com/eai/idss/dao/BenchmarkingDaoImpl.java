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
    public List<SkuDetailVo> getSkuDetail(BenchmarkingRequest br) {
        try {
            logger.info("getSkuDetail");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(br.getType()));
            query.addCriteria(Criteria.where("industryName").is(br.getIndustryName()));
            List<Double> benchmarkingSourceData=mongoTemplate.findDistinct(query,"categoryCode",BenchmarkingSourceData.class,Double.class); // benchmarkingSourceDataRepository.findDistinctCategoryCodeByIndustryType(dr.getType());//mongoTemplate.find(query,BenchmarkingSourceData.class);

            List<SkuDetailVo> skuDetailVos = benchmarkingSourceData.stream().map(benchmarkingData -> {
                SkuDetailVo skuDetailVo = new SkuDetailVo();
                List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByCategoryCode(benchmarkingData); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                double productQty = benchmarkingSourceDataList.stream().mapToDouble(BenchmarkingSourceData::getProductionQty).sum();
                double average = productQty / benchmarkingSourceDataList.size();
                skuDetailVo.setProductQuantity(average);
                skuDetailVo.setProductCategory(benchmarkingSourceDataList.get(0).getProductCategory());
                List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByCategoryCodeAndIndustryType(benchmarkingData,br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
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
    public List<SkuDetailVo> getAirPollutant(BenchmarkingRequest br) {
        try {
            logger.info("getAirPollutant");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(br.getType()));
            List<String> benchmarkingSourceData=mongoTemplate.findDistinct(query,"airPollutantsGroup",BenchmarkingSourceData.class,String.class); // benchmarkingSourceDataRepository.findDistinctCategoryCodeByIndustryType(dr.getType());//mongoTemplate.find(query,BenchmarkingSourceData.class);

            List<SkuDetailVo> skuDetailVos = benchmarkingSourceData.stream().map(benchmarkingData -> {
                if(benchmarkingData.equals("NOx")){
                    SkuDetailVo skuDetailVo = new SkuDetailVo();
                    List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByIndustryTypeAndAirPollutantsGroup(br.getType(),benchmarkingData);
                    double productQty = benchmarkingSourceDataList.stream().mapToDouble(BenchmarkingSourceData::getConcentration).sum();
                    double average = productQty / benchmarkingSourceDataList.size();
                    skuDetailVo.setProductQuantity(average);
                    skuDetailVo.setProductCategory(benchmarkingData);
                    List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByIndustryType(br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                    double avgNoxSumValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgNox).sum();
                    skuDetailVo.setBenchmarkValues(avgNoxSumValue);
                    return skuDetailVo;
                }else if(benchmarkingData.equals("SO2")){
                    SkuDetailVo skuDetailVo = new SkuDetailVo();
                    List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByIndustryTypeAndAirPollutantsGroup(br.getType(),benchmarkingData);
                    double productQty = benchmarkingSourceDataList.stream().mapToDouble(BenchmarkingSourceData::getConcentration).sum();
                    double average = productQty / benchmarkingSourceDataList.size();
                    skuDetailVo.setProductQuantity(average);
                    skuDetailVo.setProductCategory(benchmarkingData);
                    List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByIndustryType(br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                    double avgSo2SumValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgSo2).sum();
                    skuDetailVo.setBenchmarkValues(avgSo2SumValue);
                    return skuDetailVo;
                }else if(benchmarkingData.equals("P_M")){
                    SkuDetailVo skuDetailVo = new SkuDetailVo();
                    List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByIndustryTypeAndAirPollutantsGroup(br.getType(),benchmarkingData);
                    double productQty = benchmarkingSourceDataList.stream().mapToDouble(BenchmarkingSourceData::getConcentration).sum();
                    double average = productQty / benchmarkingSourceDataList.size();
                    skuDetailVo.setProductQuantity(average);
                    skuDetailVo.setProductCategory(benchmarkingData);
                    List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByIndustryType(br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
                     double avgPmSumValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgPm).sum();
                    skuDetailVo.setBenchmarkValues(avgPmSumValue);
                    return skuDetailVo;
                }else {
                    return null;
                }

            }).filter(Objects::nonNull).collect(Collectors.toList());
            return skuDetailVos;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SkuDetailVo> getWaterPollutant(BenchmarkingRequest br) {
        try {
            logger.info("getWaterPollutant");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(br.getType()));
            List<BenchmarkingSourceData> benchmarkingSourceData=mongoTemplate.find(query,BenchmarkingSourceData.class);

            List<SkuDetailVo> skuDetailVo = new ArrayList<SkuDetailVo>();

            SkuDetailVo skuDetailVo1 = new SkuDetailVo();
            skuDetailVo1.setProductCategory("ETP");
            double capacityOfEtpSumValue = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getCapacityOfEtp).sum();
            double capacityOfEtpAvg =capacityOfEtpSumValue / benchmarkingSourceData.size();
            skuDetailVo1.setProductQuantity(capacityOfEtpAvg);
            List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByIndustryType(br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
            double avgEtpSumValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgEtp).sum();
            skuDetailVo1.setBenchmarkValues(avgEtpSumValue);
            skuDetailVo.add(skuDetailVo1);

            SkuDetailVo skuDetailVo2 = new SkuDetailVo();
            skuDetailVo2.setProductCategory("STP");
            double capacityOfStpSumValue = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getCapacityOfStp).sum();
            double capacityOfStpAvg =capacityOfStpSumValue / benchmarkingSourceData.size();
            skuDetailVo2.setProductQuantity(capacityOfStpAvg);
            List<BenchmarkingDestinationData> benchmarkingDestinationData1=benchmarkingDestinationDataRepository.findByIndustryType(br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
            double avgStpSumValue =benchmarkingDestinationData1.stream().mapToDouble(BenchmarkingDestinationData::getAvgStp).sum();
            skuDetailVo2.setBenchmarkValues(avgStpSumValue);
            skuDetailVo.add(skuDetailVo2);

            return skuDetailVo;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SkuDetailVo> getEffluents(BenchmarkingRequest br) {
        try {
            logger.info("getEffluents");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(br.getType()));
            List<BenchmarkingSourceData> benchmarkingSourceData=mongoTemplate.find(query,BenchmarkingSourceData.class);

            List<SkuDetailVo> skuDetailVo = new ArrayList<SkuDetailVo>();

            SkuDetailVo skuDetailVo1 = new SkuDetailVo();
            skuDetailVo1.setProductCategory("BOD");
            double treatedEffluentBodSumValue = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getTreatedEffluentBod).sum();
            double treatedEffluentBodAvg =treatedEffluentBodSumValue / benchmarkingSourceData.size();
            skuDetailVo1.setProductQuantity(treatedEffluentBodAvg);
            List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByIndustryType(br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
            double avgBodSumValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgBod).sum();
            skuDetailVo1.setBenchmarkValues(avgBodSumValue);
            skuDetailVo.add(skuDetailVo1);

            SkuDetailVo skuDetailVo2 = new SkuDetailVo();
            skuDetailVo2.setProductCategory("COD");
            double treatedEffluentCodSumValue = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getTreatedEffluentCod).sum();
            double treatedEffluentCodAvg =treatedEffluentCodSumValue / benchmarkingSourceData.size();
            skuDetailVo2.setProductQuantity(treatedEffluentCodAvg);
            List<BenchmarkingDestinationData> benchmarkingDestinationData1=benchmarkingDestinationDataRepository.findByIndustryType(br.getType());
            double avgCodSumValue =benchmarkingDestinationData1.stream().mapToDouble(BenchmarkingDestinationData::getAvgCod).sum();
            skuDetailVo2.setBenchmarkValues(avgCodSumValue);
            skuDetailVo.add(skuDetailVo2);

            SkuDetailVo skuDetailVo3 = new SkuDetailVo();
            skuDetailVo3.setProductCategory("TDS");
            double treatedEffluentTdsSumValue = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getTreatedEffluentTds).sum();
            double treatedEffluentTdsAvg = treatedEffluentTdsSumValue / benchmarkingSourceData.size();
            skuDetailVo3.setProductQuantity(treatedEffluentTdsAvg);
            List<BenchmarkingDestinationData> benchmarkingDestinationData2=benchmarkingDestinationDataRepository.findByIndustryType(br.getType());
            double avgTdsSumValue =benchmarkingDestinationData2.stream().mapToDouble(BenchmarkingDestinationData::getAvgTds).sum();
            skuDetailVo3.setBenchmarkValues(avgTdsSumValue);
            skuDetailVo.add(skuDetailVo3);

            SkuDetailVo skuDetailVo4 = new SkuDetailVo();
            skuDetailVo4.setProductCategory("TSS");
            double TreatedEffluentSsSumValue = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getTreatedEffluentSs).sum();
            double treatedEffluentSsAvg = TreatedEffluentSsSumValue / benchmarkingSourceData.size();
            skuDetailVo4.setProductQuantity(treatedEffluentSsAvg);
            List<BenchmarkingDestinationData> benchmarkingDestinationData4=benchmarkingDestinationDataRepository.findByIndustryType(br.getType());
            double avgTssSumValue =benchmarkingDestinationData4.stream().mapToDouble(BenchmarkingDestinationData::getAvgTss).sum();
            skuDetailVo4.setBenchmarkValues(avgTssSumValue);
            skuDetailVo.add(skuDetailVo4);


            SkuDetailVo skuDetailVo5 = new SkuDetailVo();
            skuDetailVo5.setProductCategory("PH");
            double treatedEffluentPhSumValue = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getTreatedEffluentPh).sum();
            double treatedEffluentPhAvg = treatedEffluentPhSumValue / benchmarkingSourceData.size();
            skuDetailVo5.setProductQuantity(treatedEffluentPhAvg);
            BenchmarkingDestinationData benchmarkingDestinationData5=benchmarkingDestinationDataRepository.findTop1ByIndustryTypeOrderByAvgPhDesc(br.getType());
            skuDetailVo5.setBenchmarkValues(benchmarkingDestinationData5.getAvgPh());
            skuDetailVo.add(skuDetailVo5);

            return skuDetailVo;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<IndustryNameByTypeVo> getIndustryNameByType(String type) {
        try {
            logger.info("getIndustryNameByType");
            Query query = new Query();
            query.addCriteria(Criteria.where("industryType").is(type));
            List<String> benchmarkingSourceData=mongoTemplate.findDistinct(query,"industryName",BenchmarkingSourceData.class,String.class);

            List<IndustryNameByTypeVo> industryNameByTypeVoList = benchmarkingSourceData.stream().map(data -> {
                IndustryNameByTypeVo industryNameByTypeVo = new IndustryNameByTypeVo();
                industryNameByTypeVo.setIndustryName(data);
                return industryNameByTypeVo;
            }).collect(Collectors.toList());
            return industryNameByTypeVoList;
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
