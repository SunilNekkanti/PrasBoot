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

import com.pfchoice.springboot.model.EventWeekNumber;
import com.pfchoice.springboot.service.EventWeekNumberService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EventWeekNumberController {

	public static final Logger logger = LoggerFactory.getLogger(EventWeekNumberController.class);

	@Autowired
	EventWeekNumberService eventWeekNumberService; // Service which will do all
													// data
													// retrieval/manipulation
													// work

	// -------------------Retrieve All
	// EventWeekNumbers---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventWeekNumber/", method = RequestMethod.GET)
	public ResponseEntity<List<EventWeekNumber>> listAllEventWeekNumbers() {
		List<EventWeekNumber> eventWeekNumbers = eventWeekNumberService.findAllEventWeekNumbers();
		if (eventWeekNumbers.isEmpty()) {
			System.out.println("no eventWeekNumbers");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<EventWeekNumber>>(eventWeekNumbers, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// EventWeekNumber------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventWeekNumber/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEventWeekNumber(@PathVariable("id") int id) {
		logger.info("Fetching EventWeekNumber with id {}", id);
		EventWeekNumber eventWeekNumber = eventWeekNumberService.findById(id);
		if (eventWeekNumber == null) {
			logger.error("EventWeekNumber with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("EventWeekNumber with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<EventWeekNumber>(eventWeekNumber, HttpStatus.OK);
	}

	// -------------------Create a
	// EventWeekNumber-------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekNumber/", method = RequestMethod.POST)
	public ResponseEntity<?> createEventWeekNumber(@RequestBody EventWeekNumber eventWeekNumber,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating EventWeekNumber : {}", eventWeekNumber);

		if (eventWeekNumberService.isEventWeekNumberExist(eventWeekNumber)) {
			logger.error("Unable to create. A EventWeekNumber with name {} already exist", eventWeekNumber.getId());
			return new ResponseEntity(new CustomErrorType(
					"Unable to create. A EventWeekNumber with name " + eventWeekNumber.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		eventWeekNumber.setCreatedBy("sarath");
		eventWeekNumber.setUpdatedBy("sarath");
		eventWeekNumberService.saveEventWeekNumber(eventWeekNumber);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/eventWeekNumber/{id}").buildAndExpand(eventWeekNumber.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a EventWeekNumber
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekNumber/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEventWeekNumber(@PathVariable("id") int id,
			@RequestBody EventWeekNumber eventWeekNumber) {
		logger.info("Updating EventWeekNumber with id {}", id);

		EventWeekNumber currentEventWeekNumber = eventWeekNumberService.findById(id);

		if (currentEventWeekNumber == null) {
			logger.error("Unable to update. EventWeekNumber with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. EventWeekNumber with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentEventWeekNumber.setId(eventWeekNumber.getId());
		currentEventWeekNumber.setDescription(eventWeekNumber.getDescription());

		eventWeekNumberService.updateEventWeekNumber(currentEventWeekNumber);
		return new ResponseEntity<EventWeekNumber>(currentEventWeekNumber, HttpStatus.OK);
	}

	// ------------------- Delete a
	// EventWeekNumber-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekNumber/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEventWeekNumber(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting EventWeekNumber with id {}", id);

		EventWeekNumber eventWeekNumber = eventWeekNumberService.findById(id);
		if (eventWeekNumber == null) {
			logger.error("Unable to delete. EventWeekNumber with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. EventWeekNumber with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		eventWeekNumberService.deleteEventWeekNumberById(id);
		return new ResponseEntity<EventWeekNumber>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// EventWeekNumbers-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekNumber/", method = RequestMethod.DELETE)
	public ResponseEntity<EventWeekNumber> deleteAllEventWeekNumbers() {
		logger.info("Deleting All EventWeekNumbers");

		eventWeekNumberService.deleteAllEventWeekNumbers();
		return new ResponseEntity<EventWeekNumber>(HttpStatus.NO_CONTENT);
	}

}