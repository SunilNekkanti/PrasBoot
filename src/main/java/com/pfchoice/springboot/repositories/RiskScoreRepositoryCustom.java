package com.pfchoice.springboot.repositories;

import java.util.List;

import com.pfchoice.springboot.model.RiskScore;

public interface RiskScoreRepositoryCustom {
	List<RiskScore> calculateRiskScore(String icdcodes);
}


    