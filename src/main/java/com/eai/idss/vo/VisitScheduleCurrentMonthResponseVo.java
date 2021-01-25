package com.eai.idss.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VisitScheduleCurrentMonthResponseVo {
    String scale;
    String category;
    private String type;
    private String industryName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduledOn;
    private Double latitude;
    private Double longitude;

    private String priority;

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
    public Double  getLatitude() {
        return latitude;
    }
    public void setLatitude(Double  latitude) {
        this.latitude = latitude;
    }
    public Double  getLongitude() {
        return longitude;
    }
    public void setLongitude(Double  longitude) {
        this.longitude = longitude;
    }
    public String  getPriority() {
        return priority;
    }
    public void setPriority(String  priority) {
        this.priority = priority;
    }
}