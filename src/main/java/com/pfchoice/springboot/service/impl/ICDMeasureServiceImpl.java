package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.ICDMeasure;
import com.pfchoice.springboot.repositories.ICDMeasureRepository;
import com.pfchoice.springboot.service.ICDMeasureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("icdMeasureService")
@Transactional
public class ICDMeasureServiceImpl implements ICDMeasureService {

	@Autowired
	private ICDMeasureRepository icdMeasureRepository;

	public ICDMeasure findById(Integer id) {
		return icdMeasureRepository.findOne(id);
	}

	public ICDMeasure findByCode(String code) {
		return icdMeasureRepository.findByCode(code);
	}

	public void saveICDMeasure(ICDMeasure icdMeasure) {
		icdMeasureRepository.save(icdMeasure);
	}

	public void updateICDMeasure(ICDMeasure icdMeasure) {
		saveICDMeasure(icdMeasure);
	}

	public void deleteICDMeasureById(Integer id) {
		icdMeasureRepository.delete(id);
	}

	public void deleteAllICDMeasures() {
		icdMeasureRepository.deleteAll();
	}

	public List<ICDMeasure> findAllICDMeasures() {
		return (List<ICDMeasure>) icdMeasureRepository.findAll();
	}

	public Page<ICDMeasure> findAllICDMeasuresByPage(Specification<ICDMeasure> spec, Pageable pageable) {
		return icdMeasureRepository.findAll(spec, pageable);
	}

	public boolean isICDMeasureExist(ICDMeasure icdMeasure) {
		return findById(icdMeasure.getId()) != null;
	}

}
