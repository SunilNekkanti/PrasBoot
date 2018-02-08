package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.MembershipStatus;
import com.pfchoice.springboot.repositories.MembershipStatusRepository;
import com.pfchoice.springboot.service.MembershipStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("membershipStatusService")
@Transactional
public class MembershipStatusServiceImpl implements MembershipStatusService {

	@Autowired
	private MembershipStatusRepository leadStatusRepository;

	public MembershipStatus findById(Byte id) {
		return leadStatusRepository.findById(id);
	}

	public MembershipStatus findByDescription(String description) {
		return leadStatusRepository.findByDescription(description);
	}

	public void saveMembershipStatus(MembershipStatus leadStatus) {
		leadStatusRepository.save(leadStatus);
	}

	public void updateMembershipStatus(MembershipStatus leadStatus) {
		saveMembershipStatus(leadStatus);
	}

	public void deleteMembershipStatusById(Byte id) {
		leadStatusRepository.delete(id);
	}

	public void deleteAllMembershipStatuses() {
		leadStatusRepository.deleteAll();
	}

	public Page<MembershipStatus> findAllMembershipStatusesByPage(Specification<MembershipStatus> spec,
			Pageable pageable) {
		return leadStatusRepository.findAll(spec, pageable);
	}

	public boolean isMembershipStatusExist(MembershipStatus leadStatus) {
		return findByDescription(leadStatus.getDescription()) != null;
	}

}
