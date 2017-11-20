package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.EventWeekDay;

@Repository
public interface EventWeekDayRepository extends JpaRepository<EventWeekDay, Integer> {

	public EventWeekDay findById(Integer id);

	public EventWeekDay findByDescription(String description);

	public EventWeekDay findByShortName(String shortName);
}
