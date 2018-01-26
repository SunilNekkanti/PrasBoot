package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.MembershipHedisMeasure;
import com.pfchoice.springboot.repositories.MembershipHedisMeasureRepository;
import com.pfchoice.springboot.service.MembershipHedisMeasureService;
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

@Service("membershipHedisMeasureService")
@Transactional
public class MembershipHedisMeasureServiceImpl implements MembershipHedisMeasureService {

	@Autowired
	private MembershipHedisMeasureRepository membershipHedisMeasureRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;
	
	public MembershipHedisMeasure findById(Integer id) {
		return membershipHedisMeasureRepository.findOne(id);
	}

	public void saveMembershipHedisMeasure(MembershipHedisMeasure membershipHedisMeasure) {
		membershipHedisMeasureRepository.save(membershipHedisMeasure);
	}

	public void updateMembershipHedisMeasure(MembershipHedisMeasure membershipHedisMeasure) {
		saveMembershipHedisMeasure(membershipHedisMeasure);
	}

	public void deleteMembershipHedisMeasureById(Integer id) {
		membershipHedisMeasureRepository.delete(id);
	}

	public void deleteAllMembershipHedisMeasures() {
		membershipHedisMeasureRepository.deleteAll();
	}

	public List<MembershipHedisMeasure> findAllMembershipHedisMeasures() {
		return (List<MembershipHedisMeasure>) membershipHedisMeasureRepository.findAll();
	}

	public Page<MembershipHedisMeasure> findAllMembershipHedisMeasuresByPage(Specification<MembershipHedisMeasure> spec,
			Pageable pageable) {
		return membershipHedisMeasureRepository.findAll(spec, pageable);
	}

	public boolean isMembershipHedisMeasureExist(MembershipHedisMeasure membershipHedisMeasure) {
		return findById(membershipHedisMeasure.getId()) != null;
	}

	public boolean isDataExistsInTable(String tableName) {
		return (membershipHedisMeasureRepository.isDataExistsInTable() == 0) ? false : true;
	}

	public int unloadCSV2Table() {
		return membershipHedisMeasureRepository.unloadCSV2Table();
	}

	public Integer loadData(final Map<String, Object> parameters, String insuranceCode) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(membershipHedisMeasureRepository,insuranceCode, parameters, configProperties.getQueryTypeInsert() );
	}
}
