package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FacilityType;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface FacilityTypeRepository
		extends PagingAndSortingRepository<FacilityType, Integer>, JpaSpecificationExecutor<FacilityType>, RecordDetailsAwareRepository<FacilityType, Integer> {

	public FacilityType findById(Integer id);

	public FacilityType findByDescription(String code);
}
