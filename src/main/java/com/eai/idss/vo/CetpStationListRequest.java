package com.eai.idss.vo;

import java.security.SecureRandom;

public class CetpStationListRequest {

    private String regionOffice;
    private String subRegionOffice;
    private String cetpStatus;
    private int code;

    public int getCode()
    {
        return code;
    }
    public void setCode(int code)
    {
        this.code=code;
    }

    public  void setRegionOffice(String regionOffice)
    {
        this.regionOffice=regionOffice;
    }
    public String getRegionOffice() {
        return regionOffice;
    }

    public void setSubRegionOffice(String subRegionOffice){
        this.subRegionOffice=subRegionOffice;
    }
    public String getSubRegionOffice()
    {
        return subRegionOffice;
    }

    public  void setCetpStatus(String cetpStatus)
    {
        this.cetpStatus=cetpStatus;
    }
    public String getCetpStatus()
    {
        return  cetpStatus;
    }
}
