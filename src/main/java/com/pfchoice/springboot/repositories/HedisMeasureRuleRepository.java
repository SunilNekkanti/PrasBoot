package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.HedisMeasureRule;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface HedisMeasureRuleRepository
		extends PagingAndSortingRepository<HedisMeasureRule, Integer>, JpaSpecificationExecutor<HedisMeasureRule>, RecordDetailsAwareRepository<HedisMeasureRule, Integer> {

	public HedisMeasureRule findById(Integer id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	HedisMeasureRule findByDescription(String description);

}
