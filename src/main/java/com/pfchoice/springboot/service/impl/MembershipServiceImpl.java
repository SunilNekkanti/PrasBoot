package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.Membership;
import com.pfchoice.springboot.repositories.MembershipRepository;
import com.pfchoice.springboot.service.MembershipService;
import com.pfchoice.springboot.util.PrasUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("membershipService")
@Transactional
public class MembershipServiceImpl implements MembershipService {

	@Autowired
	private MembershipRepository membershipRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;
	
	public Membership findById(Integer id) {
		return membershipRepository.findOne(id);
	}

	public Membership findByFirstName(String name) {
		return membershipRepository.findByFirstName(name);
	}

	public Membership findByLastName(String name) {
		return membershipRepository.findByLastName(name);
	}

	public Membership findByDob(Date dob) {
		return membershipRepository.findByDob(dob);
	}

	public void saveMembership(Membership membership) {
		membershipRepository.save(membership);
	}

	public void updateMembership(Membership membership) {
		saveMembership(membership);
	}

	public void deleteMembershipById(Integer id) {
		membershipRepository.delete(id);
	}

	public void deleteAllMemberships() {
		membershipRepository.deleteAll();
	}

	public List<Membership> findAllMemberships() {
		return (List<Membership>) membershipRepository.findAll();
	}

	public Page<Membership> findAllMembershipsByPage(Specification<Membership> spec, Pageable pageable) {
		return membershipRepository.findAll(spec, pageable);
	}

	public boolean isMembershipExist(Membership membership) {
		return findByFirstName(membership.getFirstName()) != null;
	}

	public boolean isDataExistsInTable(String tableName) {
		return (membershipRepository.isDataExistsInTable() == 0) ? false : true;
	}

	public int unloadCSV2Table() {
		return membershipRepository.unloadCSV2Table();
	}
	
	public Integer loadData(final Map<String, Object> parameters) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(membershipRepository,parameters, configProperties.getQueryTypeInsert() );
	}
	
	public Integer loadData(final String entityClassName, final Map<String, Object> parameters) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(entityClassName, parameters, configProperties.getQueryTypeInsert() );
	}

}
