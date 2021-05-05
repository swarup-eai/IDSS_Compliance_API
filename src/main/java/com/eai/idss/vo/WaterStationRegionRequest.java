package com.eai.idss.vo;

public class WaterStationRegionRequest {

    private String regionOffice;
    private String subregionOffice;
    private String sampleName;
    private String prgName;

    public  void setRegionOffice(String regionOffice)
    {
        this.regionOffice=regionOffice;
    }
    public String getRegionOffice() {
        return regionOffice;
    }

    public String getSubregionOffice()
    {
        return subregionOffice;
    }
    public  void setSubregionOffice(String subregionOffice)
    {
        this.subregionOffice = subregionOffice;
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
}
