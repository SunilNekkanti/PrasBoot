package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Insurance;

@Repository
public interface InsuranceRepository
		extends PagingAndSortingRepository<Insurance, Integer>, JpaSpecificationExecutor<Insurance> {

	public Insurance findById(Integer id);

	public Insurance findByName(String name);
}
