package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.EventMonth;

public interface EventMonthService {

	EventMonth findById(Integer id);

	EventMonth findByDescription(String description);

	EventMonth findByShortName(String shortName);

	void saveEventMonth(EventMonth eventMonth);

	void updateEventMonth(EventMonth eventMonth);

	void deleteEventMonthById(Integer id);

	void deleteAllEventMonths();

	List<EventMonth> findAllEventMonths();

	boolean isEventMonthExist(EventMonth eventMonth);
}