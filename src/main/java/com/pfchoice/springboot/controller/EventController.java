package com.pfchoice.springboot.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.Event;
import com.pfchoice.springboot.repositories.specifications.EventSpecifications;
import com.pfchoice.springboot.service.EmailService;
import com.pfchoice.springboot.service.EventService;
import com.pfchoice.springboot.service.UserService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class EventController {

	public static final Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	EventService eventService; // Service which will do all data
								// retrieval/manipulation work

	@Autowired
	UserService userService; // Service which will do all data//
								// retrieval/manipulation work

	@Autowired
	EmailService emailService;
	// -------------------Retrieve All
	// Events---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/event/", method = RequestMethod.GET)
	public ResponseEntity<Page<Event>> listAllEvents(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search, @ModelAttribute("userId") Integer userId,
			@ModelAttribute("roleName") String roleName) {

		Specification<Event> spec = new EventSpecifications(userId, roleName, search);
		Page<Event> events = eventService.findAllEventsByPage(spec, pageRequest);

		if (events.getTotalElements() == 0) {
			System.out.println("no events");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Event>>(events, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Event------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/event/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEvent(@PathVariable("id") int id) {
		logger.info("Fetching Event with id {}", id);
		Event event = eventService.findById(id);
		if (event == null) {
			logger.error("Event with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Event with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Event>(event, HttpStatus.OK);
	}

	// -------------------Create a
	// Event-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_EVENT_COORDINATOR" })
	@RequestMapping(value = "/event/", method = RequestMethod.POST)
	public ResponseEntity<?> createEvent(@RequestBody Event event, UriComponentsBuilder ucBuilder,
			@ModelAttribute("userId") Integer userId, @ModelAttribute("username") String username)
			throws MessagingException, IOException, InterruptedException {
		logger.info("Creating Event : {}", event);

		if (eventService.isEventExists(event.getEventName())) {
			logger.error("Unable to create. A Event with name {} already exist", event.getEventName());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A Event with name " + event.getEventName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		event.setCreatedBy(username);
		event.setUpdatedBy(username);
		eventService.saveEvent(event);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/event/{id}").buildAndExpand(event.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Event
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/event/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEvent(@PathVariable("id") int id, @RequestBody Event event,
			@ModelAttribute("username") String username) {
		logger.info("Updating Event with id {}", id);

		Event currentEvent = eventService.findById(id);

		if (currentEvent == null) {
			logger.error("Unable to update. Event with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Event with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentEvent.setEventName(event.getEventName());
		currentEvent.setEventDateStartTime(event.getEventDateStartTime());
		currentEvent.setEventDateEndTime(event.getEventDateEndTime());
		currentEvent.setEventType(event.getEventType());
		currentEvent.setContact(event.getContact());
		currentEvent.setNotes(event.getNotes());
		currentEvent.setUpdatedBy(username);
		currentEvent.getAttachments().clear();
		currentEvent.getAttachments().addAll(event.getAttachments());
		eventService.updateEvent(currentEvent);

		return new ResponseEntity<Event>(currentEvent, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Event-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/event/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEvent(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Event with id {}", id);

		Event event = eventService.findById(id);
		if (event == null) {
			logger.error("Unable to delete. Event with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Event with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		eventService.deleteEventById(id);
		return new ResponseEntity<Event>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Events-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/event/", method = RequestMethod.DELETE)
	public ResponseEntity<Event> deleteAllEvents() {
		logger.info("Deleting All Events");

		eventService.deleteAllEvents();
		return new ResponseEntity<Event>(HttpStatus.NO_CONTENT);
	}

}