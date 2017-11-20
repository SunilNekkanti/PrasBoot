package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.EventWeekNumber;
import com.pfchoice.springboot.repositories.EventWeekNumberRepository;
import com.pfchoice.springboot.service.EventWeekNumberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eventWeekNumberService")
@Transactional
public class EventWeekNumberServiceImpl implements EventWeekNumberService {

	@Autowired
	private EventWeekNumberRepository eventWeekNumberRepository;

	public EventWeekNumber findById(Integer id) {
		return eventWeekNumberRepository.findOne(id);
	}

	public EventWeekNumber findByDescription(String description) {
		return eventWeekNumberRepository.findByDescription(description);
	}

	public EventWeekNumber findByShortName(String shortName) {
		return eventWeekNumberRepository.findByDescription(shortName);
	}

	public void saveEventWeekNumber(EventWeekNumber eventWeekNumber) {
		eventWeekNumberRepository.save(eventWeekNumber);
	}

	public void updateEventWeekNumber(EventWeekNumber eventWeekNumber) {
		saveEventWeekNumber(eventWeekNumber);
	}

	public void deleteEventWeekNumberById(Integer id) {
		eventWeekNumberRepository.delete(id);
	}

	public void deleteAllEventWeekNumbers() {
		eventWeekNumberRepository.deleteAll();
	}

	public List<EventWeekNumber> findAllEventWeekNumbers() {
		return eventWeekNumberRepository.findAll();
	}

	public boolean isEventWeekNumberExist(EventWeekNumber eventWeekNumber) {
		return findByDescription(eventWeekNumber.getDescription()) != null;
	}

}
