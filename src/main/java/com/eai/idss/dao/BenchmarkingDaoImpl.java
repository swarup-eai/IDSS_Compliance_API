package com.eai.idss.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eai.idss.model.BenchmarkingDestinationData;
import com.eai.idss.model.BenchmarkingSourceData;
import com.eai.idss.repository.BenchmarkingDestinationDataRepository;
import com.eai.idss.repository.BenchmarkingSourceDataRepository;
import com.eai.idss.vo.BenchmarkingRequest;
import com.eai.idss.vo.IndustryNameByTypeVo;
import com.eai.idss.vo.SkuDetailVo;
import com.mongodb.client.MongoClient;

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
            if(StringUtils.hasText(br.getType())){
                query.addCriteria(Criteria.where("industryType").is(br.getType()));
            }
            if(StringUtils.hasText(br.getIndustryName())){
                query.addCriteria(Criteria.where("industryName").is(br.getIndustryName()));
            }
            List<BenchmarkingSourceData> benchmarkingSourceData=mongoTemplate.find(query,BenchmarkingSourceData.class); // benchmarkingSourceDataRepository.findDistinctCategoryCodeByIndustryType(dr.getType());//mongoTemplate.find(query,BenchmarkingSourceData.class);
            List<SkuDetailVo> skuDetailVoList = new ArrayList<>();
            SkuDetailVo skuDetailVo = new SkuDetailVo();
            double productQty = benchmarkingSourceData.stream().mapToDouble(BenchmarkingSourceData::getProductionQty).sum();
            double average = productQty / benchmarkingSourceData.size();
            skuDetailVo.setProductQuantity(average);
            skuDetailVo.setProductCategory(benchmarkingSourceData.get(0).getProductCategory());
            List<BenchmarkingDestinationData> benchmarkingDestinationData=benchmarkingDestinationDataRepository.findByCategoryCodeAndIndustryType(benchmarkingSourceData.get(0).getCategoryCode(),br.getType()); //mongoTemplate.find(query,BenchmarkingSourceData.class);
            double benchmarkingValue =benchmarkingDestinationData.stream().mapToDouble(BenchmarkingDestinationData::getAvgQty).sum();
            skuDetailVo.setBenchmarkValues(benchmarkingValue);
            skuDetailVoList.add(skuDetailVo);
            return skuDetailVoList;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<SkuDetailVo> getAirPollutant(BenchmarkingRequest br) {
        try {
            logger.info("getAirPollutant");
            Query query = new Query();
            if(StringUtils.hasText(br.getType())){
                query.addCriteria(Criteria.where("industryType").is(br.getType()));
            }
            if(StringUtils.hasText(br.getIndustryName())){
                query.addCriteria(Criteria.where("industryName").is(br.getIndustryName()));
            }            List<String> benchmarkingSourceData=mongoTemplate.findDistinct(query,"airPollutantsGroup",BenchmarkingSourceData.class,String.class); // benchmarkingSourceDataRepository.findDistinctCategoryCodeByIndustryType(dr.getType());//mongoTemplate.find(query,BenchmarkingSourceData.class);

            List<SkuDetailVo> skuDetailVos = benchmarkingSourceData.stream().map(benchmarkingData -> {
                if(benchmarkingData.equals("NOx")){
                    SkuDetailVo skuDetailVo = new SkuDetailVo();
                    List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByIndustryTypeAndIndustryNameAndAirPollutantsGroup(br.getType(),br.getIndustryName(),benchmarkingData);
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
                    List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByIndustryTypeAndIndustryNameAndAirPollutantsGroup(br.getType(),br.getIndustryName(),benchmarkingData);
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
                    List<BenchmarkingSourceData> benchmarkingSourceDataList=benchmarkingSourceDataRepository.findByIndustryTypeAndIndustryNameAndAirPollutantsGroup(br.getType(),br.getIndustryName(),benchmarkingData);
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
            if(StringUtils.hasText(br.getType())){
                query.addCriteria(Criteria.where("industryType").is(br.getType()));
            }
            if(StringUtils.hasText(br.getIndustryName())){
                query.addCriteria(Criteria.where("industryName").is(br.getIndustryName()));
            }            List<BenchmarkingSourceData> benchmarkingSourceData=mongoTemplate.find(query,BenchmarkingSourceData.class);

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
            if(StringUtils.hasText(br.getType())){
                query.addCriteria(Criteria.where("industryType").is(br.getType()));
            }
            if(StringUtils.hasText(br.getIndustryName())){
                query.addCriteria(Criteria.where("industryName").is(br.getIndustryName()));
            }            List<BenchmarkingSourceData> benchmarkingSourceData=mongoTemplate.find(query,BenchmarkingSourceData.class);

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


}
