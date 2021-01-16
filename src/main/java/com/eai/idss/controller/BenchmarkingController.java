package com.eai.idss.controller;

import com.eai.idss.dao.BenchmarkingDao;
import com.eai.idss.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BenchmarkingController {
    @Autowired
    BenchmarkingDao benchmarkingDao;
    @RequestMapping(method = RequestMethod.POST, value = "/benchmarking", produces = "application/json")
    public ResponseEntity<BenchmarkingResponseVo> getDashboardData(@RequestBody DashboardRequest dbr) throws IOException {
        BenchmarkingResponseVo dr = new BenchmarkingResponseVo();
        try {
            dr.setSkuDetail(benchmarkingDao.getSkuDetail(dbr));
            dr.setAirPollutant(benchmarkingDao.getAirPollutant(dbr));
            dr.setWaterPollutant(benchmarkingDao.getWaterPollutant(dbr));
            dr.setEffluents(benchmarkingDao.getEffluents(dbr));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Exception in /benchmarking", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<BenchmarkingResponseVo>(dr,HttpStatus.OK);
    }

}
