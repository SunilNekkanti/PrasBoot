package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.AttPhysician;

@Repository
public interface AttPhysicianRepository extends PagingAndSortingRepository<AttPhysician, Integer>, JpaSpecificationExecutor<AttPhysician> {

	AttPhysician findByName(String name);

}
