package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.HedisMeasureGroup;

@Repository
public interface HedisMeasureGroupRepository
		extends PagingAndSortingRepository<HedisMeasureGroup, Integer>, JpaSpecificationExecutor<HedisMeasureGroup> {

	public HedisMeasureGroup findById(Integer id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	HedisMeasureGroup findByCode(String code);

}
