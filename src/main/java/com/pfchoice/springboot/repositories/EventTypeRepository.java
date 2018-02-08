package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.EventType;

@Repository
public interface EventTypeRepository
		extends PagingAndSortingRepository<EventType, Integer>, JpaSpecificationExecutor<EventType> {

	public EventType findById(Integer id);

	public EventType findByDescription(String code);
}
