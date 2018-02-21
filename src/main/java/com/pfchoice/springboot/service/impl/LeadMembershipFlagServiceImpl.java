package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.LeadMembership;
import com.pfchoice.springboot.model.LeadMembershipFlag;
import com.pfchoice.springboot.repositories.LeadMembershipFlagRepository;
import com.pfchoice.springboot.service.LeadMembershipFlagService;
import com.pfchoice.springboot.util.PrasUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("leadMembershipFlagService")
@Transactional
public class LeadMembershipFlagServiceImpl implements LeadMembershipFlagService {

	@Autowired
	private LeadMembershipFlagRepository leadMembershipFlagRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;

	public LeadMembershipFlag findById(Integer id) {
		return leadMembershipFlagRepository.findOne(id);
	}

	public LeadMembershipFlag findByLead(LeadMembership lead){
		return leadMembershipFlagRepository.findByLead(lead);
	}

	public void saveLeadMembershipFlag(LeadMembershipFlag leadMembershipFlag) {
		leadMembershipFlagRepository.save(leadMembershipFlag);
	}

	public void updateLeadMembershipFlag(LeadMembershipFlag leadMembershipFlag) {
		saveLeadMembershipFlag(leadMembershipFlag);
	}

	public void deleteLeadMembershipFlagById(Integer id) {
		leadMembershipFlagRepository.delete(id);
	}

	public boolean isLeadMembershipFlagExist(LeadMembershipFlag leadMembershipFlag) {
		return findByLead(leadMembershipFlag.getLead()) != null;
	}

}
