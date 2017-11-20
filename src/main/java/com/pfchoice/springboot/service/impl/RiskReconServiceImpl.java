package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.RiskRecon;
import com.pfchoice.springboot.repositories.RiskReconRepository;
import com.pfchoice.springboot.service.RiskReconService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("riskReconService")
@Transactional
public class RiskReconServiceImpl implements RiskReconService {

	@Autowired
	private RiskReconRepository riskReconRepository;

	public RiskRecon findById(Integer id) {
		return riskReconRepository.findOne(id);
	}

	public RiskRecon findByName(String name) {
		return riskReconRepository.findByName(name);
	}

	public void saveRiskRecon(RiskRecon riskRecon) {
		riskReconRepository.save(riskRecon);
	}

	public void updateRiskRecon(RiskRecon riskRecon) {
		saveRiskRecon(riskRecon);
	}

	public void deleteRiskReconById(Integer id) {
		riskReconRepository.delete(id);
	}

	public void deleteAllRiskRecons() {
		riskReconRepository.deleteAll();
	}

	public List<RiskRecon> findAllRiskRecons() {
		return riskReconRepository.findAll();
	}

	public boolean isRiskReconExist(RiskRecon riskRecon) {
		return findById(riskRecon.getId()) != null;
	}

}
