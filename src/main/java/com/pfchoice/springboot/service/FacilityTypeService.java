package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.FacilityType;

public interface FacilityTypeService {

	FacilityType findById(Integer id);

	FacilityType findByDescription(String code);

	void saveFacilityType(FacilityType facilityType);

	void updateFacilityType(FacilityType facilityType);

	void deleteFacilityTypeById(Integer id);

	void deleteAllFacilityTypes();

	Page<FacilityType> findAllFacilityTypesByPage(Specification<FacilityType> spec, Pageable pageable);

	boolean isFacilityTypeExist(FacilityType facilityType);
}