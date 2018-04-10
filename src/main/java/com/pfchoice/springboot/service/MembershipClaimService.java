package com.pfchoice.springboot.service;

import java.io.IOException;

import java.util.List;
import java.util.Map;

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
	
	Integer loadData(Map<String, Object> parameters, String insuranceCode)  throws IOException, InterruptedException;
	
	List<Object[]>  executeStoredProcedure(String spName, Map<String, Object> params);
}