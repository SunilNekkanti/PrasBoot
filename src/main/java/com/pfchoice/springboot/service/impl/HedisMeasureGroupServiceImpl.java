package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.HedisMeasureGroup;
import com.pfchoice.springboot.repositories.HedisMeasureGroupRepository;
import com.pfchoice.springboot.service.HedisMeasureGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("hedisMeasureGroupService")
@Transactional
public class HedisMeasureGroupServiceImpl implements HedisMeasureGroupService {

	@Autowired
	private HedisMeasureGroupRepository hedisMeasureGroupRepository;

	public HedisMeasureGroup findById(Integer id) {
		return hedisMeasureGroupRepository.findOne(id);
	}

	public HedisMeasureGroup findByCode(String code) {
		return hedisMeasureGroupRepository.findByCode(code);
	}
	
	public void saveHedisMeasureGroup(HedisMeasureGroup hedisMeasureGroup) {
		hedisMeasureGroupRepository.save(hedisMeasureGroup);
	}

	public void updateHedisMeasureGroup(HedisMeasureGroup hedisMeasureGroup) {
		saveHedisMeasureGroup(hedisMeasureGroup);
	}

	public void deleteHedisMeasureGroupById(Integer id) {
		hedisMeasureGroupRepository.delete(id);
	}

	public void deleteAllHedisMeasureGroups() {
		hedisMeasureGroupRepository.deleteAll();
	}

	public List<HedisMeasureGroup> findAllHedisMeasureGroups() {
		return (List<HedisMeasureGroup>) hedisMeasureGroupRepository.findAll();
	}

	public Page<HedisMeasureGroup> findAllHedisMeasureGroupsByPage(Specification<HedisMeasureGroup> spec, Pageable pageable) {
		return hedisMeasureGroupRepository.findAll(spec, pageable);
	}

	public boolean isHedisMeasureGroupExist(HedisMeasureGroup hedisMeasureGroup) {
		return findById(hedisMeasureGroup.getId()) != null;
	}

}
