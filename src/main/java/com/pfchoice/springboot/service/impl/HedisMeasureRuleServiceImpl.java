package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.HedisMeasureRule;
import com.pfchoice.springboot.repositories.HedisMeasureRuleRepository;
import com.pfchoice.springboot.service.HedisMeasureRuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("hedisMeasureRuleService")
@Transactional
public class HedisMeasureRuleServiceImpl implements HedisMeasureRuleService {

	@Autowired
	private HedisMeasureRuleRepository hedisMeasureRuleRepository;

	public HedisMeasureRule findById(Integer id) {
		return hedisMeasureRuleRepository.findOne(id);
	}

	public HedisMeasureRule findByDescription(String description) {
		return hedisMeasureRuleRepository.findByDescription(description);
	}

	public void saveHedisMeasureRule(HedisMeasureRule hedisMeasureRule) {
		hedisMeasureRuleRepository.save(hedisMeasureRule);
	}

	public void updateHedisMeasureRule(HedisMeasureRule hedisMeasureRule) {
		saveHedisMeasureRule(hedisMeasureRule);
	}

	public void deleteHedisMeasureRuleById(Integer id) {
		hedisMeasureRuleRepository.delete(id);
	}

	public void deleteAllHedisMeasureRules() {
		hedisMeasureRuleRepository.deleteAll();
	}

	public List<HedisMeasureRule> findAllHedisMeasureRules() {
		return (List<HedisMeasureRule>) hedisMeasureRuleRepository.findAll();
	}

	public Page<HedisMeasureRule> findAllHedisMeasureRulesByPage(Specification<HedisMeasureRule> spec,
			Pageable pageable) {
		return hedisMeasureRuleRepository.findAll(spec, pageable);
	}

	public boolean isHedisMeasureRuleExist(HedisMeasureRule hedisMeasureRule) {
		return findByDescription(hedisMeasureRule.getDescription()) != null;
	}

}
