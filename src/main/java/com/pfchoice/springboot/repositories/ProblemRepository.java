package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Problem;

@Repository
public interface ProblemRepository
		extends PagingAndSortingRepository<Problem, Integer>, JpaSpecificationExecutor<Problem> {

	public Problem findById(Integer id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	Problem findByDescription(String description);

}
