package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.LeadStatus;

@Repository
public interface LeadStatusRepository
		extends PagingAndSortingRepository<LeadStatus, Short>, JpaSpecificationExecutor<LeadStatus> {

	public LeadStatus findById(Short id);

	public LeadStatus findByDescription(String description);

}
