package com.eai.idss.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.time.LocalDate;

@Document(collection = "waterStationMaster")
public class WaterStationMaster {

    private long  stnId;
    private String stnName;
    private String regionOffice;
    private String sampleName;
    private String prgName;
    private String distName;
    private String stnCriteria;

    public long getStnId() {
        return stnId;
    }
    public void setStnId(long stnId){
        this.stnId=stnId;
    }

    public String getStnName()
    {
        return stnName;
    }
    public void setStnName(String stnName)
    {
        this.stnName=stnName;
    }

    public String getRegionOffice() {
        return regionOffice;
    }
    public  void setRegionOffice(String regionOffice) {
        this.regionOffice=regionOffice;
    }

    public String getSampleName()
    {
        return sampleName;
    }
    public void setSampleName(String sampleName)
    {
        this.sampleName=sampleName;
    }

    public String getPrgName()
    {
        return  prgName;
    }
    public void setPrgName(String prgName)
    {
        this.prgName=prgName;
    }

    public  String getDistName() {
        return distName;
    }
    public void setDistName(String distName) {
        this.distName=distName;
    }

    public String getStnCriteria()
    {
        return  stnCriteria;
    }
    public void setStnCriteria(String stnCriteria)
    {
        this.stnCriteria=stnCriteria;
    }
}




