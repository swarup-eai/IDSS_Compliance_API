package com.eai.idss.vo;

import java.util.List;
import java.util.Map;

public class ComparisonTableResponseVo {

	private Map<String,List<ComparisonTableSKUVo>> comparisonTable;

	public Map<String, List<ComparisonTableSKUVo>> getComparisonTable() {
		return comparisonTable;
	}

	public void setComparisonTable(Map<String, List<ComparisonTableSKUVo>> comparisonTable) {
		this.comparisonTable = comparisonTable;
	}


}
