package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.PlaceOfService;
import com.pfchoice.springboot.repositories.PlaceOfServiceRepository;
import com.pfchoice.springboot.service.PlaceOfServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("placeOfServiceService")
@Transactional
public class PlaceOfServiceServiceImpl implements PlaceOfServiceService {

	@Autowired
	private PlaceOfServiceRepository placeOfServiceRepository;

	public PlaceOfService findById(Integer id) {
		return placeOfServiceRepository.findOne(id);
	}

	public PlaceOfService findByDescription(String description) {
		return placeOfServiceRepository.findByDescription(description);
	}

	public PlaceOfService findByCode(String code) {
		return placeOfServiceRepository.findByCode(code);
	}
	
	public void savePlaceOfService(PlaceOfService placeOfService) {
		placeOfServiceRepository.save(placeOfService);
	}

	public void updatePlaceOfService(PlaceOfService placeOfService) {
		savePlaceOfService(placeOfService);
	}

	public void deletePlaceOfServiceById(Integer id) {
		placeOfServiceRepository.delete(id);
	}

	public void deleteAllPlaceOfServices() {
		placeOfServiceRepository.deleteAll();
	}

	public Page<PlaceOfService> findAllPlaceOfServicesByPage(Specification<PlaceOfService> spec, Pageable pageable) {
		return placeOfServiceRepository.findAll(spec, pageable);
	}

	public boolean isPlaceOfServiceExist(PlaceOfService placeOfService) {
		return findByCode(placeOfService.getCode()) != null;
	}

}
