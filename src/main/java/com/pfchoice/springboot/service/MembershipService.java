package com.pfchoice.springboot.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Membership;

public interface MembershipService {

	Membership findById(Integer id);

	Membership findByFirstName(String name);

	Membership findByLastName(String name);

	Membership findByDob(Date dob);

	void saveMembership(Membership membership);

	void updateMembership(Membership membership);

	void deleteMembershipById(Integer id);

	void deleteAllMemberships();

	List<Membership> findAllMemberships();

	Page<Membership> findAllMembershipsByPage(Specification<Membership> spec, Pageable pageable);

	boolean isMembershipExist(Membership membership);

	int unloadCSV2Table();

	boolean isDataExistsInTable(String tableName);

}