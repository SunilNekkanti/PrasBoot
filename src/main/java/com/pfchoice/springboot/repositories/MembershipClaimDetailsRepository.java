package com.pfchoice.springboot.repositories;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipClaimDetails;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface MembershipClaimDetailsRepository
		extends PagingAndSortingRepository<MembershipClaimDetails, Integer>, JpaSpecificationExecutor<MembershipClaimDetails>, RecordDetailsAwareRepository<MembershipClaimDetails, Integer> {

	public MembershipClaimDetails findById(Integer id);
	
}
