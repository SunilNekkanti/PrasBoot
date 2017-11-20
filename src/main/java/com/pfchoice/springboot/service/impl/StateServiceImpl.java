package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.State;
import com.pfchoice.springboot.repositories.StateRepository;
import com.pfchoice.springboot.service.StateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("stateService")
@Transactional
public class StateServiceImpl implements StateService {

	@Autowired
	private StateRepository stateRepository;

	public State findByCode(Integer code) {
		return stateRepository.findByCode(code);
	}

	public State findByDescription(String description) {
		return stateRepository.findByDescription(description);
	}

	public void saveState(State state) {
		stateRepository.save(state);
	}

	public void updateState(State state) {
		saveState(state);
	}

	public void deleteStateById(Integer id) {
		stateRepository.delete(id);
	}

	public void deleteAllStates() {
		stateRepository.deleteAll();
	}

	public List<State> findAllStates() {
		return stateRepository.findAll();
	}

	public boolean isStateExist(State state) {
		return findByCode(state.getCode()) != null;
	}

}
