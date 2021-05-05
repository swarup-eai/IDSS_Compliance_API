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

    private double fecalColiform;
    private double pH;
    private double BOD;
    private double COD;
    private double dissolvedOxygen;
    private double nitrate;
    public List<String > requestWaterParam;


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

    public double getpH()
    {
        return pH;
    }
    public  void setpH(double pH)
    {
        this.pH=pH;
    }

    public double getFecalColiform()
    {
        return fecalColiform;
    }
    public void setFecalColiform(double fecalColiform)
    {
        this.fecalColiform=fecalColiform;
    }

    public double getBOD()
    {
        return BOD;
    }
    public void setBOD(double BOD)
    {
        this.BOD=BOD;
    }

    public  double getCOD()
    {
        return  COD;
    }
    public void  setCOD(double COD)
    {
        this.COD=COD;
    }

    public  double getDissolvedOxygen()
    {
        return dissolvedOxygen;
    }
    public  void setDissolvedOxygen(double dissolvedOxygen)
    {
        this.dissolvedOxygen=dissolvedOxygen;
    }

    public double getNitrate()
    {
        return  nitrate;
    }
    public void setNitrate(double nitrate)
    {
        this.nitrate=nitrate;
    }

}
