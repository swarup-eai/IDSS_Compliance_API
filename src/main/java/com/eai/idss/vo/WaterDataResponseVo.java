package com.eai.idss.vo;

import java.time.LocalDate;
import java.util.List;

public class WaterDataResponseVo {



   // public String value;

    private LocalDate date;
    private Double pH;
    private Double BOD;
    private Double COD;
    private Double dissolvedOxygen;
    private Double nitrate;
    private Double fecalColiform;
    private Double stdMinpH;
    private Double stdMaxpH;

    private String className;

    private Double stdBod;
    private Double stdDissolvedOxygen;
    private Double stdNitrate;
    private Double stdFecalCholiform;

    public LocalDate getDate()
    {
        return date;
    }
    public  void setDate(LocalDate date)
    {
        this.date=date;
    }

    public Double getFecalColiform()
    {
        return  fecalColiform;
    }
    public  void setFecalColiform(Double fecalColiform)
    {
        this.fecalColiform=fecalColiform;
    }

    public Double getpH()
    {
        return pH;
    }
    public void setpH(Double pH)
    {
        this.pH=pH;
    }

    public Double getBOD()
    {
        return BOD;
    }
    public void setBOD(Double BOD)
    {
        this.BOD=BOD;
    }

    public Double getCOD()
    {
        return  COD;
    }
    public void  setCOD(Double COD)
    {
        this.COD=COD;
    }

    public Double getDissolvedOxygen()
    {
        return dissolvedOxygen;
    }
    public void setDissolvedOxygen(Double dissolvedOxygen)
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

    public void setStdMinpH(Double stdMinpH) {
        this.stdMinpH=stdMinpH;
    }
    public Double getStdMinpH() {
        return stdMinpH;
    }

    public void setStdMaxpH(Double stdMaxpH) {
        this.stdMaxpH=stdMaxpH;
    }
    public Double getStdMaxpH()
    {
        return stdMaxpH;
    }

    public String getClassName()
    {
        return className;
    }
    public  void  setClassName(String className)
    {
        this.className=className;
    }

    public void setstdBod(Double stdBod) {
        this.stdBod=stdBod;
    }
    public Double getstdBod()
    {
        return stdBod;
    }

    public void setStdNitrate(Double stdNitrate) {
        this.stdNitrate=stdNitrate;
    }
    public Double getStdNitrate()
    {
        return stdNitrate;
    }

    public void setStdFecalCholiform(Double stdFecalCholiform) {
        this.stdFecalCholiform=stdFecalCholiform;
    }
    public Double getStdFecalCholiform()
    {
        return stdFecalCholiform;
    }

    public void setStdDissolvedOxygen(Double stdDissolvedOxygen) {
        this.stdDissolvedOxygen=stdDissolvedOxygen;
    }
    public Double getStdDissolvedOxygen()
    {
        return stdDissolvedOxygen;
    }

}
