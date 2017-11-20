package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.AttPhysician;

public interface AttPhysicianService {

	AttPhysician findById(Integer id);

	AttPhysician findByName(String name);

	void saveAttPhysician(AttPhysician attPhysician);

	void updateAttPhysician(AttPhysician attPhysician);

	void deleteAttPhysicianById(Integer id);

	void deleteAllAttPhysicians();

	Page<AttPhysician> findAllAttPhysiciansByPage(Specification<AttPhysician> spec, Pageable pageable);

	boolean isAttPhysicianExist(AttPhysician frequencyType);

}