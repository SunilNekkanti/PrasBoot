package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipProblem;

@Repository
public interface MembershipProblemRepository
		extends PagingAndSortingRepository<MembershipProblem, Integer>, JpaSpecificationExecutor<MembershipProblem> {

	public MembershipProblem findById(Integer id);
}
