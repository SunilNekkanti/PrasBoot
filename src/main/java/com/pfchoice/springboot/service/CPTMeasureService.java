package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.CPTMeasure;

public interface CPTMeasureService {

	CPTMeasure findById(Integer id);

	CPTMeasure findByCode(String code);

	CPTMeasure findByDescription(String description);

	void saveCPTMeasure(CPTMeasure cptMeasure);

	void updateCPTMeasure(CPTMeasure cptMeasure);

	void deleteCPTMeasureById(Integer id);

	void deleteAllCPTMeasures();

	List<CPTMeasure> findAllCPTMeasures();

	Page<CPTMeasure> findAllCPTMeasuresByPage(Specification<CPTMeasure> spec, Pageable pageable);

	boolean isCPTMeasureExist(CPTMeasure cptMeasure);
}