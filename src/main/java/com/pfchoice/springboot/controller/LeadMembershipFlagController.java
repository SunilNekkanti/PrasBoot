package com.pfchoice.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.LeadMembershipFlag;
import com.pfchoice.springboot.service.LeadMembershipFlagService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class LeadMembershipFlagController {

	public static final Logger logger = LoggerFactory.getLogger(LeadMembershipFlagController.class);

	@Autowired
	LeadMembershipFlagService leadMembershipFlagService; // Service which will do all data
												// retrieval/manipulation work


	// -------------------Retrieve Single
	// LeadMembershipFlag------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadMembershipFlag/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getLeadMembershipFlag(@PathVariable("id") int id) {
		logger.info("Fetching LeadMembershipFlag with id {}", id);
		LeadMembershipFlag leadMembershipFlag = leadMembershipFlagService.findById(id);
		if (leadMembershipFlag == null) {
			logger.error("LeadMembershipFlag with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("LeadMembershipFlag with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<LeadMembershipFlag>(leadMembershipFlag, HttpStatus.OK);
	}

	// -------------------Create a
	// LeadMembershipFlag-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadMembershipFlag/", method = RequestMethod.POST)
	public ResponseEntity<?> createLeadMembershipFlag(@RequestBody LeadMembershipFlag leadMembershipFlag, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating LeadMembershipFlag : {}", leadMembershipFlag);

		if (leadMembershipFlagService.isLeadMembershipFlagExist(leadMembershipFlag)) {
			logger.error("Unable to create. A LeadMembershipFlag with name {} already exist", leadMembershipFlag.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A LeadMembershipFlag with name " + leadMembershipFlag.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		leadMembershipFlag.setCreatedBy(username);
		leadMembershipFlag.setUpdatedBy(username);
		leadMembershipFlagService.saveLeadMembershipFlag(leadMembershipFlag);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/leadMembershipFlag/{id}").buildAndExpand(leadMembershipFlag.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a LeadMembershipFlag
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadMembershipFlag/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLeadMembershipFlag(@PathVariable("id") int id, @RequestBody LeadMembershipFlag leadMembershipFlag,
			@ModelAttribute("username") String username) {
		logger.info("Updating LeadMembershipFlag with id {}", id);

		LeadMembershipFlag currentLeadMembershipFlag = leadMembershipFlagService.findById(id);

		if (currentLeadMembershipFlag == null) {
			logger.error("Unable to update. LeadMembershipFlag with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. LeadMembershipFlag with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentLeadMembershipFlag.setId(leadMembershipFlag.getId());
		currentLeadMembershipFlag.setScheduledFlag(leadMembershipFlag.getScheduledFlag());
		currentLeadMembershipFlag.setEngagedFlag(leadMembershipFlag.getEngagedFlag());
		currentLeadMembershipFlag.setUpdatedBy(username);
		leadMembershipFlagService.updateLeadMembershipFlag(currentLeadMembershipFlag);
		return new ResponseEntity<LeadMembershipFlag>(currentLeadMembershipFlag, HttpStatus.OK);
	}

	// ------------------- Delete a
	// LeadMembershipFlag-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadMembershipFlag/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLeadMembershipFlag(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting LeadMembershipFlag with id {}", id);

		LeadMembershipFlag leadMembershipFlag = leadMembershipFlagService.findById(id);
		if (leadMembershipFlag == null) {
			logger.error("Unable to delete. LeadMembershipFlag with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. LeadMembershipFlag with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		leadMembershipFlagService.deleteLeadMembershipFlagById(id);
		return new ResponseEntity<LeadMembershipFlag>(HttpStatus.NO_CONTENT);
	}


}