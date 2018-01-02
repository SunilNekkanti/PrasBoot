package com.pfchoice.springboot.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipClaimDetails;

public interface MembershipClaimDetailsService {

	MembershipClaimDetails findById(Integer id);

	void saveMembershipClaimDetails(MembershipClaimDetails membershipClaimDetails);

	void updateMembershipClaimDetails(MembershipClaimDetails membershipClaimDetails);

	void deleteMembershipClaimDetailsById(Integer id);

	void deleteAllMembershipClaimDetailss();

	List<MembershipClaimDetails> findAllMembershipClaimDetailss();

	Page<MembershipClaimDetails> findAllMembershipClaimDetailssByPage(Specification<MembershipClaimDetails> spec, Pageable pageable);

	boolean isMembershipClaimDetailsExist(MembershipClaimDetails membership);
	
	Integer loadData(Map<String, Object> parameters)  throws IOException, InterruptedException;
}