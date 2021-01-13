package com.eai.idss.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Date;

public class VisitScheduleCurrentMonthResponseVo {
    String scale;
    String category;
    private String type;
    private String industryName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduledOn;
    private String latitude;
    private String longitude;

    public String getScale() {
        return scale;
    }
    public void setScale(String scale) {
        this.scale = scale;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
    public LocalDate getScheduledOn() {
        return scheduledOn;
    }
    public void setScheduledOn(LocalDate scheduledOn) {
        this.scheduledOn = scheduledOn;
    }
    public String  getLatitude() {
        return latitude;
    }
    public void setLatitude(String  latitude) {
        this.latitude = latitude;
    }
    public String  getLongitude() {
        return longitude;
    }
    public void setLongitude(String  longitude) {
        this.longitude = longitude;
    }
}
