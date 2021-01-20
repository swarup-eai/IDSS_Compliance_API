package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

public class ComparisonTableResponseVo {

	private Map<String,List<ComparisonTableParamGroupVo>> comparisonTable;

	public Map<String, List<ComparisonTableParamGroupVo>> getComparisonTable() {
		return comparisonTable;
	}

	public void setComparisonTable(Map<String, List<ComparisonTableParamGroupVo>> comparisonTable) {
		this.comparisonTable = comparisonTable;
	}


}
