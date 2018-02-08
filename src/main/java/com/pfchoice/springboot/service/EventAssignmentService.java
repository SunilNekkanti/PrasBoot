package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.EventAssignment;

public interface EventAssignmentService {

	EventAssignment findById(Integer id);

	void saveEventAssignment(EventAssignment lead);

	void updateEventAssignment(EventAssignment lead);

	void deleteEventAssignmentById(Integer id);

	void deleteAllEventAssignments();

	Page<EventAssignment> findAllEventAssignmentsByPage(Specification<EventAssignment> spec, Pageable pageable);

}