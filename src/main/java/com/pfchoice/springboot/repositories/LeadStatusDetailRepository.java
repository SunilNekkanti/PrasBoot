package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.LeadStatusDetail;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface LeadStatusDetailRepository
		extends PagingAndSortingRepository<LeadStatusDetail, Short>, JpaSpecificationExecutor<LeadStatusDetail>, RecordDetailsAwareRepository<LeadStatusDetail, Short> {

	public LeadStatusDetail findById(Short id);

	public LeadStatusDetail findByDescription(String description);

}
