package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.State;

public interface StateService {

	State findByCode(Integer code);

	State findByDescription(String description);

	void saveState(State state);

	void updateState(State state);

	void deleteStateById(Integer id);

	void deleteAllStates();

	List<State> findAllStates();

	boolean isStateExist(State state);
}