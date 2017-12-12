package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.RefContractInsurance;
import com.pfchoice.springboot.repositories.RefContractInsuranceRepository;
import com.pfchoice.springboot.service.RefContractInsuranceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("refContractInsuranceService")
@Transactional
public class RefContractInsuranceServiceImpl implements RefContractInsuranceService {

	@Autowired
	private RefContractInsuranceRepository refContractInsuranceRepository;

	public RefContractInsurance findById(Integer id) {
		return refContractInsuranceRepository.findOne(id);
	}

	public RefContractInsurance findByName(String name) {
		return refContractInsuranceRepository.findByName(name);
	}

	public void saveRefContractInsurance(RefContractInsurance refContractInsurance) {
		refContractInsuranceRepository.save(refContractInsurance);
	}

	public void updateRefContractInsurance(RefContractInsurance refContractInsurance) {
		saveRefContractInsurance(refContractInsurance);
	}

	public void deleteRefContractInsuranceById(Integer id) {
		refContractInsuranceRepository.delete(id);
	}

	public void deleteAllRefContractInsurances() {
		refContractInsuranceRepository.deleteAll();
	}

	public List<RefContractInsurance> findAllRefContractInsurances() {
		return (List<RefContractInsurance>) refContractInsuranceRepository.findAll();
	}

	public Page<RefContractInsurance> findAllRefContractInsurancesByPage(Specification<RefContractInsurance> spec,
			Pageable pageable) {
		return refContractInsuranceRepository.findAll(spec, pageable);
	}

	public boolean isRefContractInsuranceExist(RefContractInsurance refContractInsurance) {
		return findByName(refContractInsurance.getName()) != null;
	}

}
