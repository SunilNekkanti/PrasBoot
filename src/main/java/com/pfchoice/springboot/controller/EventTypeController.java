package com.pfchoice.springboot.controller;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.EventType;
import com.pfchoice.springboot.repositories.specifications.EventTypeSpecifications;
import com.pfchoice.springboot.service.EventTypeService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EventTypeController {

	public static final Logger logger = LoggerFactory.getLogger(EventTypeController.class);

	@Autowired
	EventTypeService eventTypeService; // Service which will do all data
												// retrieval/manipulation work

	// -------------------Retrieve All
	// EventTypes---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventType/", method = RequestMethod.GET)
	public ResponseEntity<Page<EventType>> listAllEventTypes(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {
		Specification<EventType> spec   = new EventTypeSpecifications(search);

		Page<EventType> eventTypes = eventTypeService.findAllEventTypesByPage(spec, pageRequest);
		if (eventTypes.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<EventType>>(eventTypes, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// EventType------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventType/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEventType(@PathVariable("id") int id) {
		logger.info("Fetching EventType with id {}", id);
		EventType eventType = eventTypeService.findById(id);
		if (eventType == null) {
			logger.error("EventType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("EventType with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<EventType>(eventType, HttpStatus.OK);
	}

	// -------------------Create a
	// EventType-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventType/", method = RequestMethod.POST)
	public ResponseEntity<?> createEventType(@RequestBody EventType eventType, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating EventType : {}", eventType);

		if (eventTypeService.isEventTypeExist(eventType)) {
			logger.error("Unable to create. A EventType with name {} already exist", eventType.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A EventType with name " + eventType.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		eventType.setCreatedBy(username);
		eventType.setUpdatedBy(username);
		eventTypeService.saveEventType(eventType);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/eventType/{id}").buildAndExpand(eventType.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a EventType
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventType/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEventType(@PathVariable("id") int id, @RequestBody EventType eventType,
			@ModelAttribute("username") String username) {
		logger.info("Updating EventType with id {}", id);

		EventType currentEventType = eventTypeService.findById(id);

		if (currentEventType == null) {
			logger.error("Unable to update. EventType with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. EventType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentEventType.setId(eventType.getId());
		currentEventType.setDescription(eventType.getDescription());
		currentEventType.setUpdatedBy(username);
		eventTypeService.updateEventType(currentEventType);
		return new ResponseEntity<EventType>(currentEventType, HttpStatus.OK);
	}

	// ------------------- Delete a
	// EventType-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/eventType/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEventType(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting EventType with id {}", id);

		EventType eventType = eventTypeService.findById(id);
		if (eventType == null) {
			logger.error("Unable to delete. EventType with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. EventType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		eventTypeService.deleteEventTypeById(id);
		return new ResponseEntity<EventType>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All EventTypes-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/eventType/", method = RequestMethod.DELETE)
	public ResponseEntity<EventType> deleteAllEventTypes() {
		logger.info("Deleting All EventTypes");

		eventTypeService.deleteAllEventTypes();
		return new ResponseEntity<EventType>(HttpStatus.NO_CONTENT);
	}

}