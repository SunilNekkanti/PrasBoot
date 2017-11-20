package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.FrequencyType;

public interface FrequencyTypeService {

	FrequencyType findById(Integer id);

	FrequencyType findByDescription(String description);

	void saveFrequencyType(FrequencyType frequencyType);

	void updateFrequencyType(FrequencyType frequencyType);

	void deleteFrequencyTypeById(Integer id);

	void deleteAllFrequencyTypes();

	Page<FrequencyType> findAllFrequencyTypesByPage(Specification<FrequencyType> spec, Pageable pageable);

	boolean isFrequencyTypeExist(FrequencyType frequencyType);

}