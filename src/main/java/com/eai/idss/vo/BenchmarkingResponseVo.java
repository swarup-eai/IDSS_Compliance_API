package com.eai.idss.vo;

import java.util.List;

public class BenchmarkingResponseVo {
    private List<SkuDetailVo> skuDetail;
    private List<SkuDetailVo> airPollutant;
    private List<SkuDetailVo> waterPollutant;
    private List<SkuDetailVo> effluents;
    public List<SkuDetailVo> getSkuDetail() {
        return skuDetail;
    }
    public void setSkuDetail(List<SkuDetailVo> skuDetail) {
        this.skuDetail = skuDetail;
    }

    public List<SkuDetailVo> getAirPollutant() {
        return airPollutant;
    }
    public void setAirPollutant(List<SkuDetailVo> airPollutant) {
        this.airPollutant = airPollutant;
    }


    public List<SkuDetailVo> getWaterPollutant() {
        return waterPollutant;
    }
    public void setWaterPollutant(List<SkuDetailVo> waterPollutant) {
        this.waterPollutant = waterPollutant;
    }

    public List<SkuDetailVo> getEffluents() {
        return effluents;
    }
    public void setEffluents(List<SkuDetailVo> effluents) {
        this.effluents = effluents;
    }




}
