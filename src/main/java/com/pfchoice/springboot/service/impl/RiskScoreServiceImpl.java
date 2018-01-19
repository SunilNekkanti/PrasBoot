package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.RiskScore;
import com.pfchoice.springboot.repositories.RiskScoreRepository;
import com.pfchoice.springboot.service.RiskScoreService;
import com.pfchoice.springboot.util.PrasUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("riskScoreService")
@Transactional
public class RiskScoreServiceImpl implements RiskScoreService {

	@Autowired
	private RiskScoreRepository riskScoreRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;

	public RiskScore findById(Integer id) {
		return riskScoreRepository.findOne(id);
	}


	public void saveRiskScore(RiskScore riskScore) {
		riskScoreRepository.save(riskScore);
	}

	public void updateRiskScore(RiskScore riskScore) {
		saveRiskScore(riskScore);
	}

	public void deleteRiskScoreById(Integer id) {
		riskScoreRepository.delete(id);
	}

	public void deleteAllRiskScores() {
		riskScoreRepository.deleteAll();
	}

	public Page<RiskScore> findAllRiskScoresByPage(Specification<RiskScore> spec, Pageable pageable) {
		return riskScoreRepository.findAll(spec, pageable);
	}

	public List<String> findAllPaymentYears(){
		return riskScoreRepository.findAllPaymentYears();
	}
	
	public List<RiskScore> calculateRiskScore(String icdcodes){
		return   riskScoreRepository.calculateRiskScore( icdcodes);
	}
}
