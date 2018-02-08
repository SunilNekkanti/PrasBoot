package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.LeadStatus;

public interface LeadStatusService {

	LeadStatus findById(Short id);

	LeadStatus findByDescription(String description);

	void saveLeadStatus(LeadStatus leadStatus);

	void updateLeadStatus(LeadStatus leadStatus);

	void deleteLeadStatusById(Short id);

	void deleteAllLeadStatuses();

	Page<LeadStatus> findAllLeadStatusesByPage(Specification<LeadStatus> spec, Pageable pageable);

	boolean isLeadStatusExist(LeadStatus leadStatus);
}