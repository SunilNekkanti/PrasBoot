package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.PlanType;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface PlanTypeRepository
		extends PagingAndSortingRepository<PlanType, Integer>, JpaSpecificationExecutor<PlanType>, RecordDetailsAwareRepository<PlanType, Integer> {

	public PlanType findById(Integer id);

	public PlanType findByDescription(String code);
}
