package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.EventWeekNumber;

public interface EventWeekNumberService {

	EventWeekNumber findById(Integer id);

	EventWeekNumber findByDescription(String description);

	EventWeekNumber findByShortName(String shortName);

	void saveEventWeekNumber(EventWeekNumber eventWeekDay);

	void updateEventWeekNumber(EventWeekNumber eventWeekDay);

	void deleteEventWeekNumberById(Integer id);

	void deleteAllEventWeekNumbers();

	List<EventWeekNumber> findAllEventWeekNumbers();

	boolean isEventWeekNumberExist(EventWeekNumber eventWeekDay);
}