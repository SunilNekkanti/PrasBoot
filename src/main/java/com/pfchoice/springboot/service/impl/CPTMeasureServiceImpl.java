package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.CPTMeasure;
import com.pfchoice.springboot.repositories.CPTMeasureRepository;
import com.pfchoice.springboot.service.CPTMeasureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("cptMeasureService")
@Transactional
public class CPTMeasureServiceImpl implements CPTMeasureService {

	@Autowired
	private CPTMeasureRepository cptMeasureRepository;

	public CPTMeasure findById(Integer id) {
		return cptMeasureRepository.findOne(id);
	}

	public CPTMeasure findByCode(String code) {
		return cptMeasureRepository.findByCode(code);
	}
	
	public CPTMeasure findByDescription(String description) {
		return cptMeasureRepository.findByDescription(description);
	}
	
	public void saveCPTMeasure(CPTMeasure cptMeasure) {
		cptMeasureRepository.save(cptMeasure);
	}

	public void updateCPTMeasure(CPTMeasure cptMeasure) {
		saveCPTMeasure(cptMeasure);
	}

	public void deleteCPTMeasureById(Integer id) {
		cptMeasureRepository.delete(id);
	}

	public void deleteAllCPTMeasures() {
		cptMeasureRepository.deleteAll();
	}

	public List<CPTMeasure> findAllCPTMeasures() {
		return (List<CPTMeasure>) cptMeasureRepository.findAll();
	}

	public Page<CPTMeasure> findAllCPTMeasuresByPage(Specification<CPTMeasure> spec, Pageable pageable) {
		return cptMeasureRepository.findAll(spec, pageable);
	}

	public boolean isCPTMeasureExist(CPTMeasure cptMeasure) {
		return findById(cptMeasure.getId()) != null;
	}

}
