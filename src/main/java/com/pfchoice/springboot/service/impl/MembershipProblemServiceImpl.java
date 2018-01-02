package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.MembershipProblem;
import com.pfchoice.springboot.repositories.MembershipProblemRepository;
import com.pfchoice.springboot.service.MembershipProblemService;
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

@Service("membershipProblemService")
@Transactional
public class MembershipProblemServiceImpl implements MembershipProblemService {

	@Autowired
	private MembershipProblemRepository membershipProblemRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;

	
	public MembershipProblem findById(Integer id) {
		return membershipProblemRepository.findOne(id);
	}

	public void saveMembershipProblem(MembershipProblem membershipProblem) {
		membershipProblemRepository.save(membershipProblem);
	}

	public void updateMembershipProblem(MembershipProblem membershipProblem) {
		saveMembershipProblem(membershipProblem);
	}

	public void deleteMembershipProblemById(Integer id) {
		membershipProblemRepository.delete(id);
	}

	public void deleteAllMembershipProblems() {
		membershipProblemRepository.deleteAll();
	}

	public List<MembershipProblem> findAllMembershipProblems() {
		return (List<MembershipProblem>) membershipProblemRepository.findAll();
	}

	public Page<MembershipProblem> findAllMembershipProblemsByPage(Specification<MembershipProblem> spec, Pageable pageable) {
		return membershipProblemRepository.findAll(spec, pageable);
	}

	public boolean isMembershipProblemExist(MembershipProblem membershipProblem) {
		return findById(membershipProblem.getId()) != null;
	}
	public Integer loadData(final Map<String, Object> parameters) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(membershipProblemRepository,parameters, configProperties.getQueryTypeInsert() );
	}
}
