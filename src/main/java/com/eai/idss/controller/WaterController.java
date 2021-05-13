package com.eai.idss.controller;

import java.io.IOException;
import java.util.List;

import com.eai.idss.vo.*;

import com.eai.idss.model.WaterMasterData;
import com.eai.idss.model.WaterStationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eai.idss.dao.WaterDao;

@RestController
@CrossOrigin(origins ={"http://localhost:4200", "http://10.10.10.32:8080"})
public class WaterController {

    @Autowired
    private WaterDao wd;

    @RequestMapping(method = RequestMethod.POST,value="/Water-station-list",produces="application/json")
    public ResponseEntity<List<WaterStationListResponseVo>> getWaterStationList(@RequestBody WaterStationRegionRequest wsr)
    {
        List<WaterStationListResponseVo> waterStationList=null;
        waterStationList= wd.getWaterStationList(wsr);
        return new ResponseEntity< List<WaterStationListResponseVo>>( waterStationList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value="/water-station-info",produces ="application/json")
    public ResponseEntity<List<WaterDataResponseVo>> getWaterStationInfo(@RequestBody WaterDetailRequest wdr )throws IOException
    {
        List<WaterDataResponseVo> waterStationData=null;
        waterStationData = wd.getWaterStationDetailStationId(wdr);
        return new ResponseEntity< List<WaterDataResponseVo>>( waterStationData, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value ="/cetp-station-list",produces ="application/json")
    public ResponseEntity<List<CetpStationListResponseVo>>getCetpStationList(@RequestBody CetpStationListRequest csr) throws IOException
    {
        List<CetpStationListResponseVo> cetpStationList=null;
        cetpStationList = wd.getCetpStationList(csr);

        return new ResponseEntity<List<CetpStationListResponseVo>>(cetpStationList,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/cetp-station-info",produces = "application/json")
    public ResponseEntity<List<CetpStationDetailsResponseVo>> getCetpStationInfo(@RequestBody CetpStationDetailsRequest cdr)
    {
        List<CetpStationDetailsResponseVo> cetpStationInfo=null;
        cetpStationInfo=wd.getCetpStationInfo(cdr);

        return new ResponseEntity<List<CetpStationDetailsResponseVo>>(cetpStationInfo,HttpStatus.OK);

    }
    // public List<CetpStationListResponseVo> getCetpStationList(CetpStationListRequest csr);
}
