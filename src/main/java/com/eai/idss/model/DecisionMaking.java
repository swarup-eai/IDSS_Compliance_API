package com.eai.idss.model;

import java.time.LocalDate;
import java.util.Date;

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
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
//	private double 
	

}
