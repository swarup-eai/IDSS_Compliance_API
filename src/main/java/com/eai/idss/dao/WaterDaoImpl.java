package com.eai.idss.dao;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


import com.eai.idss.model.Plastic_Annual_Report_Local_Body_Form_V;
import com.eai.idss.model.WaterMasterData;
import com.eai.idss.model.WaterStationMaster;

import com.eai.idss.repository.WaterMasterDataRepository;
import com.eai.idss.vo.WaterDataResponseVo;
import com.eai.idss.vo.WaterDetailRequest;
import com.eai.idss.vo.WaterStationListResponseVo;
import com.eai.idss.vo.WaterStationRegionRequest;
import com.mongodb.client.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.eai.idss.repository.WaterStationMasterRepository;
import org.springframework.util.StringUtils;

@Repository
public class WaterDaoImpl implements WaterDao {

    @Value("${dbName}")
    private String dbName;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    private WaterStationMasterRepository waterStationMasterRepository;

    @Autowired
    private WaterMasterDataRepository waterMasterDataRepository;




    public List<WaterStationListResponseVo> getWaterStationList(WaterStationRegionRequest wsr)
    {
        try {
            List<String> stationList = new ArrayList<String>();
            List<WaterStationMaster> stList =null;
            List<WaterStationListResponseVo> waterStationListresponseVoList = new ArrayList<WaterStationListResponseVo>();

            Query query = new Query();
            if("ALL".equalsIgnoreCase(wsr.getRegionOffice()))
            {
                 stList = waterStationMasterRepository.findAll();

            }
            else
            {
                if(StringUtils.hasText(wsr.getRegionOffice()))
                {
                    query.addCriteria(Criteria.where("regionOffice").is(wsr.getRegionOffice()));
                }
                if(StringUtils.hasText(wsr.getSubregionOffice())) {
                    query.addCriteria(Criteria.where("subRegionOffice").is(wsr.getSubregionOffice()));
                }
                if(StringUtils.hasText(wsr.getSampleName())) {
                    query.addCriteria(Criteria.where("sampleName").is(wsr.getSampleName()));
                }
                if(StringUtils.hasText(wsr.getPrgName())){
                    query.addCriteria(Criteria.where("prgName").is(wsr.getPrgName()));
                }
                stList = mongoTemplate.find(query, WaterStationMaster.class);

            }

            for (WaterStationMaster waterStationMasterData : stList) {
                WaterStationListResponseVo waterDataResponseVo = new WaterStationListResponseVo();

                waterDataResponseVo.setStnName(waterStationMasterData.getStnName());
                waterDataResponseVo.setStnId(waterStationMasterData.getStnId());
                waterDataResponseVo.setDistName(waterStationMasterData.getDistName());
                waterDataResponseVo.setPrgName(waterStationMasterData.getPrgName());
                waterDataResponseVo.setSampleName(waterStationMasterData.getSampleName());
                waterDataResponseVo.setStnCriteria(waterStationMasterData.getStnCriteria());
                waterStationListresponseVoList.add(waterDataResponseVo);

            }
            return  waterStationListresponseVoList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    public List<WaterDataResponseVo> getWaterStationDetailStationId(WaterDetailRequest wdr) {
        try
        {

            List<String> param = new ArrayList<String>();
            Query query = new Query();

            query.addCriteria(Criteria.where("stnId").is(wdr.getStnId()));

            query.addCriteria(Criteria.where("readDate")
                    .gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(wdr.getFromDate() + " 00:00:00.000+0000"))
                    .lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(wdr.getToDate()+ " 23:59:59.000+0000")));

            List<WaterMasterData> stationList= mongoTemplate.find(query, WaterMasterData.class);
            Map<String,String> msd = new LinkedHashMap<String, String>();

            List<WaterDataResponseVo>   waterDataResponseVoList = new ArrayList<WaterDataResponseVo>();
            for(WaterMasterData  waterMasterData : stationList) {
                WaterDataResponseVo waterDataResponseVo = new WaterDataResponseVo();
                for(int i=0;i<wdr.requestWaterParam.size();i++)
                {
                    if(wdr.getRequestWaterParam().get(i).equalsIgnoreCase("pH")){
                        waterDataResponseVo.setpH(waterMasterData.getpH());

                    }
                    if(wdr.getRequestWaterParam().get(i).equalsIgnoreCase("fecalColiform")){

                        waterDataResponseVo.setFecalColiform(waterMasterData.getFecalColiform());
                    }
                    if(wdr.getRequestWaterParam().get(i).equalsIgnoreCase("BOD") ){
                        waterDataResponseVo.setBOD(waterMasterData.getBOD());
                    }
                    if(wdr.getRequestWaterParam().get(i).equalsIgnoreCase("dissolvedOxygen")){
                        waterDataResponseVo.setDissolvedOxygen(waterMasterData.getDissolvedOxygen());
                    }
                    if(wdr.getRequestWaterParam().get(i).equalsIgnoreCase("nitrate")){
                        waterDataResponseVo.setNitrate(waterMasterData.getNitrate());

                    }
                }
                  waterDataResponseVo.setDate(waterMasterData.getReadDate());
                waterDataResponseVoList.add(waterDataResponseVo);
            }

            return waterDataResponseVoList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;

    }
}