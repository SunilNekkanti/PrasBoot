package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.EventType;
import com.pfchoice.springboot.repositories.EventTypeRepository;
import com.pfchoice.springboot.service.EventTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eventTypeService")
@Transactional
public class EventTypeServiceImpl implements EventTypeService {

	@Autowired
	private EventTypeRepository eventTypeRepository;

	public EventType findById(Integer id) {
		return eventTypeRepository.findOne(id);
	}

	public EventType findByDescription(String name) {
		return eventTypeRepository.findByDescription(name);
	}

	public void saveEventType(EventType eventType) {
		eventTypeRepository.save(eventType);
	}

	public void updateEventType(EventType eventType) {
		saveEventType(eventType);
	}

	public void deleteEventTypeById(Integer id) {
		eventTypeRepository.delete(id);
	}

	public void deleteAllEventTypes() {
		eventTypeRepository.deleteAll();
	}

	public Page<EventType> findAllEventTypesByPage(Specification<EventType> spec, Pageable pageable) {
		return eventTypeRepository.findAll(spec, pageable);
	}

	public boolean isEventTypeExist(EventType eventType) {
		return findByDescription(eventType.getDescription()) != null;
	}

}
