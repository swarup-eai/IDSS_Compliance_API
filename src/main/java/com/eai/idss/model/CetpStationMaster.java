package com.eai.idss.model;

public class CetpStationMaster {

    private int code;
    private String name;
    private String district;
    private String cetpStatus;
    private String freqName;
    private String regionOffice;
    private String subRegionOffice;


    public int getCode()
    {
        return code;
    }
    public void setCode(int code)
    {
        this.code=code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district=district;
    }

    public String getCetpStatus() {
        return cetpStatus;
    }
    public void setCetpStatus(String cetpStatus) {
        this.cetpStatus=cetpStatus;
    }

    public  void setRegionOffice(String regionOffice) {
        this.regionOffice=regionOffice;
    }

    public String getRegionOffice() {
        return regionOffice;
    }

    public  String getSubRegionOffice()
    {
        return subRegionOffice;
    }
    public void setSubRegionOffice(String subRegionOffice)
    {
        this.subRegionOffice=subRegionOffice;
    }

    public  String getFreqName()
    {
        return freqName;
    }
    public void setFreqName(String freqName)
    {
        this.freqName=freqName;
    }

}
