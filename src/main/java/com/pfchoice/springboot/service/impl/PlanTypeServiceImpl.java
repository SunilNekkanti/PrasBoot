package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.PlanType;
import com.pfchoice.springboot.repositories.PlanTypeRepository;
import com.pfchoice.springboot.service.PlanTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("planTypeService")
@Transactional
public class PlanTypeServiceImpl implements PlanTypeService {

	@Autowired
	private PlanTypeRepository planTypeRepository;

	public PlanType findById(Integer id) {
		return planTypeRepository.findOne(id);
	}

	public PlanType findByDescription(String name) {
		return planTypeRepository.findByDescription(name);
	}

	public void savePlanType(PlanType planType) {
		planTypeRepository.save(planType);
	}

	public void updatePlanType(PlanType planType) {
		savePlanType(planType);
	}

	public void deletePlanTypeById(Integer id) {
		planTypeRepository.delete(id);
	}

	public void deleteAllPlanTypes() {
		planTypeRepository.deleteAll();
	}

	public List<PlanType> findAllPlanTypes() {
		return (List<PlanType>) planTypeRepository.findAll();
	}

	public Page<PlanType> findAllPlanTypesByPage(Specification<PlanType> spec, Pageable pageable) {
		return planTypeRepository.findAll(spec, pageable);
	}

	public boolean isPlanTypeExist(PlanType planType) {
		return findByDescription(planType.getDescription()) != null;
	}

}
