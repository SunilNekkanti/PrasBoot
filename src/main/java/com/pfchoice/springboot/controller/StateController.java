package com.pfchoice.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.State;
import com.pfchoice.springboot.service.StateService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StateController {

	public static final Logger logger = LoggerFactory.getLogger(StateController.class);

	@Autowired
	StateService stateService; // Service which will do all data
								// retrieval/manipulation work

	// -------------------Retrieve All
	// States---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/state/", method = RequestMethod.GET)
	public ResponseEntity<List<State>> listAllStates() {
		List<State> states = stateService.findAllStates();
		if (states.isEmpty()) {
			System.out.println("no states");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<State>>(states, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// State------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/state/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getState(@PathVariable("id") int id) {
		logger.info("Fetching State with id {}", id);
		State state = stateService.findByCode(id);
		if (state == null) {
			logger.error("State with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("State with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<State>(state, HttpStatus.OK);
	}

	// -------------------Create a
	// State-------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/state/", method = RequestMethod.POST)
	public ResponseEntity<?> createState(@RequestBody State state, UriComponentsBuilder ucBuilder) {
		logger.info("Creating State : {}", state);

		if (stateService.isStateExist(state)) {
			logger.error("Unable to create. A State with name {} already exist", state.getCode());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A State with name " + state.getCode() + " already exist."),
					HttpStatus.CONFLICT);
		}
		state.setCreatedBy("sarath");
		state.setUpdatedBy("sarath");
		stateService.saveState(state);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/state/{id}").buildAndExpand(state.getCode()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a State
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/state/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateState(@PathVariable("id") int id, @RequestBody State state) {
		logger.info("Updating State with id {}", id);

		State currentState = stateService.findByCode(id);

		if (currentState == null) {
			logger.error("Unable to update. State with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. State with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentState.setCode(state.getCode());
		currentState.setDescription(state.getDescription());

		stateService.updateState(currentState);
		return new ResponseEntity<State>(currentState, HttpStatus.OK);
	}

	// ------------------- Delete a
	// State-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/state/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteState(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting State with id {}", id);

		State state = stateService.findByCode(id);
		if (state == null) {
			logger.error("Unable to delete. State with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. State with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		stateService.deleteStateById(id);
		return new ResponseEntity<State>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All States-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/state/", method = RequestMethod.DELETE)
	public ResponseEntity<State> deleteAllStates() {
		logger.info("Deleting All States");

		stateService.deleteAllStates();
		return new ResponseEntity<State>(HttpStatus.NO_CONTENT);
	}

}