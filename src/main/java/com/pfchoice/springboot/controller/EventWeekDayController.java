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

import com.pfchoice.springboot.model.EventWeekDay;
import com.pfchoice.springboot.service.EventWeekDayService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EventWeekDayController {

	public static final Logger logger = LoggerFactory.getLogger(EventWeekDayController.class);

	@Autowired
	EventWeekDayService eventWeekDayService; // Service which will do all data
												// retrieval/manipulation work

	// -------------------Retrieve All
	// EventWeekDays---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventWeekDay/", method = RequestMethod.GET)
	public ResponseEntity<List<EventWeekDay>> listAllEventWeekDays() {
		List<EventWeekDay> eventWeekDays = eventWeekDayService.findAllEventWeekDays();
		if (eventWeekDays.isEmpty()) {
			System.out.println("no eventWeekDays");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<EventWeekDay>>(eventWeekDays, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// EventWeekDay------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventWeekDay/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEventWeekDay(@PathVariable("id") int id) {
		logger.info("Fetching EventWeekDay with id {}", id);
		EventWeekDay eventWeekDay = eventWeekDayService.findById(id);
		if (eventWeekDay == null) {
			logger.error("EventWeekDay with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("EventWeekDay with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<EventWeekDay>(eventWeekDay, HttpStatus.OK);
	}

	// -------------------Create a
	// EventWeekDay-------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekDay/", method = RequestMethod.POST)
	public ResponseEntity<?> createEventWeekDay(@RequestBody EventWeekDay eventWeekDay,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating EventWeekDay : {}", eventWeekDay);

		if (eventWeekDayService.isEventWeekDayExist(eventWeekDay)) {
			logger.error("Unable to create. A EventWeekDay with name {} already exist", eventWeekDay.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A EventWeekDay with name " + eventWeekDay.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		eventWeekDay.setCreatedBy("sarath");
		eventWeekDay.setUpdatedBy("sarath");
		eventWeekDayService.saveEventWeekDay(eventWeekDay);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/eventWeekDay/{id}").buildAndExpand(eventWeekDay.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a EventWeekDay
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekDay/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEventWeekDay(@PathVariable("id") int id, @RequestBody EventWeekDay eventWeekDay) {
		logger.info("Updating EventWeekDay with id {}", id);

		EventWeekDay currentEventWeekDay = eventWeekDayService.findById(id);

		if (currentEventWeekDay == null) {
			logger.error("Unable to update. EventWeekDay with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. EventWeekDay with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentEventWeekDay.setId(eventWeekDay.getId());
		currentEventWeekDay.setDescription(eventWeekDay.getDescription());

		eventWeekDayService.updateEventWeekDay(currentEventWeekDay);
		return new ResponseEntity<EventWeekDay>(currentEventWeekDay, HttpStatus.OK);
	}

	// ------------------- Delete a
	// EventWeekDay-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekDay/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEventWeekDay(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting EventWeekDay with id {}", id);

		EventWeekDay eventWeekDay = eventWeekDayService.findById(id);
		if (eventWeekDay == null) {
			logger.error("Unable to delete. EventWeekDay with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. EventWeekDay with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		eventWeekDayService.deleteEventWeekDayById(id);
		return new ResponseEntity<EventWeekDay>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All EventWeekDays-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventWeekDay/", method = RequestMethod.DELETE)
	public ResponseEntity<EventWeekDay> deleteAllEventWeekDays() {
		logger.info("Deleting All EventWeekDays");

		eventWeekDayService.deleteAllEventWeekDays();
		return new ResponseEntity<EventWeekDay>(HttpStatus.NO_CONTENT);
	}

}