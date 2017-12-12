package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.PlaceOfService;

public interface PlaceOfServiceService {

	PlaceOfService findById(Integer id);

	PlaceOfService findByDescription(String description);

	PlaceOfService findByCode(String code);

	void savePlaceOfService(PlaceOfService placeOfService);

	void updatePlaceOfService(PlaceOfService placeOfService);

	void deletePlaceOfServiceById(Integer id);

	void deleteAllPlaceOfServices();

	Page<PlaceOfService> findAllPlaceOfServicesByPage(Specification<PlaceOfService> spec, Pageable pageable);

	boolean isPlaceOfServiceExist(PlaceOfService placeOfService);

}