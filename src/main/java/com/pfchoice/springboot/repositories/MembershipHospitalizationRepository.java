package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipHospitalization;
import com.pfchoice.springboot.repositories.intf.FollowupTypeAwareRepository;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface MembershipHospitalizationRepository
		extends PagingAndSortingRepository<MembershipHospitalization, Integer>, JpaSpecificationExecutor<MembershipHospitalization>
    , RecordDetailsAwareRepository<MembershipHospitalization, Integer>
    , FollowupTypeAwareRepository<MembershipHospitalization, Integer> {

	public MembershipHospitalization findById(Integer id);
	
}
