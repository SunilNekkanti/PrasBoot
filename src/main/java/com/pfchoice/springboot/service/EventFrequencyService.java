package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.EventFrequency;

public interface EventFrequencyService {

	EventFrequency findById(Integer id);

	EventFrequency findByDescription(String description);

	EventFrequency findByShortName(String shortName);

	void saveEventFrequency(EventFrequency eventFrequency);

	void updateEventFrequency(EventFrequency eventFrequency);

	void deleteEventFrequencyById(Integer id);

	void deleteAllEventFrequencies();

	List<EventFrequency> findAllEventFrequencies();

	boolean isEventFrequencyExist(EventFrequency eventFrequency);
}