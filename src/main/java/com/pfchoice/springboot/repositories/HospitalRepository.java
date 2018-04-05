package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Hospital;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface HospitalRepository
		extends PagingAndSortingRepository<Hospital, Integer>, JpaSpecificationExecutor<Hospital>, RecordDetailsAwareRepository<Hospital, Integer> {

	Hospital findByName(String name);

}
