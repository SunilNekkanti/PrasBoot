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

import com.pfchoice.springboot.model.AttPhysician;
import com.pfchoice.springboot.repositories.specifications.AttPhysicianSpecifications;
import com.pfchoice.springboot.service.AttPhysicianService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class AttPhysicianController {

	public static final Logger logger = LoggerFactory.getLogger(AttPhysicianController.class);

	@Autowired
	AttPhysicianService attPhysicianService; // Service which will do all data
								// retrieval/manipulation work

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	// -------------------Retrieve AttPhysicians as per page request
	// ---------------------------------------------

	@RequestMapping(value = "/attPhysician/", method = RequestMethod.GET)
	public ResponseEntity<Page<AttPhysician>> listAllAttPhysicians(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<AttPhysician> spec = new AttPhysicianSpecifications(search);
		Page<AttPhysician> attPhysicians = attPhysicianService.findAllAttPhysiciansByPage(spec, pageRequest);
		
		if (attPhysicians.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<AttPhysician>>(attPhysicians, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// AttPhysician------------------------------------------
	@Secured({ "ROLE_ADMIN",  "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/attPhysician/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAttPhysician(@PathVariable("id") int id) {
		logger.info("Fetching AttPhysician with id {}", id);
		AttPhysician attPhysician = attPhysicianService.findById(id);
		if (attPhysician == null) {
			logger.error("AttPhysician with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("AttPhysician with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AttPhysician>(attPhysician, HttpStatus.OK);
	}

	// -------------------Create a
	// AttPhysician-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/attPhysician/", method = RequestMethod.POST)
	public ResponseEntity<?> createAttPhysician(@RequestBody AttPhysician attPhysician, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating AttPhysician : {}", attPhysician);

		if (attPhysicianService.isAttPhysicianExist(attPhysician)) {
			logger.error("Unable to create. A AttPhysician with name {} already exist", attPhysician.getName());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A AttPhysician with name " + attPhysician.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		// logger.info(" attPhysician.getRoles().size() :{} ", attPhysician.getRoles().size());
		attPhysician.setCreatedBy(username);
		attPhysician.setUpdatedBy(username);
		attPhysicianService.saveAttPhysician(attPhysician);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/attPhysician/{id}").buildAndExpand(attPhysician.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a AttPhysician
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/attPhysician/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateAttPhysician(@PathVariable("id") int id, @RequestBody AttPhysician attPhysician,
			@ModelAttribute("username") String username) {
		logger.info("Updating AttPhysician with id {}", id);

		AttPhysician currentAttPhysician = attPhysicianService.findById(id);

		if (currentAttPhysician == null) {
			logger.error("Unable to update. AttPhysician with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. AttPhysician with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentAttPhysician.setName(attPhysician.getName());
		currentAttPhysician.setCode(attPhysician.getCode());
		currentAttPhysician.setUpdatedBy(username);
		attPhysicianService.updateAttPhysician(currentAttPhysician);
		return new ResponseEntity<AttPhysician>(currentAttPhysician, HttpStatus.OK);
	}

	// ------------------- Delete a
	// AttPhysician-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/attPhysician/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAttPhysician(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting AttPhysician with id {}", id);

		AttPhysician attPhysician = attPhysicianService.findById(id);
		if (attPhysician == null) {
			logger.error("Unable to delete. AttPhysician with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. AttPhysician with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		attPhysicianService.deleteAttPhysicianById(id);
		return new ResponseEntity<AttPhysician>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All AttPhysicians-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/attPhysician/", method = RequestMethod.DELETE)
	public ResponseEntity<AttPhysician> deleteAllAttPhysicians() {
		logger.info("Deleting All AttPhysicians");

		attPhysicianService.deleteAllAttPhysicians();
		return new ResponseEntity<AttPhysician>(HttpStatus.NO_CONTENT);
	}

}