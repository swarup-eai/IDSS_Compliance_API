package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.text.BreakIterator;

@Document(collection = "waterParamStdMaster")
public class WaterParamStdMasterData {

    private String className;
    private Double stdMinpH;
    private Double stdMaxpH;
    private Double stdBod;
    private Double stdDissolvedOxygen;
    private Double stdNitrate;
    private Double stdFecalCholiform;

    public String getClassName()
    {
        return className;
    }
    public  void  setClassName(String className)
    {
        this.className=className;
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
