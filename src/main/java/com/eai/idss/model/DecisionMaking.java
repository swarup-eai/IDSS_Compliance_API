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
	private double requiredStpOperational;
	private double requiredEtpOperational;
	private double requiredStackFacilityExist;
	private double requiredPercentPlantation;
	
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
	public double getBgImposed() {
		return bgImposed;
	}
	public void setBgImposed(double bgImposed) {
		this.bgImposed = bgImposed;
	}
	public double getLegalAnyComplaint() {
		return legalAnyComplaint;
	}
	public void setLegalAnyComplaint(double legalAnyComplaint) {
		this.legalAnyComplaint = legalAnyComplaint;
	}
	public double getOmsAmbientInstalled() {
		return omsAmbientInstalled;
	}
	public void setOmsAmbientInstalled(double omsAmbientInstalled) {
		this.omsAmbientInstalled = omsAmbientInstalled;
	}
	public double getOmswPlacedProperly() {
		return omswPlacedProperly;
	}
	public void setOmswPlacedProperly(double omswPlacedProperly) {
		this.omswPlacedProperly = omswPlacedProperly;
	}
	public double getOmswInstalled() {
		return omswInstalled;
	}
	public void setOmswInstalled(double omswInstalled) {
		this.omswInstalled = omswInstalled;
	}
	public double getOmsStackProperlyPlaced() {
		return omsStackProperlyPlaced;
	}
	public void setOmsStackProperlyPlaced(double omsStackProperlyPlaced) {
		this.omsStackProperlyPlaced = omsStackProperlyPlaced;
	}
	public double getOmsStackInstalled() {
		return omsStackInstalled;
	}
	public void setOmsStackInstalled(double omsStackInstalled) {
		this.omsStackInstalled = omsStackInstalled;
	}
	public double getStackFacilityExist() {
		return stackFacilityExist;
	}
	public void setStackFacilityExist(double stackFacilityExist) {
		this.stackFacilityExist = stackFacilityExist;
	}
	public double getStpOperational() {
		return stpOperational;
	}
	public void setStpOperational(double stpOperational) {
		this.stpOperational = stpOperational;
	}
	public double getEtpOperational() {
		return etpOperational;
	}
	public void setEtpOperational(double etpOperational) {
		this.etpOperational = etpOperational;
	}
	public double getPercentPlantation() {
		return percentPlantation;
	}
	public void setPercentPlantation(double percentPlantation) {
		this.percentPlantation = percentPlantation;
	}
	public double getRequiredBgImposed() {
		return requiredBgImposed;
	}
	public void setRequiredBgImposed(double requiredBgImposed) {
		this.requiredBgImposed = requiredBgImposed;
	}
	public double getRequiredLegalAnyComplaint() {
		return requiredLegalAnyComplaint;
	}
	public void setRequiredLegalAnyComplaint(double requiredLegalAnyComplaint) {
		this.requiredLegalAnyComplaint = requiredLegalAnyComplaint;
	}
	public double getRequiredOmsAmbientInstalled() {
		return requiredOmsAmbientInstalled;
	}
	public void setRequiredOmsAmbientInstalled(double requiredOmsAmbientInstalled) {
		this.requiredOmsAmbientInstalled = requiredOmsAmbientInstalled;
	}
	public double getRequiredOmswPlacedProperly() {
		return requiredOmswPlacedProperly;
	}
	public void setRequiredOmswPlacedProperly(double requiredOmswPlacedProperly) {
		this.requiredOmswPlacedProperly = requiredOmswPlacedProperly;
	}
	public double getRequiredOmswInstalled() {
		return requiredOmswInstalled;
	}
	public void setRequiredOmswInstalled(double requiredOmswInstalled) {
		this.requiredOmswInstalled = requiredOmswInstalled;
	}
	public double getRequiredOmsStackProperlyPlaced() {
		return requiredOmsStackProperlyPlaced;
	}
	public void setRequiredOmsStackProperlyPlaced(double requiredOmsStackProperlyPlaced) {
		this.requiredOmsStackProperlyPlaced = requiredOmsStackProperlyPlaced;
	}
	public double getRequiredOmsStackInstalled() {
		return requiredOmsStackInstalled;
	}
	public void setRequiredOmsStackInstalled(double requiredOmsStackInstalled) {
		this.requiredOmsStackInstalled = requiredOmsStackInstalled;
	}
	public double getRequiredStpOperational() {
		return requiredStpOperational;
	}
	public void setRequiredStpOperational(double requiredStpOperational) {
		this.requiredStpOperational = requiredStpOperational;
	}
	public double getRequiredEtpOperational() {
		return requiredEtpOperational;
	}
	public void setRequiredEtpOperational(double requiredEtpOperational) {
		this.requiredEtpOperational = requiredEtpOperational;
	}
	public double getRequiredStackFacilityExist() {
		return requiredStackFacilityExist;
	}
	public void setRequiredStackFacilityExist(double requiredStackFacilityExist) {
		this.requiredStackFacilityExist = requiredStackFacilityExist;
	}
	public double getRequiredPercentPlantation() {
		return requiredPercentPlantation;
	}
	public void setRequiredPercentPlantation(double requiredPercentPlantation) {
		this.requiredPercentPlantation = requiredPercentPlantation;
	}
}
