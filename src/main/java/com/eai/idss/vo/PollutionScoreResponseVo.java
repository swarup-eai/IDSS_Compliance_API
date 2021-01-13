package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

public class PollutionScoreResponseVo {

	private Map<String,List<PollutionParamGroupVo>> pollutionScore;

	public Map<String, List<PollutionParamGroupVo>> getPollutionScore() {
		return pollutionScore;
	}

	public void setPollutionScore(Map<String, List<PollutionParamGroupVo>> pollutionScore) {
		this.pollutionScore = pollutionScore;
	}

}
