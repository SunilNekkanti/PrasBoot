package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Hospital;

public interface HospitalService {

	Hospital findById(Integer id);

	Hospital findByName(String name);

	void saveHospital(Hospital hospital);

	void updateHospital(Hospital hospital);

	void deleteHospitalById(Integer id);

	void deleteAllHospitals();

	Page<Hospital> findAllHospitalsByPage(Specification<Hospital> spec, Pageable pageable);

	boolean isHospitalExist(Hospital frequencyType);

}