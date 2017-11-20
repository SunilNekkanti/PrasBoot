package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FacilityType;

@Repository
public interface FacilityTypeRepository
		extends PagingAndSortingRepository<FacilityType, Integer>, JpaSpecificationExecutor<FacilityType> {

	public FacilityType findById(Integer id);

	public FacilityType findByDescription(String code);
}
