package com.eai.idss.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Decision_making")
public class DecisionMaking {

	@Id
	private String _id;
	private long industryId;
	private long visitId;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate visitedDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate schduledOn;
	private double disposalIndustrialAsPerConsent;
	private double disposalDomesticAsPerConsent;
	private double bgImposed; 
	private double legalAnyComplaint;
	private double omsAmbientInstalled;
	private double omswPlacedProperly;
	private double omswInstalled;
	private double omsStackProperlyPlaced;
	private double omsStackInstalled;
	private double stackFacilityExist;
	private double stpOperational;
	private double etpOperational;
	private double percentPlantation;

	private double requiredDisposalIndustrialAsPerConsent;
	private double requiredDisposalDomesticAsPerConsent;
	private double requiredBgImposed;
	private double requiredLegalAnyComplaint;
	private double requiredOmsAmbientInstalled;
	private double requiredOmswPlacedProperly;
	private double requiredOmswInstalled;
	private double requiredOmsStackProperlyPlaced;
	private double requiredOmsStackInstalled;
//	private double 
//	private double 
//	private double 
//	private double 
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}
	public long getVisitId() {
		return visitId;
	}
	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}
	public LocalDate getVisitedDate() {
		return visitedDate;
	}
	public void setVisitedDate(LocalDate visitedDate) {
		this.visitedDate = visitedDate;
	}
	public LocalDate getSchduledOn() {
		return schduledOn;
	}
	public void setSchduledOn(LocalDate schduledOn) {
		this.schduledOn = schduledOn;
	}
	public double getDisposalIndustrialAsPerConsent() {
		return disposalIndustrialAsPerConsent;
	}
	public void setDisposalIndustrialAsPerConsent(double disposalIndustrialAsPerConsent) {
		this.disposalIndustrialAsPerConsent = disposalIndustrialAsPerConsent;
	}
	public double getDisposalDomesticAsPerConsent() {
		return disposalDomesticAsPerConsent;
	}
	public void setDisposalDomesticAsPerConsent(double disposalDomesticAsPerConsent) {
		this.disposalDomesticAsPerConsent = disposalDomesticAsPerConsent;
	}
	public double getRequiredDisposalIndustrialAsPerConsent() {
		return requiredDisposalIndustrialAsPerConsent;
	}
	public void setRequiredDisposalIndustrialAsPerConsent(double requiredDisposalIndustrialAsPerConsent) {
		this.requiredDisposalIndustrialAsPerConsent = requiredDisposalIndustrialAsPerConsent;
	}
	public double getRequiredDisposalDomesticAsPerConsent() {
		return requiredDisposalDomesticAsPerConsent;
	}
	public void setRequiredDisposalDomesticAsPerConsent(double requiredDisposalDomesticAsPerConsent) {
		this.requiredDisposalDomesticAsPerConsent = requiredDisposalDomesticAsPerConsent;
	}
	
}
