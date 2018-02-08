package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.EventType;

public interface EventTypeService {

	EventType findById(Integer id);

	EventType findByDescription(String code);

	void saveEventType(EventType eventType);

	void updateEventType(EventType eventType);

	void deleteEventTypeById(Integer id);

	void deleteAllEventTypes();

	Page<EventType> findAllEventTypesByPage(Specification<EventType> spec, Pageable pageable);

	boolean isEventTypeExist(EventType eventType);
}