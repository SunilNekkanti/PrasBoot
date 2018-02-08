package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.EventAssignment;
import com.pfchoice.springboot.repositories.EventAssignmentRepository;
import com.pfchoice.springboot.service.EventAssignmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eventAssignmentService")
@Transactional
public class EventAssignmentServiceImpl implements EventAssignmentService {

	@Autowired
	private EventAssignmentRepository eventAssignmentRepository;

	public EventAssignment findById(Integer id) {
		return eventAssignmentRepository.findOne(id);
	}

	public void saveEventAssignment(EventAssignment eventAssignment) {
		eventAssignmentRepository.save(eventAssignment);
	}

	public void updateEventAssignment(EventAssignment eventAssignment) {
		saveEventAssignment(eventAssignment);
	}

	public void deleteEventAssignmentById(Integer id) {
		eventAssignmentRepository.delete(id);
	}

	public void deleteAllEventAssignments() {
		eventAssignmentRepository.deleteAll();
	}

	public Page<EventAssignment> findAllEventAssignmentsByPage(Specification<EventAssignment> spec, Pageable pageable) {
		return eventAssignmentRepository.findAll(spec, pageable);
	}

}
