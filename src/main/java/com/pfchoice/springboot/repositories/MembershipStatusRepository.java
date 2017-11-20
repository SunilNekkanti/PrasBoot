package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipStatus;

@Repository
public interface MembershipStatusRepository
		extends PagingAndSortingRepository<MembershipStatus, Byte>, JpaSpecificationExecutor<MembershipStatus> {

	public MembershipStatus findById(Byte id);

	public MembershipStatus findByDescription(String description);

}
