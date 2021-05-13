package com.eai.idss.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class CetpStationDetailsResponseVo {

    private double pH;
    private double BOD;
    private double COD;
    private double ss;
    private double tan;
    private double rcl;
    private double cl;
    private double s;
    private double tkn;
    private double tds;
    private  double ong;

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

    public double getCOD()
    {
        return  COD;
    }
    public void  setCOD(double COD)
    {
        this.COD=COD;
    }
    public double getSs()
    {
        return ss;
    }
    public void setSs(double ss)
    {
        this.ss=ss;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate inletDate;

    public LocalDate getInletDate(){
        return inletDate   ;
    }

    public void setInletDate(LocalDate inletDate)
    {
        this.inletDate=inletDate;
    }

    public  void setTan(double tan)
    {
        this.tan=tan;
    }

    public double getTan() {
        return tan;
    }

    public double getRcl()
    {
        return rcl;
    }
    public void setRcl(double rcl)
    {
        this.rcl=rcl;
    }

    public double getCl()
    {
        return cl;
    }
    public void setCl(double cl)
    {
        this.cl=cl;
    }

    public double getS()
    {
        return s;
    }
    public void setS(double s)
    {
        this.s=s;
    }

    public double getTkn()
    {
        return tkn;
    }
    public void setTkn(double tkn)
    {
        this.tkn=tkn;
    }

    public double getTds()
    {
        return tds;
    }
    public void setTds(double tds)
    {
        this.tds=tds;
    }

    public double getOng()
    {
        return ong;
    }
    public void setOng(double tds)
    {
        this.ong=ong;
    }
}
