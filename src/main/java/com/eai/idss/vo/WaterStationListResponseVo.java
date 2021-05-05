package com.eai.idss.vo;

public class WaterStationListResponseVo {

    private long stnId;
    private String stnName;
    private String distName;
    private String sampleName;
    private String prgName;
    private String stnCriteria;

    public long getStnId() {
        return stnId;
    }

    public void setStnId(long stnId)
    {
        this.stnId=stnId;
    }

    public String getStnName() {
        return stnName;
    }
    public  void setStnName(String stnName) {
        this.stnName=stnName;
    }

    public  String getDistName() {
        return distName;
    }
    public void setDistName(String distName) {
        this.distName=distName;
    }
    public String getSampleName()
    {
        return  sampleName;
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
    public String getStnCriteria()
    {
        return  stnCriteria;
    }
    public void setStnCriteria(String stnCriteria)
    {
        this.stnCriteria=stnCriteria;
    }
}
