package com.eai.idss.controller;

import com.eai.idss.dao.BenchmarkingDao;
import com.eai.idss.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins ={"http://localhost:4200", "http://10.10.10.32:8080"})
public class BenchmarkingController {
    @Autowired
    BenchmarkingDao benchmarkingDao;
    @RequestMapping(method = RequestMethod.POST, value = "/benchmarking", produces = "application/json")
    public ResponseEntity<BenchmarkingResponseVo> getBenchmarkingData(@RequestBody BenchmarkingRequest br) throws IOException {
        BenchmarkingResponseVo dr = new BenchmarkingResponseVo();
        try {
            dr.setSkuDetail(benchmarkingDao.getSkuDetail(br));
            dr.setAirPollutant(benchmarkingDao.getAirPollutant(br));
            dr.setWaterPollutant(benchmarkingDao.getWaterPollutant(br));
            dr.setEffluents(benchmarkingDao.getEffluents(br));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Exception in /benchmarking", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<BenchmarkingResponseVo>(dr,HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/benchmarking/industryType", produces = "application/json")
    public ResponseEntity<List<IndustryNameByTypeVo>> getVisitsDetailsForOneVisit(@RequestParam(required = true) String type) throws IOException {
        List<IndustryNameByTypeVo> industryNameByTypeVoList = null;
        try {
            industryNameByTypeVoList = benchmarkingDao.getIndustryNameByType(type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Exception in /benchmakring/{industryType}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<IndustryNameByTypeVo>>(industryNameByTypeVoList,HttpStatus.OK);
    }

}
