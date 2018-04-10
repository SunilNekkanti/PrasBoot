package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.MembershipClaim;
import com.pfchoice.springboot.repositories.MembershipClaimRepository;
import com.pfchoice.springboot.service.MembershipClaimService;
import com.pfchoice.springboot.util.PrasUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("membershipClaimService")
@Transactional
public class MembershipClaimServiceImpl implements MembershipClaimService {

	@Autowired
	private MembershipClaimRepository membershipClaimRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;

	
	public MembershipClaim findById(Integer id) {
		return membershipClaimRepository.findOne(id);
	}

	public void saveMembershipClaim(MembershipClaim membershipClaim) {
		membershipClaimRepository.save(membershipClaim);
	}

	public void updateMembershipClaim(MembershipClaim membershipClaim) {
		saveMembershipClaim(membershipClaim);
	}

	public void deleteMembershipClaimById(Integer id) {
		membershipClaimRepository.delete(id);
	}

	public void deleteAllMembershipClaims() {
		membershipClaimRepository.deleteAll();
	}

	public List<MembershipClaim> findAllMembershipClaims() {
		return (List<MembershipClaim>) membershipClaimRepository.findAll();
	}

	public Page<MembershipClaim> findAllMembershipClaimsByPage(Specification<MembershipClaim> spec, Pageable pageable) {
		return membershipClaimRepository.findAll(spec, pageable);
	}

	public boolean isMembershipClaimExist(MembershipClaim membershipClaim) {
		return findById(membershipClaim.getId()) != null;
	}

	public List<String> findAllMembershipClaimReportMonths() {
		return membershipClaimRepository.findAllMembershipClaimReportMonths();
	}

	public List<String> findAllMembershipClaimRiskCategories() {
		return membershipClaimRepository.findAllMembershipClaimRiskCategories();
	}
	
	public Integer loadData(final Map<String, Object> parameters, String insuranceCode) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(membershipClaimRepository,insuranceCode, parameters, configProperties.getQueryTypeInsert() );
	}
	
	
	public List<Object[]>  executeStoredProcedure(final String spName, final Map<String, Object> params){
		return prasUtil.executeStoredProcedure(spName,params);
	}
}
