package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.EventFrequency;
import com.pfchoice.springboot.repositories.EventFrequencyRepository;
import com.pfchoice.springboot.service.EventFrequencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eventFrequencyService")
@Transactional
public class EventFrequencyServiceImpl implements EventFrequencyService {

	@Autowired
	private EventFrequencyRepository eventFrequencyRepository;

	public EventFrequency findById(Integer id) {
		return eventFrequencyRepository.findOne(id);
	}

	public EventFrequency findByDescription(String description) {
		return eventFrequencyRepository.findByDescription(description);
	}

	public EventFrequency findByShortName(String shortName) {
		return eventFrequencyRepository.findByDescription(shortName);
	}

	public void saveEventFrequency(EventFrequency eventFrequency) {
		eventFrequencyRepository.save(eventFrequency);
	}

	public void updateEventFrequency(EventFrequency eventFrequency) {
		saveEventFrequency(eventFrequency);
	}

	public void deleteEventFrequencyById(Integer id) {
		eventFrequencyRepository.delete(id);
	}

	public void deleteAllEventFrequencies() {
		eventFrequencyRepository.deleteAll();
	}

	public List<EventFrequency> findAllEventFrequencies() {
		return eventFrequencyRepository.findAll();
	}

	public boolean isEventFrequencyExist(EventFrequency eventFrequency) {
		return findByDescription(eventFrequency.getDescription()) != null;
	}

}
