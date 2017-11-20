package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.EventWeekDay;

public interface EventWeekDayService {

	EventWeekDay findById(Integer id);

	EventWeekDay findByDescription(String description);

	EventWeekDay findByShortName(String shortName);

	void saveEventWeekDay(EventWeekDay eventWeekDay);

	void updateEventWeekDay(EventWeekDay eventWeekDay);

	void deleteEventWeekDayById(Integer id);

	void deleteAllEventWeekDays();

	List<EventWeekDay> findAllEventWeekDays();

	boolean isEventWeekDayExist(EventWeekDay eventWeekDay);
}