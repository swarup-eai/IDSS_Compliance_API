package com.eai.idss.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "Directions")
public class Directions {

    private long industryId;

    private LocalDate statusUpdatedOn;

    public LocalDate getStatusUpdatedOn() {
        return statusUpdatedOn;
    }
    public void setStatusUpdatedOn(LocalDate statusUpdatedOn) {
        this.statusUpdatedOn = statusUpdatedOn;
    }
	public long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}

    
}
