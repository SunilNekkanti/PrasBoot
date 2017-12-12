package com.pfchoice.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipClaim;

@Repository
public interface MembershipClaimRepository
		extends PagingAndSortingRepository<MembershipClaim, Integer>, JpaSpecificationExecutor<MembershipClaim> {

	public MembershipClaim findById(Integer id);

	@Query(value = "select distinct cast(report_month as char)  from membership_claims order by report_month desc", nativeQuery = true)
	public List<String> findAllMembershipClaimReportMonths();

	@Query(value = "select distinct  risk_recon_cos_des  from membership_claim_details order by risk_recon_cos_des ", nativeQuery = true)
	public List<String> findAllMembershipClaimRiskCategories();
}
