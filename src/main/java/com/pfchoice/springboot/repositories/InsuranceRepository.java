package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Insurance;
import com.pfchoice.springboot.repositories.intf.InsuranceAwareRepository;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface InsuranceRepository
		extends PagingAndSortingRepository<Insurance, Integer>, JpaSpecificationExecutor<Insurance>, RecordDetailsAwareRepository<Insurance, Integer>
, InsuranceAwareRepository<Insurance, Integer>{

	public Insurance findById(Integer id);

	public Insurance findByName(String name);
}
