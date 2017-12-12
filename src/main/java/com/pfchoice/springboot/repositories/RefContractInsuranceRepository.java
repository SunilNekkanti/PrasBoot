package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.RefContractInsurance;

@Repository
public interface RefContractInsuranceRepository extends PagingAndSortingRepository<RefContractInsurance, Integer>,
		JpaSpecificationExecutor<RefContractInsurance> {

	public RefContractInsurance findById(Integer id);

	public RefContractInsurance findByName(String name);
}
