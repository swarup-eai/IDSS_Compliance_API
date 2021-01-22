package com.eai.idss.dao;

import com.eai.idss.vo.BenchmarkingRequest;
import com.eai.idss.vo.IndustryNameByTypeVo;
import com.eai.idss.vo.SkuDetailVo;

import java.util.List;

public interface BenchmarkingDao {
    public List<SkuDetailVo> getSkuDetail(BenchmarkingRequest br);

    public List<SkuDetailVo> getAirPollutant(BenchmarkingRequest br);

    public List<SkuDetailVo> getWaterPollutant(BenchmarkingRequest br);

    public List<SkuDetailVo> getEffluents(BenchmarkingRequest br);

    public List<IndustryNameByTypeVo> getIndustryNameByType(String type);

}
