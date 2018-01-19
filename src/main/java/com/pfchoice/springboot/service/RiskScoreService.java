package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.RiskScore;

public interface RiskScoreService {


	void saveRiskScore(RiskScore RiskScore);

	void updateRiskScore(RiskScore RiskScore);

	void deleteRiskScoreById(Integer id);

	void deleteAllRiskScores();

	Page<RiskScore> findAllRiskScoresByPage(Specification<RiskScore> spec, Pageable pageable);
	
	List<String> findAllPaymentYears();
	
	List<RiskScore> calculateRiskScore(String icdcodes);
}