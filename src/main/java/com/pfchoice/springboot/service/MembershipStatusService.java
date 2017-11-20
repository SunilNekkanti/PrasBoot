package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipStatus;

public interface MembershipStatusService {

	MembershipStatus findById(Byte id);

	MembershipStatus findByDescription(String description);

	void saveMembershipStatus(MembershipStatus membershipStatus);

	void updateMembershipStatus(MembershipStatus membershipStatus);

	void deleteMembershipStatusById(Byte id);

	void deleteAllMembershipStatuses();

	Page<MembershipStatus> findAllMembershipStatusesByPage(Specification<MembershipStatus> spec, Pageable pageable);

	boolean isMembershipStatusExist(MembershipStatus membershipStatus);
}