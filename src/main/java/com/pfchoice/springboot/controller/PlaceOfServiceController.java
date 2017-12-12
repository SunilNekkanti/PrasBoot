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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.PlaceOfService;
import com.pfchoice.springboot.repositories.specifications.PlaceOfServiceSpecifications;
import com.pfchoice.springboot.service.PlaceOfServiceService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class PlaceOfServiceController {

	public static final Logger logger = LoggerFactory.getLogger(PlaceOfServiceController.class);

	@Autowired
	PlaceOfServiceService placeOfServiceService; // Service which will do all
													// data
	// retrieval/manipulation work

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	// -------------------Retrieve PlaceOfServices as per page request
	// ---------------------------------------------

	@RequestMapping(value = "/placeOfService/", method = RequestMethod.GET)
	public ResponseEntity<Page<PlaceOfService>> listAllPlaceOfServices(
			@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<PlaceOfService> spec = new PlaceOfServiceSpecifications(search);
		Page<PlaceOfService> placeOfServices = placeOfServiceService.findAllPlaceOfServicesByPage(spec, pageRequest);

		if (placeOfServices.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<PlaceOfService>>(placeOfServices, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// PlaceOfService------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/placeOfService/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPlaceOfService(@PathVariable("id") int id) {
		logger.info("Fetching PlaceOfService with id {}", id);
		PlaceOfService placeOfService = placeOfServiceService.findById(id);
		if (placeOfService == null) {
			logger.error("PlaceOfService with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("PlaceOfService with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<PlaceOfService>(placeOfService, HttpStatus.OK);
	}

	// -------------------Create a
	// PlaceOfService-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/placeOfService/", method = RequestMethod.POST)
	public ResponseEntity<?> createPlaceOfService(@RequestBody PlaceOfService placeOfService,
			UriComponentsBuilder ucBuilder, @ModelAttribute("username") String username) {
		logger.info("Creating PlaceOfService : {}", placeOfService);

		if (placeOfServiceService.isPlaceOfServiceExist(placeOfService)) {
			logger.error("Unable to create. A PlaceOfService with description {} already exist",
					placeOfService.getDescription());
			return new ResponseEntity(new CustomErrorType("Unable to create. A PlaceOfService with description "
					+ placeOfService.getDescription() + " already exist."), HttpStatus.CONFLICT);
		}
		// logger.info(" placeOfService.getRoles().size() :{} ",
		// placeOfService.getRoles().size());
		placeOfService.setCreatedBy(username);
		placeOfService.setUpdatedBy(username);
		placeOfServiceService.savePlaceOfService(placeOfService);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/placeOfService/{id}").buildAndExpand(placeOfService.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a PlaceOfService
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/placeOfService/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePlaceOfService(@PathVariable("id") int id,
			@RequestBody PlaceOfService placeOfService, @ModelAttribute("username") String username) {
		logger.info("Updating PlaceOfService with id {}", id);

		PlaceOfService currentPlaceOfService = placeOfServiceService.findById(id);

		if (currentPlaceOfService == null) {
			logger.error("Unable to update. PlaceOfService with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. PlaceOfService with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentPlaceOfService.setDescription(placeOfService.getDescription());
		currentPlaceOfService.setName(placeOfService.getName());
		currentPlaceOfService.setUpdatedBy(username);
		placeOfServiceService.updatePlaceOfService(currentPlaceOfService);
		return new ResponseEntity<PlaceOfService>(currentPlaceOfService, HttpStatus.OK);
	}

	// ------------------- Delete a
	// PlaceOfService-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/placeOfService/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePlaceOfService(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting PlaceOfService with id {}", id);

		PlaceOfService placeOfService = placeOfServiceService.findById(id);
		if (placeOfService == null) {
			logger.error("Unable to delete. PlaceOfService with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. PlaceOfService with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		placeOfServiceService.deletePlaceOfServiceById(id);
		return new ResponseEntity<PlaceOfService>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// PlaceOfServices-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/placeOfService/", method = RequestMethod.DELETE)
	public ResponseEntity<PlaceOfService> deleteAllPlaceOfServices() {
		logger.info("Deleting All PlaceOfServices");

		placeOfServiceService.deleteAllPlaceOfServices();
		return new ResponseEntity<PlaceOfService>(HttpStatus.NO_CONTENT);
	}

}