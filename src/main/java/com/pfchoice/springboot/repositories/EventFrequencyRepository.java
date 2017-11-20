package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.EventFrequency;

@Repository
public interface EventFrequencyRepository extends JpaRepository<EventFrequency, Integer> {

	public EventFrequency findById(Integer id);

	public EventFrequency findByDescription(String description);

	public EventFrequency findByShortName(String shortName);
}
