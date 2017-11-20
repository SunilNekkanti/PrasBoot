package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipHedisMeasure;

public interface MembershipHedisMeasureService {

	MembershipHedisMeasure findById(Integer id);

	void saveMembershipHedisMeasure(MembershipHedisMeasure membershipHedisMeasure);

	void updateMembershipHedisMeasure(MembershipHedisMeasure membershipHedisMeasure);

	void deleteMembershipHedisMeasureById(Integer id);

	void deleteAllMembershipHedisMeasures();

	List<MembershipHedisMeasure> findAllMembershipHedisMeasures();

	Page<MembershipHedisMeasure> findAllMembershipHedisMeasuresByPage(Specification<MembershipHedisMeasure> spec, Pageable pageable);

	boolean isMembershipHedisMeasureExist(MembershipHedisMeasure membershipHedisMeasure);
	
	int  unloadCSV2Table();
	
	boolean isDataExistsInTable(String tableName);
	
}