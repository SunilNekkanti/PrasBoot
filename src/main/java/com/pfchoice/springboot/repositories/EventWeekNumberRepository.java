package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.EventWeekNumber;

@Repository
public interface EventWeekNumberRepository extends JpaRepository<EventWeekNumber, Integer> {

	public EventWeekNumber findById(Integer id);

	public EventWeekNumber findByDescription(String description);

	public EventWeekNumber findByShortName(String shortName);
}
