package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.Hospital;
import com.pfchoice.springboot.repositories.HospitalRepository;
import com.pfchoice.springboot.service.HospitalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("hospitalService")
@Transactional
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	private HospitalRepository hospitalRepository;

	public Hospital findById(Integer id) {
		return hospitalRepository.findOne(id);
	}

	public Hospital findByName(String name) {
		return hospitalRepository.findByName(name);
	}

	public void saveHospital(Hospital hospital) {
		hospitalRepository.save(hospital);
	}

	public void updateHospital(Hospital hospital) {
		saveHospital(hospital);
	}

	public void deleteHospitalById(Integer id) {
		hospitalRepository.delete(id);
	}

	public void deleteAllHospitals() {
		hospitalRepository.deleteAll();
	}

	public Page<Hospital> findAllHospitalsByPage(Specification<Hospital> spec, Pageable pageable) {
		return hospitalRepository.findAll(spec, pageable);
	}

	public boolean isHospitalExist(Hospital hospital) {
		return findByName(hospital.getName()) != null;
	}

}
