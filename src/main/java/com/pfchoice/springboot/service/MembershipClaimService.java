package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipClaim;

public interface MembershipClaimService {

	MembershipClaim findById(Integer id);

	void saveMembershipClaim(MembershipClaim membershipClaim);

	void updateMembershipClaim(MembershipClaim membershipClaim);

	void deleteMembershipClaimById(Integer id);

	void deleteAllMembershipClaims();

	List<MembershipClaim> findAllMembershipClaims();

	Page<MembershipClaim> findAllMembershipClaimsByPage(Specification<MembershipClaim> spec, Pageable pageable);

	boolean isMembershipClaimExist(MembershipClaim membership);

	List<String> findAllMembershipClaimReportMonths();

	List<String> findAllMembershipClaimRiskCategories();
}