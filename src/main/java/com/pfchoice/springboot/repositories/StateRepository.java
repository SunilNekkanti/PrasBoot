package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

	public State findByCode(Integer code);

	public State findByDescription(String description);
}
