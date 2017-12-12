package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.HedisMeasure;

@Repository
public interface HedisMeasureRepository
		extends PagingAndSortingRepository<HedisMeasure, Integer>, JpaSpecificationExecutor<HedisMeasure> {

	public HedisMeasure findById(Integer id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	HedisMeasure findByCode(String code);

}
