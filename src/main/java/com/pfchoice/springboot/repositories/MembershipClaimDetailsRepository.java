package com.pfchoice.springboot.repositories;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipClaimDetails;

@Repository
public interface MembershipClaimDetailsRepository
		extends PagingAndSortingRepository<MembershipClaimDetails, Integer>, JpaSpecificationExecutor<MembershipClaimDetails> {

	public MembershipClaimDetails findById(Integer id);
	
}
