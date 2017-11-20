package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.FacilityType;
import com.pfchoice.springboot.repositories.FacilityTypeRepository;
import com.pfchoice.springboot.service.FacilityTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("facilityTypeService")
@Transactional
public class FacilityTypeServiceImpl implements FacilityTypeService {

	@Autowired
	private FacilityTypeRepository facilityTypeRepository;

	public FacilityType findById(Integer id) {
		return facilityTypeRepository.findOne(id);
	}

	public FacilityType findByDescription(String name) {
		return facilityTypeRepository.findByDescription(name);
	}

	public void saveFacilityType(FacilityType facilityType) {
		facilityTypeRepository.save(facilityType);
	}

	public void updateFacilityType(FacilityType facilityType) {
		saveFacilityType(facilityType);
	}

	public void deleteFacilityTypeById(Integer id) {
		facilityTypeRepository.delete(id);
	}

	public void deleteAllFacilityTypes() {
		facilityTypeRepository.deleteAll();
	}

	public Page<FacilityType> findAllFacilityTypesByPage(Specification<FacilityType> spec, Pageable pageable) {
		return facilityTypeRepository.findAll(spec, pageable);
	}

	public boolean isFacilityTypeExist(FacilityType facilityType) {
		return findByDescription(facilityType.getDescription()) != null;
	}

}
