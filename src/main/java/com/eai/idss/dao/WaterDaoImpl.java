package com.eai.idss.dao;
import java.text.SimpleDateFormat;
import java.util.*;

import com.eai.idss.model.*;

import com.eai.idss.repository.CetpMasterDataRepository;
import com.eai.idss.repository.CetpStationMasterRepository;
import com.eai.idss.repository.WaterMasterDataRepository;
import com.eai.idss.vo.*;
import com.mongodb.client.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.eai.idss.repository.WaterStationMasterRepository;
import com.eai.idss.repository.WaterParamStdMasterRepository;
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

    @Autowired
    private CetpMasterDataRepository cetpMasterDataRepository;

    @Autowired
    private CetpStationMasterRepository cetpStationMasterRepository;

    @Autowired
    private WaterParamStdMasterRepository waterParamStdMasterRepository;

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
                if(StringUtils.hasText(wsr.getRegionOffice()) && "ALL".equalsIgnoreCase(wsr.getSubregionOffice())) {
                    query.addCriteria(Criteria.where("regionOffice").is(wsr.getRegionOffice()));
                }
                else {
                    if(StringUtils.hasText(wsr.getSubregionOffice()) && !"ALL".equalsIgnoreCase(wsr.getSubregionOffice())) {
                        query.addCriteria(Criteria.where("subRegionOffice").is(wsr.getSubregionOffice()));
                    }
                }
                if(!"ALL".equalsIgnoreCase(wsr.getSampleName()) && StringUtils.hasText(wsr.getSampleName())) {
                    query.addCriteria(Criteria.where("sampleName").is(wsr.getSampleName()));
                }
                if(!"ALL".equalsIgnoreCase(wsr.getPrgName()) && StringUtils.hasText(wsr.getPrgName())) {
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
                    .lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(wdr.getToDate()+ " 23:59:59.000+0000"))).with(Sort.by(Sort.Direction.ASC, "readDate"));

            List<WaterMasterData> stationList= mongoTemplate.find(query, WaterMasterData.class);
            Map<String,String> msd = new LinkedHashMap<String, String>();

            List<WaterDataResponseVo>   waterDataResponseVoList = new ArrayList<WaterDataResponseVo>();
            for(WaterMasterData  waterMasterData : stationList) {
                WaterDataResponseVo waterDataResponseVo = new WaterDataResponseVo();
                for(int i=0;i<wdr.requestWaterParam.size();i++){
                    Query queryparam = new Query();
                    queryparam.addCriteria(Criteria.where("className").is(waterMasterData.getClassName()));
                    List<WaterParamStdMasterData> classList= mongoTemplate.find(queryparam,WaterParamStdMasterData.class);
                    for(WaterParamStdMasterData  stdParamMaster : classList) {

                        if (wdr.getRequestWaterParam().get(i).equalsIgnoreCase("pH")) {

                            waterDataResponseVo.setpH(waterMasterData.getpH());
                            waterDataResponseVo.setStdMaxpH(stdParamMaster.getStdMaxpH());
                            waterDataResponseVo.setStdMinpH(stdParamMaster.getStdMinpH());
                        }
                        if (wdr.getRequestWaterParam().get(i).equalsIgnoreCase("fecalColiform")) {
                            waterDataResponseVo.setFecalColiform(waterMasterData.getFecalColiform());
                            waterDataResponseVo.setStdFecalCholiform(stdParamMaster.getStdFecalCholiform());
                        }
                        if (wdr.getRequestWaterParam().get(i).equalsIgnoreCase("BOD")) {
                            waterDataResponseVo.setBOD(waterMasterData.getBOD());
                            waterDataResponseVo.setstdBod(stdParamMaster.getstdBod());
                        }
                        if (wdr.getRequestWaterParam().get(i).equalsIgnoreCase("dissolvedOxygen")) {
                            waterDataResponseVo.setDissolvedOxygen(waterMasterData.getDissolvedOxygen());
                            waterDataResponseVo.setStdDissolvedOxygen(stdParamMaster.getStdDissolvedOxygen());
                        }
                        if (wdr.getRequestWaterParam().get(i).equalsIgnoreCase("Nitrate")) {
                            waterDataResponseVo.setNitrate(waterMasterData.getNitrate());
                            waterDataResponseVo.setStdNitrate(stdParamMaster.getStdNitrate());
                        }
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


    public List<CetpStationListResponseVo> getCetpStationList(CetpStationListRequest csr ) {

        try {
            List<CetpStationMaster> cetpList = null;
            List<CetpStationListResponseVo> cetpStationListresponseVoList = new ArrayList<CetpStationListResponseVo>();

            Query query = new Query();
            if ("ALL".equalsIgnoreCase(csr.getRegionOffice())) {
                cetpList = cetpStationMasterRepository.findAll();
            }
            else {
                if(StringUtils.hasText(csr.getRegionOffice()) && "ALL".equalsIgnoreCase(csr.getSubRegionOffice())) {
                    query.addCriteria(Criteria.where("regionOffice").is(csr.getRegionOffice()));
                }
                if(StringUtils.hasText(csr.getSubRegionOffice()) && !"ALL".equalsIgnoreCase(csr.getSubRegionOffice())) {
                    query.addCriteria(Criteria.where("subRegionOffice").is(csr.getSubRegionOffice()));
                }
                if(!"ALL".equalsIgnoreCase(csr.getCetpStatus()) && !csr.getCetpStatus().isEmpty()) {
                    query.addCriteria(Criteria.where("cetpStatus").is(csr.getCetpStatus()));
                }
                cetpList = mongoTemplate.find(query, CetpStationMaster.class);
            }

            for (CetpStationMaster cetpStationMasterData : cetpList) {
                CetpStationListResponseVo cetpDataResponseVo = new CetpStationListResponseVo();

                cetpDataResponseVo.setName(cetpStationMasterData.getName());
                cetpDataResponseVo.setCode(cetpStationMasterData.getCode());
                cetpDataResponseVo.setDistrict(cetpStationMasterData.getDistrict());
                cetpDataResponseVo.setFreqName(cetpStationMasterData.getFreqName());
                cetpDataResponseVo.setCetpStatus(cetpStationMasterData.getCetpStatus());
                cetpStationListresponseVoList.add(cetpDataResponseVo);
            }
            return cetpStationListresponseVoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CetpStationDetailsResponseVo> getCetpStationInfo(CetpStationDetailsRequest cdr)
    {
        try {
            List<String> param = new ArrayList<String>();
            Query query = new Query();
            List<CetpStationMaster> stList =null;
            List<CetpStationDetailsResponseVo>   cetpDataResponseVoList = new ArrayList<CetpStationDetailsResponseVo>();

            query.addCriteria(Criteria.where("code").is(cdr.getCode()));

            query.addCriteria(Criteria.where("inletDate")
                    .gte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(cdr.getFromDate() + " 00:00:00.000+0000"))
                    .lte(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(cdr.getToDate()+ " 23:59:59.000+0000"))).with(Sort.by(Sort.Direction.ASC, "inletDate"));

            List<CetpDataMaster> stationList= mongoTemplate.find(query, CetpDataMaster.class);

            for(CetpDataMaster  cetpDataMaster : stationList) {
                CetpStationDetailsResponseVo  cetpDataResponseVo = new CetpStationDetailsResponseVo();
                for(int i=0;i<cdr.requestCetpParam.size();i++){
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("pH")){
                        cetpDataResponseVo.setpH(cetpDataMaster.getPh());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("cod")){
                        cetpDataResponseVo.setCOD(cetpDataMaster.getCod());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("bod") ){
                        cetpDataResponseVo.setBOD(cetpDataMaster.getBod());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("ss")){
                        cetpDataResponseVo.setSs(cetpDataMaster.getSs());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("tan")){
                        cetpDataResponseVo.setTan(cetpDataMaster.getTan());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("ong")){
                        cetpDataResponseVo.setOng(cetpDataMaster.getOng());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("rcl")){
                        cetpDataResponseVo.setRcl(cetpDataMaster.getRcl());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("cl")){
                        cetpDataResponseVo.setCl(cetpDataMaster.getCl());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("s")){
                        cetpDataResponseVo.setS(cetpDataMaster.getS());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("tkn")){
                        cetpDataResponseVo.setTkn(cetpDataMaster.getTkn());
                    }
                    if(cdr.getRequestCetpParam().get(i).equalsIgnoreCase("tds")){
                        cetpDataResponseVo.setTds(cetpDataMaster.getTds());
                    }
                }
                cetpDataResponseVo.setInletDate(cetpDataMaster.getInletDate());
                cetpDataResponseVoList.add(cetpDataResponseVo);
            }
            return cetpDataResponseVoList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}