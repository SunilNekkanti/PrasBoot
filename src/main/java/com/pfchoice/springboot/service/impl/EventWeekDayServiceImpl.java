package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.EventWeekDay;
import com.pfchoice.springboot.repositories.EventWeekDayRepository;
import com.pfchoice.springboot.service.EventWeekDayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("eventWeekDayService")
@Transactional
public class EventWeekDayServiceImpl implements EventWeekDayService {

	@Autowired
	private EventWeekDayRepository eventWeekDayRepository;

	public EventWeekDay findById(Integer id) {
		return eventWeekDayRepository.findOne(id);
	}

	public EventWeekDay findByDescription(String description) {
		return eventWeekDayRepository.findByDescription(description);
	}

	public EventWeekDay findByShortName(String shortName) {
		return eventWeekDayRepository.findByDescription(shortName);
	}

	public void saveEventWeekDay(EventWeekDay eventWeekDay) {
		eventWeekDayRepository.save(eventWeekDay);
	}

	public void updateEventWeekDay(EventWeekDay eventWeekDay) {
		saveEventWeekDay(eventWeekDay);
	}

	public void deleteEventWeekDayById(Integer id) {
		eventWeekDayRepository.delete(id);
	}

	public void deleteAllEventWeekDays() {
		eventWeekDayRepository.deleteAll();
	}

	public List<EventWeekDay> findAllEventWeekDays() {
		return eventWeekDayRepository.findAll();
	}

	public boolean isEventWeekDayExist(EventWeekDay eventWeekDay) {
		return findByDescription(eventWeekDay.getDescription()) != null;
	}

}
