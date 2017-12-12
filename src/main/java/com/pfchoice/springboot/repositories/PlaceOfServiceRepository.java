package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.PlaceOfService;

@Repository
public interface PlaceOfServiceRepository
		extends PagingAndSortingRepository<PlaceOfService, Integer>, JpaSpecificationExecutor<PlaceOfService> {

	PlaceOfService findByDescription(String description);

	PlaceOfService findByCode(String code);
}
