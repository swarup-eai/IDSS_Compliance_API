package com.eai.idss.vo;

public class CetpStationListResponseVo {
    private String name;
    private String regionOffice;
    private String subRegionOffice;
    private String cetpStatus;
    private String freqName;
    private String district;

    private int code;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code=code;
    }

    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }

    public  void setCetpStatus(String cetpStatus)
    {
        this.cetpStatus=cetpStatus;
    }
    public String getCetpStatus()
    {
        return  cetpStatus;
    }

    public void setFreqName(String freqName)
    {
        this.freqName=freqName;
    }
    public String getFreqName()
    {
        return freqName;
    }

    public void setDistrict(String district)
    {
        this.district=district;
    }
    public String getDistrict()
    {
        return district;
    }
}

