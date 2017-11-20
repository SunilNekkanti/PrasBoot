package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.AttPhysician;
import com.pfchoice.springboot.repositories.AttPhysicianRepository;
import com.pfchoice.springboot.service.AttPhysicianService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("attPhysicianService")
@Transactional
public class AttPhysicianServiceImpl implements AttPhysicianService {

	@Autowired
	private AttPhysicianRepository attPhysicianRepository;

	public AttPhysician findById(Integer id) {
		return attPhysicianRepository.findOne(id);
	}

	public AttPhysician findByName(String name) {
		return attPhysicianRepository.findByName(name);
	}

	public void saveAttPhysician(AttPhysician attPhysician) {
		attPhysicianRepository.save(attPhysician);
	}

	public void updateAttPhysician(AttPhysician attPhysician) {
		saveAttPhysician(attPhysician);
	}

	public void deleteAttPhysicianById(Integer id) {
		attPhysicianRepository.delete(id);
	}

	public void deleteAllAttPhysicians() {
		attPhysicianRepository.deleteAll();
	}

	public Page<AttPhysician> findAllAttPhysiciansByPage(Specification<AttPhysician> spec, Pageable pageable) {
		return attPhysicianRepository.findAll(spec, pageable);
	}

	public boolean isAttPhysicianExist(AttPhysician attPhysician) {
		return findByName(attPhysician.getName()) != null;
	}

}
