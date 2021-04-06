package com.eai.idss.vo;

public class ComplianceScoreTableResponseVo {
    private String complianceScoreDistribution;
    private int scoreObtained;
    private int scoreOutOf;

    public ComplianceScoreTableResponseVo(String complianceScoreDistribution,int scoreObtained,int scoreOutOf){
        this.complianceScoreDistribution = complianceScoreDistribution;
        this.scoreObtained = scoreObtained;
        this.scoreOutOf = scoreOutOf;
    }
    public String getComplianceScoreDistribution() {
        return complianceScoreDistribution;
    }
    public void setComplianceScoreDistribution(String complianceScoreDistribution) {
        this.complianceScoreDistribution = complianceScoreDistribution;
    }

    public int getScoreObtained() {
        return scoreObtained;
    }
    public void setScoreObtained(int scoreObtained) {
        this.scoreObtained = scoreObtained;
    }

    public int getScoreOutOf() {
        return scoreOutOf;
    }
    public void setScoreOutOf(int scoreOutOf) {
        this.scoreOutOf = scoreOutOf;
    }
}
