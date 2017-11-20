package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Insurance;

public interface InsuranceService {

	Insurance findById(Integer id);

	Insurance findByName(String name);

	void saveInsurance(Insurance insurance);

	void updateInsurance(Insurance insurance);

	void deleteInsuranceById(Integer id);

	void deleteAllInsurances();

	List<Insurance> findAllInsurances();

	Page<Insurance> findAllInsurancesByPage(Specification<Insurance> spec, Pageable pageable);

	boolean isInsuranceExist(Insurance insurance);
}