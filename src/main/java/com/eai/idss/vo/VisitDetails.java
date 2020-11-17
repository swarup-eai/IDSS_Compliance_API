package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

import com.eai.idss.model.Visits;

public class VisitDetails {

	private Visits visit;
	private Map<String,List<TileVo>> visitSteps;
	private long pastVisits;
	
	public long getPastVisits() {
		return pastVisits;
	}
	public void setPastVisits(long pastVisits) {
		this.pastVisits = pastVisits;
	}
	public Visits getVisit() {
		return visit;
	}
	public void setVisit(Visits visit) {
		this.visit = visit;
	}
	public Map<String, List<TileVo>> getVisitSteps() {
		return visitSteps;
	}
	public void setVisitSteps(Map<String, List<TileVo>> visitSteps) {
		this.visitSteps = visitSteps;
	}
	
}
