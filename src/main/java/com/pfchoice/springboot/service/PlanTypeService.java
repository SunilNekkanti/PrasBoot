package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.PlanType;

public interface PlanTypeService {

	PlanType findById(Integer id);

	PlanType findByDescription(String code);

	void savePlanType(PlanType planType);

	void updatePlanType(PlanType planType);

	void deletePlanTypeById(Integer id);

	void deleteAllPlanTypes();

	List<PlanType> findAllPlanTypes();
	
	Page<PlanType> findAllPlanTypesByPage(Specification<PlanType> spec, Pageable pageable);

	boolean isPlanTypeExist(PlanType planType);
}