package com.eai.idss.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class CetpStationDetailsRequest {

    private int code;

    public List<String> requestCetpParam;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate toDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate fromDate;

    public void setCode(int code)
    {
        this.code=code;
    }
    public int getCode()
    {
        return code;
    }

    public List<String> getRequestCetpParam()
    {
        return requestCetpParam;
    }
    public void setRequestCetpParam(List<String> requestCetpParam)
    {
        this.requestCetpParam=requestCetpParam;
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

}
