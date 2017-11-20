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

import com.pfchoice.springboot.model.EventMonth;
import com.pfchoice.springboot.service.EventMonthService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EventMonthController {

	public static final Logger logger = LoggerFactory.getLogger(EventMonthController.class);

	@Autowired
	EventMonthService eventMonthService; // Service which will do all data
											// retrieval/manipulation work

	// -------------------Retrieve All
	// EventMonths---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventMonth/", method = RequestMethod.GET)
	public ResponseEntity<List<EventMonth>> listAllEventMonths() {
		List<EventMonth> eventMonths = eventMonthService.findAllEventMonths();
		if (eventMonths.isEmpty()) {
			System.out.println("no eventMonths");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<EventMonth>>(eventMonths, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// EventMonth------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventMonth/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEventMonth(@PathVariable("id") int id) {
		logger.info("Fetching EventMonth with id {}", id);
		EventMonth eventMonth = eventMonthService.findById(id);
		if (eventMonth == null) {
			logger.error("EventMonth with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("EventMonth with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<EventMonth>(eventMonth, HttpStatus.OK);
	}

	// -------------------Create a
	// EventMonth-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventMonth/", method = RequestMethod.POST)
	public ResponseEntity<?> createEventMonth(@RequestBody EventMonth eventMonth, UriComponentsBuilder ucBuilder) {
		logger.info("Creating EventMonth : {}", eventMonth);

		if (eventMonthService.isEventMonthExist(eventMonth)) {
			logger.error("Unable to create. A EventMonth with name {} already exist", eventMonth.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A EventMonth with name " + eventMonth.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		eventMonth.setCreatedBy("sarath");
		eventMonth.setUpdatedBy("sarath");
		eventMonthService.saveEventMonth(eventMonth);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/eventMonth/{id}").buildAndExpand(eventMonth.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a EventMonth
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventMonth/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEventMonth(@PathVariable("id") int id, @RequestBody EventMonth eventMonth) {
		logger.info("Updating EventMonth with id {}", id);

		EventMonth currentEventMonth = eventMonthService.findById(id);

		if (currentEventMonth == null) {
			logger.error("Unable to update. EventMonth with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. EventMonth with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentEventMonth.setId(eventMonth.getId());
		currentEventMonth.setDescription(eventMonth.getDescription());

		eventMonthService.updateEventMonth(currentEventMonth);
		return new ResponseEntity<EventMonth>(currentEventMonth, HttpStatus.OK);
	}

	// ------------------- Delete a
	// EventMonth-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventMonth/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEventMonth(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting EventMonth with id {}", id);

		EventMonth eventMonth = eventMonthService.findById(id);
		if (eventMonth == null) {
			logger.error("Unable to delete. EventMonth with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. EventMonth with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		eventMonthService.deleteEventMonthById(id);
		return new ResponseEntity<EventMonth>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All EventMonths-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventMonth/", method = RequestMethod.DELETE)
	public ResponseEntity<EventMonth> deleteAllEventMonths() {
		logger.info("Deleting All EventMonths");

		eventMonthService.deleteAllEventMonths();
		return new ResponseEntity<EventMonth>(HttpStatus.NO_CONTENT);
	}

}