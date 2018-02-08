package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Event;

public interface EventService {

	Event findById(Integer id);

	void saveEvent(Event lead);

	void updateEvent(Event lead);

	void deleteEventById(Integer id);

	void deleteAllEvents();

	Page<Event> findAllEventsByPage(Specification<Event> spec, Pageable pageable);

	boolean isEventExists(String eventName);
}