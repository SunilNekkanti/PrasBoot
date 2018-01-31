package com.pfchoice.springboot.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipProblem;

public interface MembershipProblemService {

	MembershipProblem findById(Integer id);

	void saveMembershipProblem(MembershipProblem membershipProblem);

	void updateMembershipProblem(MembershipProblem membershipClaim);

	void deleteMembershipProblemById(Integer id);

	void deleteAllMembershipProblems();

	List<MembershipProblem> findAllMembershipProblems();

	Page<MembershipProblem> findAllMembershipProblemsByPage(Specification<MembershipProblem> spec, Pageable pageable);

	boolean isMembershipProblemExist(MembershipProblem membershipProblem, Integer mbrId);
	
	Integer loadData(Map<String, Object> parameters, String insuranceCode)  throws IOException, InterruptedException;
}