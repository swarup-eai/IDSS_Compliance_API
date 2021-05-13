package com.eai.idss.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "waterDataMaster")
public class WaterMasterData {

    private long stnId;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate readDate;

    private Double fecalColiform;
    private Double pH;
    private Double BOD;
    private Double COD;
    private Double dissolvedOxygen;
    private Double nitrate;
    public List<String > requestWaterParam;

    private String  className;


    public long getStnId() {
        return stnId;
    }
    public void setStnId(long stnId){
        this.stnId=stnId;
    }

    public LocalDate getReadDate() {
        return readDate;
    }
    public void  setReadDate(LocalDate readDate)
    {
        this.readDate=readDate;
    }

    public  List<String > getRequestWaterParam()
    {
        return requestWaterParam;
    }
    public void setRequestWaterParam( List<String > requestWaterParam)
    {
        this.requestWaterParam=requestWaterParam;
    }

    public Double getpH()
    {
        return pH;
    }
    public  void setpH(Double pH)
    {
        this.pH=pH;
    }

    public Double getFecalColiform()
    {
        return fecalColiform;
    }
    public void setFecalColiform(Double fecalColiform)
    {
        this.fecalColiform=fecalColiform;
    }

    public Double getBOD()
    {
        return BOD;
    }
    public void setBOD(Double BOD)
    {
        this.BOD=BOD;
    }

    public  Double getCOD()
    {
        return  COD;
    }
    public void  setCOD(Double COD)
    {
        this.COD=COD;
    }

    public  Double getDissolvedOxygen()
    {
        return dissolvedOxygen;
    }
    public  void setDissolvedOxygen(Double dissolvedOxygen)
    {
        this.dissolvedOxygen=dissolvedOxygen;
    }

    public Double getNitrate()
    {
        return  nitrate;
    }
    public void setNitrate(Double nitrate)
    {
        this.nitrate=nitrate;
    }

    public String getClassName()
    {
        return className;
    }
    public  void  setClassName(String className)
    {
        this.className=className;
    }

}
