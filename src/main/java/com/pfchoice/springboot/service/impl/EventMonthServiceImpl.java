package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.EventMonth;
import com.pfchoice.springboot.repositories.EventMonthRepository;
import com.pfchoice.springboot.service.EventMonthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eventMonthService")
@Transactional
public class EventMonthServiceImpl implements EventMonthService {

	@Autowired
	private EventMonthRepository eventMonthRepository;

	public EventMonth findById(Integer id) {
		return eventMonthRepository.findOne(id);
	}

	public EventMonth findByDescription(String description) {
		return eventMonthRepository.findByDescription(description);
	}

	public EventMonth findByShortName(String shortName) {
		return eventMonthRepository.findByDescription(shortName);
	}

	public void saveEventMonth(EventMonth eventMonth) {
		eventMonthRepository.save(eventMonth);
	}

	public void updateEventMonth(EventMonth eventMonth) {
		saveEventMonth(eventMonth);
	}

	public void deleteEventMonthById(Integer id) {
		eventMonthRepository.delete(id);
	}

	public void deleteAllEventMonths() {
		eventMonthRepository.deleteAll();
	}

	public List<EventMonth> findAllEventMonths() {
		return eventMonthRepository.findAll();
	}

	public boolean isEventMonthExist(EventMonth eventMonth) {
		return findByDescription(eventMonth.getDescription()) != null;
	}

}
