package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.MembershipClaimDetails;
import com.pfchoice.springboot.repositories.MembershipClaimDetailsRepository;
import com.pfchoice.springboot.service.MembershipClaimDetailsService;
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

@Service("membershipClaimDetailsService")
@Transactional
public class MembershipClaimDetailsServiceImpl implements MembershipClaimDetailsService {

	@Autowired
	private MembershipClaimDetailsRepository membershipClaimDetailsRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;

	
	public MembershipClaimDetails findById(Integer id) {
		return membershipClaimDetailsRepository.findOne(id);
	}

	public void saveMembershipClaimDetails(MembershipClaimDetails membershipClaimDetails) {
		membershipClaimDetailsRepository.save(membershipClaimDetails);
	}

	public void updateMembershipClaimDetails(MembershipClaimDetails membershipClaimDetails) {
		saveMembershipClaimDetails(membershipClaimDetails);
	}

	public void deleteMembershipClaimDetailsById(Integer id) {
		membershipClaimDetailsRepository.delete(id);
	}

	public void deleteAllMembershipClaimDetailss() {
		membershipClaimDetailsRepository.deleteAll();
	}

	public List<MembershipClaimDetails> findAllMembershipClaimDetailss() {
		return (List<MembershipClaimDetails>) membershipClaimDetailsRepository.findAll();
	}

	public Page<MembershipClaimDetails> findAllMembershipClaimDetailssByPage(Specification<MembershipClaimDetails> spec, Pageable pageable) {
		return membershipClaimDetailsRepository.findAll(spec, pageable);
	}

	public boolean isMembershipClaimDetailsExist(MembershipClaimDetails membershipClaimDetails) {
		return findById(membershipClaimDetails.getId()) != null;
	}

	
	public Integer loadData(final Map<String, Object> parameters) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(membershipClaimDetailsRepository,parameters, configProperties.getQueryTypeInsert() );
	}
}
