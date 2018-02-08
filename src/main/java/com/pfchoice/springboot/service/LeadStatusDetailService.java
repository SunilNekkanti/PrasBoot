package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.LeadStatusDetail;

public interface LeadStatusDetailService {

	LeadStatusDetail findById(Short id);

	LeadStatusDetail findByDescription(String description);

	void saveLeadStatusDetail(LeadStatusDetail leadStatusDetail);

	void updateLeadStatusDetail(LeadStatusDetail leadStatusDetail);

	void deleteLeadStatusDetailById(Short id);

	void deleteAllLeadStatusDetails();

	Page<LeadStatusDetail> findAllLeadStatusDetailsByPage(Specification<LeadStatusDetail> spec, Pageable pageable);

	boolean isLeadStatusDetailExist(LeadStatusDetail leadStatusDetail);
}