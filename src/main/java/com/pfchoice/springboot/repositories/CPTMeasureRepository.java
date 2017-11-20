package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.CPTMeasure;

@Repository
public interface CPTMeasureRepository
		extends PagingAndSortingRepository<CPTMeasure, Integer>, JpaSpecificationExecutor<CPTMeasure> {

	public CPTMeasure findById(Integer id);

	public CPTMeasure findByCode(String code);
	
	public CPTMeasure findByDescription(String description);
}
