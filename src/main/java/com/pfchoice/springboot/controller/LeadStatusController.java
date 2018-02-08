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

import com.pfchoice.springboot.model.LeadStatus;
import com.pfchoice.springboot.repositories.specifications.LeadStatusSpecifications;
import com.pfchoice.springboot.service.LeadStatusService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LeadStatusController {

	public static final Logger logger = LoggerFactory.getLogger(LeadStatusController.class);

	@Autowired
	LeadStatusService leadStatusService; // Service which will do all data
											// retrieval/manipulation work

	// -------------------Retrieve All
	// LeadStatuses---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatus/", method = RequestMethod.GET)
	public ResponseEntity<Page<LeadStatus>> listAllLeadStatuses(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {
		Specification<LeadStatus> spec = null;
		if (!"".equals(search))
			spec = new LeadStatusSpecifications(search);

		Page<LeadStatus> leadStatuss = leadStatusService.findAllLeadStatusesByPage(spec, pageRequest);
		if (leadStatuss.getTotalElements() == 0) {
			System.out.println("no leadStatuss");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<LeadStatus>>(leadStatuss, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// LeadStatus------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatus/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getLeadStatus(@PathVariable("id") short id) {
		logger.info("Fetching LeadStatus with id {}", id);
		LeadStatus leadStatus = leadStatusService.findById(id);
		if (leadStatus == null) {
			logger.error("LeadStatus with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("LeadStatus with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<LeadStatus>(leadStatus, HttpStatus.OK);
	}

	// -------------------Create a
	// LeadStatus-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatus/", method = RequestMethod.POST)
	public ResponseEntity<?> createLeadStatus(@RequestBody LeadStatus leadStatus, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating LeadStatus : {}", leadStatus);

		if (leadStatusService.isLeadStatusExist(leadStatus)) {
			logger.error("Unable to create. A LeadStatus with name {} already exist", leadStatus.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A LeadStatus with name " + leadStatus.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		leadStatus.setCreatedBy(username);
		leadStatus.setUpdatedBy(username);
		leadStatusService.saveLeadStatus(leadStatus);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/leadStatus/{id}").buildAndExpand(leadStatus.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a LeadStatus
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatus/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLeadStatus(@PathVariable("id") short id, @RequestBody LeadStatus leadStatus,
			@ModelAttribute("username") String username) {
		logger.info("Updating LeadStatus with id {}", id);

		LeadStatus currentLeadStatus = leadStatusService.findById(id);

		if (currentLeadStatus == null) {
			logger.error("Unable to update. LeadStatus with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. LeadStatus with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentLeadStatus.setDescription(leadStatus.getDescription());
		currentLeadStatus.setUpdatedBy(username);
		leadStatusService.updateLeadStatus(currentLeadStatus);
		return new ResponseEntity<LeadStatus>(currentLeadStatus, HttpStatus.OK);
	}

	// ------------------- Delete a
	// LeadStatus-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatus/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLeadStatus(@PathVariable("id") short id) {
		logger.info("Fetching & Deleting LeadStatus with id {}", id);

		LeadStatus leadStatus = leadStatusService.findById(id);
		if (leadStatus == null) {
			logger.error("Unable to delete. LeadStatus with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. LeadStatus with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		leadStatusService.deleteLeadStatusById(id);
		return new ResponseEntity<LeadStatus>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All LeadStatuses-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/leadStatus/", method = RequestMethod.DELETE)
	public ResponseEntity<LeadStatus> deleteAllLeadStatuses() {
		logger.info("Deleting All LeadStatuses");

		leadStatusService.deleteAllLeadStatuses();
		return new ResponseEntity<LeadStatus>(HttpStatus.NO_CONTENT);
	}

}