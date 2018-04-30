package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.MembershipHospitalization;
import com.pfchoice.springboot.repositories.MembershipHospitalizationRepository;
import com.pfchoice.springboot.service.MembershipHospitalizationService;
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

@Service("membershipHospitalizationService")
@Transactional
public class MembershipHospitalizationServiceImpl implements MembershipHospitalizationService {

	@Autowired
	private MembershipHospitalizationRepository membershipHospitalizationRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	PrasUtil prasUtil;

	
	public MembershipHospitalization findById(Integer id) {
		return membershipHospitalizationRepository.findOne(id);
	}

	public void saveMembershipHospitalization(MembershipHospitalization membershipHospitalization) {
		membershipHospitalizationRepository.save(membershipHospitalization);
	}

	public void updateMembershipHospitalization(MembershipHospitalization membershipHospitalization) {
		saveMembershipHospitalization(membershipHospitalization);
	}

	public void deleteMembershipHospitalizationById(Integer id) {
		membershipHospitalizationRepository.delete(id);
	}

	public void deleteAllMembershipHospitalizations() {
		membershipHospitalizationRepository.deleteAll();
	}

	public List<MembershipHospitalization> findAllMembershipHospitalizations() {
		return (List<MembershipHospitalization>) membershipHospitalizationRepository.findAll();
	}

	public Page<MembershipHospitalization> findAllMembershipHospitalizationsByPage(Specification<MembershipHospitalization> spec, Pageable pageable) {
		return membershipHospitalizationRepository.findAll(spec, pageable);
	}

	public Integer loadData(final Map<String, Object> parameters, String insuranceCode) throws IOException, InterruptedException{
		return prasUtil.executeSQLQuery(membershipHospitalizationRepository,insuranceCode,parameters, configProperties.getQueryTypeInsert() );
	}
}
