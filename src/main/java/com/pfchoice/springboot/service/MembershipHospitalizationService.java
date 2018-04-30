package com.pfchoice.springboot.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipHospitalization;

public interface MembershipHospitalizationService {

	MembershipHospitalization findById(Integer id);

	void saveMembershipHospitalization(MembershipHospitalization membershipHospitalization);

	void updateMembershipHospitalization(MembershipHospitalization membershipClaim);

	void deleteMembershipHospitalizationById(Integer id);

	void deleteAllMembershipHospitalizations();

	List<MembershipHospitalization> findAllMembershipHospitalizations();

	Page<MembershipHospitalization> findAllMembershipHospitalizationsByPage(Specification<MembershipHospitalization> spec, Pageable pageable);

	Integer loadData(Map<String, Object> parameters, String insuranceCode)  throws IOException, InterruptedException;
}