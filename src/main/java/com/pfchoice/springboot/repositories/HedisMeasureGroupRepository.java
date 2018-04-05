package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.HedisMeasureGroup;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface HedisMeasureGroupRepository
		extends PagingAndSortingRepository<HedisMeasureGroup, Integer>, JpaSpecificationExecutor<HedisMeasureGroup>, RecordDetailsAwareRepository<HedisMeasureGroup, Integer> {

	public HedisMeasureGroup findById(Integer id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	HedisMeasureGroup findByCode(String code);

}
