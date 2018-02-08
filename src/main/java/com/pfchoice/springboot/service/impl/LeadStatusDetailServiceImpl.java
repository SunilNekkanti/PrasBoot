package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.LeadStatusDetail;
import com.pfchoice.springboot.repositories.LeadStatusDetailRepository;
import com.pfchoice.springboot.service.LeadStatusDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("leadStatusDetailService")
@Transactional
public class LeadStatusDetailServiceImpl implements LeadStatusDetailService {

	@Autowired
	private LeadStatusDetailRepository leadStatusDetailRepository;

	public LeadStatusDetail findById(Short id) {
		return leadStatusDetailRepository.findById(id);
	}

	public LeadStatusDetail findByDescription(String description) {
		return leadStatusDetailRepository.findByDescription(description);
	}

	public void saveLeadStatusDetail(LeadStatusDetail leadStatusDetail) {
		leadStatusDetailRepository.save(leadStatusDetail);
	}

	public void updateLeadStatusDetail(LeadStatusDetail leadStatusDetail) {
		saveLeadStatusDetail(leadStatusDetail);
	}

	public void deleteLeadStatusDetailById(Short id) {
		leadStatusDetailRepository.delete(id);
	}

	public void deleteAllLeadStatusDetails() {
		leadStatusDetailRepository.deleteAll();
	}

	public Page<LeadStatusDetail> findAllLeadStatusDetailsByPage(Specification<LeadStatusDetail> spec, Pageable pageable) {
		return leadStatusDetailRepository.findAll(spec, pageable);
	}

	public boolean isLeadStatusDetailExist(LeadStatusDetail leadStatusDetail) {
		return findByDescription(leadStatusDetail.getDescription()) != null;
	}

}
