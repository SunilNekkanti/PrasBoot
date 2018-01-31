package com.pfchoice.springboot.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipHedisProblem;

public interface MembershipHedisProblemService {

	MembershipHedisProblem findById(Integer id);

	void saveMembershipHedisProblem(MembershipHedisProblem membershipClaim);

	void updateMembershipHedisProblem(MembershipHedisProblem membershipClaim);

	void deleteMembershipHedisProblemById(Integer id);

	void deleteAllMembershipHedisProblems();

	List<MembershipHedisProblem> findAllMembershipHedisProblems();

	Page<MembershipHedisProblem> findAllMembershipHedisProblemsByPage(Specification<MembershipHedisProblem> spec, Pageable pageable);

	boolean isMembershipHedisProblemExist(MembershipHedisProblem membership);
	
	Integer loadData(Map<String, Object> parameters, String insuranceCode)  throws IOException, InterruptedException;
}