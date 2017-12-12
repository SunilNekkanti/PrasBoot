package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.ICDMeasure;

public interface ICDMeasureService {

	ICDMeasure findById(Integer id);

	ICDMeasure findByCode(String code);

	void saveICDMeasure(ICDMeasure icdMeasure);

	void updateICDMeasure(ICDMeasure icdMeasure);

	void deleteICDMeasureById(Integer id);

	void deleteAllICDMeasures();

	List<ICDMeasure> findAllICDMeasures();

	Page<ICDMeasure> findAllICDMeasuresByPage(Specification<ICDMeasure> spec, Pageable pageable);

	boolean isICDMeasureExist(ICDMeasure icdMeasure);
}