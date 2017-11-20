package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.HedisMeasureRule;

public interface HedisMeasureRuleService {

	HedisMeasureRule findById(Integer id);

	HedisMeasureRule findByDescription(String description);
	
	void saveHedisMeasureRule(HedisMeasureRule hedisMeasureRule);

	void updateHedisMeasureRule(HedisMeasureRule hedisMeasureRule);

	void deleteHedisMeasureRuleById(Integer id);

	void deleteAllHedisMeasureRules();

	List<HedisMeasureRule> findAllHedisMeasureRules();

	Page<HedisMeasureRule> findAllHedisMeasureRulesByPage(Specification<HedisMeasureRule> spec, Pageable pageable);

	boolean isHedisMeasureRuleExist(HedisMeasureRule hedisMeasureRule);
}