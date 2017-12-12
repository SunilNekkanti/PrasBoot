package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.RefContractInsurance;

public interface RefContractInsuranceService {

	RefContractInsurance findById(Integer id);

	RefContractInsurance findByName(String name);

	void saveRefContractInsurance(RefContractInsurance refContractInsurance);

	void updateRefContractInsurance(RefContractInsurance refContractInsurance);

	void deleteRefContractInsuranceById(Integer id);

	void deleteAllRefContractInsurances();

	List<RefContractInsurance> findAllRefContractInsurances();

	Page<RefContractInsurance> findAllRefContractInsurancesByPage(Specification<RefContractInsurance> spec,
			Pageable pageable);

	boolean isRefContractInsuranceExist(RefContractInsurance refContractInsurance);
}