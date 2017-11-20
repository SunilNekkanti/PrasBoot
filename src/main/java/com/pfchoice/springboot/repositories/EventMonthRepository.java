package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.EventMonth;

@Repository
public interface EventMonthRepository extends JpaRepository<EventMonth, Integer> {

	public EventMonth findById(Integer id);

	public EventMonth findByDescription(String description);

	public EventMonth findByShortName(String shortName);
}
