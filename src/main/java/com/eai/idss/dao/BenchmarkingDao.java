package com.eai.idss.dao;

import com.eai.idss.vo.DashboardRequest;
import com.eai.idss.vo.SkuDetailVo;

import java.util.List;

public interface BenchmarkingDao {
    public List<SkuDetailVo> getSkuDetail(DashboardRequest dr);

    public List<SkuDetailVo> getAirPollutant(DashboardRequest dr);

    public List<SkuDetailVo> getWaterPollutant(DashboardRequest dr);

    public List<SkuDetailVo> getEffluents(DashboardRequest dr);

}
