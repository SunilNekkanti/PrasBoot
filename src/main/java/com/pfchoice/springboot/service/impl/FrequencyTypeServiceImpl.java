package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.FrequencyType;
import com.pfchoice.springboot.repositories.FrequencyTypeRepository;
import com.pfchoice.springboot.service.FrequencyTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("frequencyTypeService")
@Transactional
public class FrequencyTypeServiceImpl implements FrequencyTypeService {

	@Autowired
	private FrequencyTypeRepository frequencyTypeRepository;

	public FrequencyType findById(Integer id) {
		return frequencyTypeRepository.findOne(id);
	}

	public FrequencyType findByDescription(String description) {
		return frequencyTypeRepository.findByDescription(description);
	}

	public void saveFrequencyType(FrequencyType frequencyType) {
		frequencyTypeRepository.save(frequencyType);
	}

	public void updateFrequencyType(FrequencyType frequencyType) {
		saveFrequencyType(frequencyType);
	}

	public void deleteFrequencyTypeById(Integer id) {
		frequencyTypeRepository.delete(id);
	}

	public void deleteAllFrequencyTypes() {
		frequencyTypeRepository.deleteAll();
	}

	public Page<FrequencyType> findAllFrequencyTypesByPage(Specification<FrequencyType> spec, Pageable pageable) {
		return frequencyTypeRepository.findAll(spec, pageable);
	}

	public boolean isFrequencyTypeExist(FrequencyType frequencyType) {
		return findByDescription(frequencyType.getDescription()) != null;
	}

}
