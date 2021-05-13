package com.eai.idss.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class CetpDataMaster {

    private long code;
    private double pH;
    private double bod;
    private double cod;
    private double ss;
    private double og;
    private double tan;
    private double rcl;
    private double cl;
    private double s;
    private double tkn;
    private double tds;
    private double ong;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate inletDate;

    public double getPh() {
        return pH;
    }

    public void setpH( double pH)
    {
        this.pH=pH;
    }

    public LocalDate getInletDate(){
        return inletDate   ;
    }

    public void setInletDate(LocalDate inletDate)
    {
        this.inletDate=inletDate;
    }

    public double getBod()
    {
        return bod;
    }
    public void setBod(double bod)
    {
        this.bod=bod;
    }
    public double getCod()
    {
        return  cod;
    }
    public void setCod(double cod)
    {
        this.cod=cod;
    }
    public double getSs()
    {
        return ss;
    }
    public void setSs(double ss)
    {
        this.ss=ss;
    }

    public double getTan()
    {
        return tan;
    }
    public void setTan(double tan)
    {
        this.tan=tan;
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
