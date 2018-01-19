package com.pfchoice.springboot.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import com.pfchoice.springboot.model.RiskScore;

public class RiskScoreRepositoryImpl implements RiskScoreRepositoryCustom {
	
	@PersistenceContext
    private EntityManager em;


    @SuppressWarnings("unchecked")
	@Override
    public List<RiskScore> calculateRiskScore(String icdcodes) {
        StoredProcedureQuery riskscoreCalculator =
              em.createNamedStoredProcedureQuery("calculateRiskScore");
        riskscoreCalculator.setParameter("icdcodes", icdcodes);
        return riskscoreCalculator.getResultList();
    }
}
