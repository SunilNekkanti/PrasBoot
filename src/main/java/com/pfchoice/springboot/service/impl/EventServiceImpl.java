package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.Event;
import com.pfchoice.springboot.repositories.EventRepository;
import com.pfchoice.springboot.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;

	public Event findById(Integer id) {
		return eventRepository.findOne(id);
	}

	public void saveEvent(Event event) {
		eventRepository.save(event);
	}

	public void updateEvent(Event event) {
		saveEvent(event);
	}

	public void deleteEventById(Integer id) {
		eventRepository.delete(id);
	}

	public void deleteAllEvents() {
		eventRepository.deleteAll();
	}

	public Page<Event> findAllEventsByPage(Specification<Event> spec, Pageable pageable) {
		return eventRepository.findAll(spec, pageable);
	}

	public boolean isEventExists(String eventName) {
		return !eventRepository.findEventByEventName(eventName).isEmpty();
	}

}
