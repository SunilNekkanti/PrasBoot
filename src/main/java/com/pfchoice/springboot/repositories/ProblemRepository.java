package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Problem;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface ProblemRepository
		extends PagingAndSortingRepository<Problem, Integer>, JpaSpecificationExecutor<Problem> , RecordDetailsAwareRepository<Problem, Integer>{

	public Problem findById(Integer id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	Problem findByDescription(String description);

}
