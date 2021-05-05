package com.eai.idss.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
public class WaterDetailRequest {

    private  long stnId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate fromDate;


    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate toDate;

    public long getStnId()
    {
        return  stnId;
    }
    public void setStnId(long stnId)
    {
        this.stnId=stnId;
    }
    public LocalDate getFromDate(){
        return fromDate;
    }
    public void  setFromDate(LocalDate fromDate)
    {
        this.fromDate=fromDate;
    }

    public  LocalDate getToDate()
    {
        return toDate;
    }
    public  void  setToDate()
    {
        this.toDate=toDate;
    }

    public List<String> requestWaterParam;

    public List<String> getRequestWaterParam()
    {
        return requestWaterParam;
    }
    public void setRequestWaterParam(List<String> requestWaterParam)
    {
        this.requestWaterParam=requestWaterParam;
    }

}
