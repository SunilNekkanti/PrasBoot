package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.RiskRecon;

public interface RiskReconService {

	RiskRecon findById(Integer id);

	RiskRecon findByName(String name);

	void saveRiskRecon(RiskRecon riskRecon);

	void updateRiskRecon(RiskRecon riskRecon);

	void deleteRiskReconById(Integer id);

	void deleteAllRiskRecons();

	List<RiskRecon> findAllRiskRecons();

	boolean isRiskReconExist(RiskRecon riskRecon);
}