package com.pfchoice.springboot.service;

import com.pfchoice.springboot.model.LeadMembership;
import com.pfchoice.springboot.model.LeadMembershipFlag;

public interface LeadMembershipFlagService {

	LeadMembershipFlag findById(Integer id);
	
	LeadMembershipFlag findByLead(LeadMembership lead);

	void saveLeadMembershipFlag(LeadMembershipFlag leadMembershipFlag);

	void updateLeadMembershipFlag(LeadMembershipFlag leadMembershipFlag);

	void deleteLeadMembershipFlagById(Integer id);

	boolean isLeadMembershipFlagExist(LeadMembershipFlag leadMembershipFlag);
	
}