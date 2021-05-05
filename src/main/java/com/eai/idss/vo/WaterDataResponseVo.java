package com.eai.idss.vo;

import java.time.LocalDate;
import java.util.List;

public class WaterDataResponseVo {



   // public String value;

    public     LocalDate date;
//    public String getValue()
//    {
//        return value;
//    }
//    public void setValue(String value)
//    {
//        this.value=value;
//    }
    public LocalDate getDate()
    {
        return date;
    }
    public  void setDate(LocalDate date)
    {
        this.date=date;
    }


//    private List<String> paramWater;
//    public List<String> getParamWater() {
//        return paramWater;
//    }
//
//    public void setParamWater(List<String> param) {
//        this.paramWater = paramWater;
//    }

    public double pH;
    public double BOD;
    public double COD;
    public double dissolvedOxygen;
    public double nitrate;
    public double fecalColiform;

    public double getFecalColiform()
    {
        return  fecalColiform;
    }
    public  void setFecalColiform(double fecalColiform)
    {
        this.fecalColiform=fecalColiform;
    }

    public double getpH()
    {
        return pH;
    }
    public void setpH(double pH)
    {
        this.pH=pH;
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
