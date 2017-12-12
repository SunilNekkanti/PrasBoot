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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.HedisMeasureGroup;
import com.pfchoice.springboot.repositories.specifications.HedisMeasureGroupSpecifications;
import com.pfchoice.springboot.service.HedisMeasureGroupService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class HedisMeasureGroupController {

	public static final Logger logger = LoggerFactory.getLogger(HedisMeasureGroupController.class);

	@Autowired
	HedisMeasureGroupService hedisMeasureGroupService; // Service which will do
														// all data
	// retrieval/manipulation work

	// -------------------Retrieve All
	// HedisMeasureGroups---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureGroup/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllHedisMeasureGroups(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<HedisMeasureGroup> spec = new HedisMeasureGroupSpecifications(search);
		Page<HedisMeasureGroup> hedisMeasureGroups = hedisMeasureGroupService.findAllHedisMeasureGroupsByPage(spec,
				pageRequest);

		if (hedisMeasureGroups.getTotalElements() == 0) {
			System.out.println("no hedisMeasureGroups");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<HedisMeasureGroup>>(hedisMeasureGroups, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// HedisMeasureGroup------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureGroup/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getHedisMeasureGroup(@PathVariable("id") int id) {
		logger.info("Fetching HedisMeasureGroup with id {}", id);
		HedisMeasureGroup hedisMeasureGroup = hedisMeasureGroupService.findById(id);
		if (hedisMeasureGroup == null) {
			logger.error("HedisMeasureGroup with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("HedisMeasureGroup with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<HedisMeasureGroup>(hedisMeasureGroup, HttpStatus.OK);
	}

	// -------------------Create a
	// HedisMeasureGroup-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureGroup/", method = RequestMethod.POST)
	public ResponseEntity<?> createHedisMeasureGroup(@RequestBody HedisMeasureGroup hedisMeasureGroup,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating HedisMeasureGroup : {}", hedisMeasureGroup);

		if (hedisMeasureGroupService.isHedisMeasureGroupExist(hedisMeasureGroup)) {
			logger.error("Unable to create. A HedisMeasureGroup with name {} already exist", hedisMeasureGroup.getId());
			return new ResponseEntity(new CustomErrorType(
					"Unable to create. A HedisMeasureGroup with name " + hedisMeasureGroup.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		hedisMeasureGroup.setCreatedBy("sarath");
		hedisMeasureGroup.setUpdatedBy("sarath");
		hedisMeasureGroupService.saveHedisMeasureGroup(hedisMeasureGroup);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/hedisMeasureGroup/{id}").buildAndExpand(hedisMeasureGroup.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a HedisMeasureGroup
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureGroup/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateHedisMeasureGroup(@PathVariable("id") int id,
			@RequestBody HedisMeasureGroup hedisMeasureGroup) {
		logger.info("Updating HedisMeasureGroup with id {}", id);

		HedisMeasureGroup currentHedisMeasureGroup = hedisMeasureGroupService.findById(id);

		if (currentHedisMeasureGroup == null) {
			logger.error("Unable to update. HedisMeasureGroup with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. HedisMeasureGroup with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentHedisMeasureGroup.setCode(hedisMeasureGroup.getCode());
		currentHedisMeasureGroup.setDescription(hedisMeasureGroup.getDescription());

		hedisMeasureGroupService.updateHedisMeasureGroup(currentHedisMeasureGroup);
		return new ResponseEntity<HedisMeasureGroup>(currentHedisMeasureGroup, HttpStatus.OK);
	}

	// ------------------- Delete a
	// HedisMeasureGroup-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureGroup/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteHedisMeasureGroup(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting HedisMeasureGroup with id {}", id);

		HedisMeasureGroup hedisMeasureGroup = hedisMeasureGroupService.findById(id);
		if (hedisMeasureGroup == null) {
			logger.error("Unable to delete. HedisMeasureGroup with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. HedisMeasureGroup with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		hedisMeasureGroupService.deleteHedisMeasureGroupById(id);
		return new ResponseEntity<HedisMeasureGroup>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// HedisMeasureGroups-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/hedisMeasureGroup/", method = RequestMethod.DELETE)
	public ResponseEntity<HedisMeasureGroup> deleteAllHedisMeasureGroups() {
		logger.info("Deleting All HedisMeasureGroups");

		hedisMeasureGroupService.deleteAllHedisMeasureGroups();
		return new ResponseEntity<HedisMeasureGroup>(HttpStatus.NO_CONTENT);
	}

}