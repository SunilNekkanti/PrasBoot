package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.RefContractInsurance;
import com.pfchoice.springboot.repositories.intf.InsuranceAwareRepository;

@Repository
public interface RefContractInsuranceRepository extends PagingAndSortingRepository<RefContractInsurance, Integer>,
		JpaSpecificationExecutor<RefContractInsurance> , InsuranceAwareRepository<RefContractInsurance, Integer>{

	public RefContractInsurance findById(Integer id);

	public RefContractInsurance findByName(String name);
}
