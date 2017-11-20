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

import com.pfchoice.springboot.model.EventFrequency;
import com.pfchoice.springboot.service.EventFrequencyService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EventFrequencyController {

	public static final Logger logger = LoggerFactory.getLogger(EventFrequencyController.class);

	@Autowired
	EventFrequencyService eventFrequencyService; // Service which will do all
													// data
													// retrieval/manipulation
													// work

	// -------------------Retrieve All
	// EventFrequencys---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventFrequency/", method = RequestMethod.GET)
	public ResponseEntity<List<EventFrequency>> listAllEventFrequencys() {
		List<EventFrequency> eventFrequencys = eventFrequencyService.findAllEventFrequencies();
		if (eventFrequencys.isEmpty()) {
			System.out.println("no eventFrequencys");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<EventFrequency>>(eventFrequencys, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// EventFrequency------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventFrequency/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEventFrequency(@PathVariable("id") int id) {
		logger.info("Fetching EventFrequency with id {}", id);
		EventFrequency eventFrequency = eventFrequencyService.findById(id);
		if (eventFrequency == null) {
			logger.error("EventFrequency with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("EventFrequency with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<EventFrequency>(eventFrequency, HttpStatus.OK);
	}

	// -------------------Create a
	// EventFrequency-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventFrequency/", method = RequestMethod.POST)
	public ResponseEntity<?> createEventFrequency(@RequestBody EventFrequency eventFrequency,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating EventFrequency : {}", eventFrequency);

		if (eventFrequencyService.isEventFrequencyExist(eventFrequency)) {
			logger.error("Unable to create. A EventFrequency with name {} already exist", eventFrequency.getId());
			return new ResponseEntity(new CustomErrorType(
					"Unable to create. A EventFrequency with name " + eventFrequency.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		eventFrequency.setCreatedBy("sarath");
		eventFrequency.setUpdatedBy("sarath");
		eventFrequencyService.saveEventFrequency(eventFrequency);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/eventFrequency/{id}").buildAndExpand(eventFrequency.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a EventFrequency
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventFrequency/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEventFrequency(@PathVariable("id") int id,
			@RequestBody EventFrequency eventFrequency) {
		logger.info("Updating EventFrequency with id {}", id);

		EventFrequency currentEventFrequency = eventFrequencyService.findById(id);

		if (currentEventFrequency == null) {
			logger.error("Unable to update. EventFrequency with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. EventFrequency with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentEventFrequency.setId(eventFrequency.getId());
		currentEventFrequency.setDescription(eventFrequency.getDescription());

		eventFrequencyService.updateEventFrequency(currentEventFrequency);
		return new ResponseEntity<EventFrequency>(currentEventFrequency, HttpStatus.OK);
	}

	// ------------------- Delete a
	// EventFrequency-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventFrequency/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEventFrequency(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting EventFrequency with id {}", id);

		EventFrequency eventFrequency = eventFrequencyService.findById(id);
		if (eventFrequency == null) {
			logger.error("Unable to delete. EventFrequency with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. EventFrequency with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		eventFrequencyService.deleteEventFrequencyById(id);
		return new ResponseEntity<EventFrequency>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// EventFrequencys-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventFrequency/", method = RequestMethod.DELETE)
	public ResponseEntity<EventFrequency> deleteAllEventFrequencys() {
		logger.info("Deleting All EventFrequencys");

		eventFrequencyService.deleteAllEventFrequencies();
		return new ResponseEntity<EventFrequency>(HttpStatus.NO_CONTENT);
	}

}