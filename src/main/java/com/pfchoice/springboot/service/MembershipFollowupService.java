package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.MembershipFollowup;

public interface MembershipFollowupService {

	MembershipFollowup findById(Integer id);

	void saveMembershipFollowup(MembershipFollowup membershipFollowup);

	void updateMembershipFollowup(MembershipFollowup membershipFollowup);

	void deleteMembershipFollowupById(Integer id);

	void deleteAllMembershipFollowups();

	List<MembershipFollowup> findAllMembershipFollowups();

	List<MembershipFollowup> findAllMembershipFollowupsByMbrId(Integer mbrId);

	boolean isMembershipFollowupExist(MembershipFollowup membershipFollowup);
}