package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.LeadMembership;
import com.pfchoice.springboot.model.LeadMembershipFlag;

@Repository
public interface LeadMembershipFlagRepository
		extends PagingAndSortingRepository<LeadMembershipFlag, Integer>, JpaSpecificationExecutor<LeadMembershipFlag> {

	public LeadMembershipFlag findById(Integer id);
	
	public LeadMembershipFlag findByLead(LeadMembership lead);

}
