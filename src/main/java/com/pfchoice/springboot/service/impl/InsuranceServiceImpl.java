package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.Insurance;
import com.pfchoice.springboot.repositories.InsuranceRepository;
import com.pfchoice.springboot.service.InsuranceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("insuranceService")
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

	@Autowired
	private InsuranceRepository insuranceRepository;

	public Insurance findById(Integer id) {
		return insuranceRepository.findOne(id);
	}

	public Insurance findByName(String name) {
		return insuranceRepository.findByName(name);
	}

	public void saveInsurance(Insurance insurance) {
		insuranceRepository.save(insurance);
	}

	public void updateInsurance(Insurance insurance) {
		saveInsurance(insurance);
	}

	public void deleteInsuranceById(Integer id) {
		insuranceRepository.delete(id);
	}

	public void deleteAllInsurances() {
		insuranceRepository.deleteAll();
	}

	public List<Insurance> findAllInsurances() {
		return (List<Insurance>) insuranceRepository.findAll();
	}

	public Page<Insurance> findAllInsurancesByPage(Specification<Insurance> spec, Pageable pageable) {
		return insuranceRepository.findAll(spec, pageable);
	}

	public boolean isInsuranceExist(Insurance insurance) {
		return findById(insurance.getId()) != null;
	}

}
