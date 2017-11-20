package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FrequencyType;

@Repository
public interface FrequencyTypeRepository extends PagingAndSortingRepository<FrequencyType, Integer>, JpaSpecificationExecutor<FrequencyType> {

	FrequencyType findByDescription(String description);

}
