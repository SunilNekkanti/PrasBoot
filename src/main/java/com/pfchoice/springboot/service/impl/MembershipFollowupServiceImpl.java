package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.MembershipFollowup;
import com.pfchoice.springboot.repositories.MembershipFollowupRepository;
import com.pfchoice.springboot.service.MembershipFollowupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("membershipFollowupService")
@Transactional
public class MembershipFollowupServiceImpl implements MembershipFollowupService {

	@Autowired
	private MembershipFollowupRepository membershipFollowupRepository;

	public MembershipFollowup findById(Integer id) {
		return membershipFollowupRepository.findOne(id);
	}

	public void saveMembershipFollowup(MembershipFollowup membershipFollowup) {
		membershipFollowupRepository.save(membershipFollowup);
	}

	public void updateMembershipFollowup(MembershipFollowup membershipFollowup) {
		saveMembershipFollowup(membershipFollowup);
	}

	public void deleteMembershipFollowupById(Integer id) {
		membershipFollowupRepository.delete(id);
	}

	public void deleteAllMembershipFollowups() {
		membershipFollowupRepository.deleteAll();
	}

	public List<MembershipFollowup> findAllMembershipFollowups() {
		return membershipFollowupRepository.findAll();
	}

	public List<MembershipFollowup> findAllMembershipFollowupsByMbrId(Integer mbrId) {
		return membershipFollowupRepository.findAllMembershipFollowupsByMbrId(mbrId);
	}

	public boolean isMembershipFollowupExist(MembershipFollowup membershipFollowup) {
		return findById(membershipFollowup.getId()) != null;
	}

}
