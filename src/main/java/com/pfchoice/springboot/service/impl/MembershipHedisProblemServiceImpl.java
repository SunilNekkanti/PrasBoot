package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.MembershipHedisProblem;
import com.pfchoice.springboot.repositories.MembershipHedisProblemRepository;
import com.pfchoice.springboot.service.MembershipHedisProblemService;
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

@Service("membershipHedisProblemService")
@Transactional
public class MembershipHedisProblemServiceImpl implements MembershipHedisProblemService {

	@Autowired
	private MembershipHedisProblemRepository membershipHedisProblemRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;

	
	public MembershipHedisProblem findById(Integer id) {
		return membershipHedisProblemRepository.findOne(id);
	}

	public void saveMembershipHedisProblem(MembershipHedisProblem membershipHedisProblem) {
		membershipHedisProblemRepository.save(membershipHedisProblem);
	}

	public void updateMembershipHedisProblem(MembershipHedisProblem membershipHedisProblem) {
		saveMembershipHedisProblem(membershipHedisProblem);
	}

	public void deleteMembershipHedisProblemById(Integer id) {
		membershipHedisProblemRepository.delete(id);
	}

	public void deleteAllMembershipHedisProblems() {
		membershipHedisProblemRepository.deleteAll();
	}

	public List<MembershipHedisProblem> findAllMembershipHedisProblems() {
		return (List<MembershipHedisProblem>) membershipHedisProblemRepository.findAll();
	}

	public Page<MembershipHedisProblem> findAllMembershipHedisProblemsByPage(Specification<MembershipHedisProblem> spec, Pageable pageable) {
		return membershipHedisProblemRepository.findAll(spec, pageable);
	}

	public boolean isMembershipHedisProblemExist(MembershipHedisProblem membershipHedisProblem) {
		return findById(membershipHedisProblem.getId()) != null;
	}
	public Integer loadData(final Map<String, Object> parameters, String insuranceCode) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(membershipHedisProblemRepository,insuranceCode,parameters, configProperties.getQueryTypeInsert() );
	}
}
