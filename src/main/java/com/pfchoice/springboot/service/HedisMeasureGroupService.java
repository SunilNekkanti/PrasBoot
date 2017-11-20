package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.HedisMeasureGroup;

public interface HedisMeasureGroupService {

	HedisMeasureGroup findById(Integer id);

	HedisMeasureGroup findByCode(String code);
	
	void saveHedisMeasureGroup(HedisMeasureGroup hedisMeasureGroup);

	void updateHedisMeasureGroup(HedisMeasureGroup hedisMeasureGroup);

	void deleteHedisMeasureGroupById(Integer id);

	void deleteAllHedisMeasureGroups();

	List<HedisMeasureGroup> findAllHedisMeasureGroups();

	Page<HedisMeasureGroup> findAllHedisMeasureGroupsByPage(Specification<HedisMeasureGroup> spec, Pageable pageable);

	boolean isHedisMeasureGroupExist(HedisMeasureGroup hedisMeasureGroup);
}