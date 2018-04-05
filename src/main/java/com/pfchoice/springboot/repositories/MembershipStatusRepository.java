package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipStatus;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface MembershipStatusRepository
		extends PagingAndSortingRepository<MembershipStatus, Byte>, JpaSpecificationExecutor<MembershipStatus>, RecordDetailsAwareRepository<MembershipStatus, Byte>{

	public MembershipStatus findById(Byte id);

	public MembershipStatus findByDescription(String description);

}
