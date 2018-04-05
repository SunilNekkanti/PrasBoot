package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipHedisMeasure;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface MembershipHedisMeasureRepository extends PagingAndSortingRepository<MembershipHedisMeasure, Integer>,
		JpaSpecificationExecutor<MembershipHedisMeasure>, RecordDetailsAwareRepository<MembershipHedisMeasure, Integer> {

	public MembershipHedisMeasure findById(Integer id);

	default int isDataExistsInTable() {
		return 1;
	}

	default int unloadCSV2Table() {
		return 1;
	}
}
