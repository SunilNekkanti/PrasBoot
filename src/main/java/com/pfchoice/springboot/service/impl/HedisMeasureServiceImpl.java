package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.HedisMeasure;
import com.pfchoice.springboot.repositories.HedisMeasureRepository;
import com.pfchoice.springboot.service.HedisMeasureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("hedisMeasureService")
@Transactional
public class HedisMeasureServiceImpl implements HedisMeasureService {

	@Autowired
	private HedisMeasureRepository hedisMeasureRepository;

	public HedisMeasure findById(Integer id) {
		return hedisMeasureRepository.findOne(id);
	}

	public HedisMeasure findByCode(String code) {
		return hedisMeasureRepository.findByCode(code);
	}

	public void saveHedisMeasure(HedisMeasure hedisMeasure) {
		hedisMeasureRepository.save(hedisMeasure);
	}

	public void updateHedisMeasure(HedisMeasure hedisMeasure) {
		saveHedisMeasure(hedisMeasure);
	}

	public void deleteHedisMeasureById(Integer id) {
		hedisMeasureRepository.delete(id);
	}

	public void deleteAllHedisMeasures() {
		hedisMeasureRepository.deleteAll();
	}

	public List<HedisMeasure> findAllHedisMeasures() {
		return (List<HedisMeasure>) hedisMeasureRepository.findAll();
	}

	public Page<HedisMeasure> findAllHedisMeasuresByPage(Specification<HedisMeasure> spec, Pageable pageable) {
		return hedisMeasureRepository.findAll(spec, pageable);
	}

	public boolean isHedisMeasureExist(HedisMeasure hedisMeasure) {
		return findById(hedisMeasure.getId()) != null;
	}

}
