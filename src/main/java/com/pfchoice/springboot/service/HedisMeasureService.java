package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.HedisMeasure;

public interface HedisMeasureService {

	HedisMeasure findById(Integer id);

	HedisMeasure findByCode(String code);
	
	void saveHedisMeasure(HedisMeasure hedisMeasure);

	void updateHedisMeasure(HedisMeasure hedisMeasure);

	void deleteHedisMeasureById(Integer id);

	void deleteAllHedisMeasures();

	List<HedisMeasure> findAllHedisMeasures();

	Page<HedisMeasure> findAllHedisMeasuresByPage(Specification<HedisMeasure> spec, Pageable pageable);

	boolean isHedisMeasureExist(HedisMeasure hedisMeasure);
}