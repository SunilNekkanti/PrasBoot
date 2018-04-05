package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipHedisProblem;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface MembershipHedisProblemRepository
		extends PagingAndSortingRepository<MembershipHedisProblem, Integer>, JpaSpecificationExecutor<MembershipHedisProblem>, RecordDetailsAwareRepository<MembershipHedisProblem, Integer> {

	public MembershipHedisProblem findById(Integer id);
}
