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

import com.pfchoice.springboot.model.LeadStatusDetail;
import com.pfchoice.springboot.repositories.specifications.LeadStatusDetailSpecifications;
import com.pfchoice.springboot.service.LeadStatusDetailService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LeadStatusDetailController {

	public static final Logger logger = LoggerFactory.getLogger(LeadStatusDetailController.class);

	@Autowired
	LeadStatusDetailService leadStatusDetailService; // Service which will do all data
											// retrieval/manipulation work

	// -------------------Retrieve All
	// LeadStatusDetailes---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatusDetail/", method = RequestMethod.GET)
	public ResponseEntity<Page<LeadStatusDetail>> listAllLeadStatusDetailes(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {
		Specification<LeadStatusDetail> spec = null;
		if (!"".equals(search))
			spec = new LeadStatusDetailSpecifications(search);

		Page<LeadStatusDetail> leadStatusDetails = leadStatusDetailService.findAllLeadStatusDetailsByPage(spec, pageRequest);
		if (leadStatusDetails.getTotalElements() == 0) {
			System.out.println("no leadStatusDetails");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<LeadStatusDetail>>(leadStatusDetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// LeadStatusDetail------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatusDetail/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getLeadStatusDetail(@PathVariable("id") short id) {
		logger.info("Fetching LeadStatusDetail with id {}", id);
		LeadStatusDetail leadStatusDetail = leadStatusDetailService.findById(id);
		if (leadStatusDetail == null) {
			logger.error("LeadStatusDetail with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("LeadStatusDetail with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<LeadStatusDetail>(leadStatusDetail, HttpStatus.OK);
	}

	// -------------------Create a
	// LeadStatusDetail-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatusDetail/", method = RequestMethod.POST)
	public ResponseEntity<?> createLeadStatusDetail(@RequestBody LeadStatusDetail leadStatusDetail, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating LeadStatusDetail : {}", leadStatusDetail);

		if (leadStatusDetailService.isLeadStatusDetailExist(leadStatusDetail)) {
			logger.error("Unable to create. A LeadStatusDetail with name {} already exist", leadStatusDetail.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A LeadStatusDetail with name " + leadStatusDetail.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		leadStatusDetail.setCreatedBy(username);
		leadStatusDetail.setUpdatedBy(username);
		leadStatusDetailService.saveLeadStatusDetail(leadStatusDetail);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/leadStatusDetail/{id}").buildAndExpand(leadStatusDetail.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a LeadStatusDetail
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatusDetail/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLeadStatusDetail(@PathVariable("id") short id, @RequestBody LeadStatusDetail leadStatusDetail,
			@ModelAttribute("username") String username) {
		logger.info("Updating LeadStatusDetail with id {}", id);

		LeadStatusDetail currentLeadStatusDetail = leadStatusDetailService.findById(id);

		if (currentLeadStatusDetail == null) {
			logger.error("Unable to update. LeadStatusDetail with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. LeadStatusDetail with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentLeadStatusDetail.setDescription(leadStatusDetail.getDescription());
		currentLeadStatusDetail.setUpdatedBy(username);
		leadStatusDetailService.updateLeadStatusDetail(currentLeadStatusDetail);
		return new ResponseEntity<LeadStatusDetail>(currentLeadStatusDetail, HttpStatus.OK);
	}

	// ------------------- Delete a
	// LeadStatusDetail-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/leadStatusDetail/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLeadStatusDetail(@PathVariable("id") short id) {
		logger.info("Fetching & Deleting LeadStatusDetail with id {}", id);

		LeadStatusDetail leadStatusDetail = leadStatusDetailService.findById(id);
		if (leadStatusDetail == null) {
			logger.error("Unable to delete. LeadStatusDetail with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. LeadStatusDetail with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		leadStatusDetailService.deleteLeadStatusDetailById(id);
		return new ResponseEntity<LeadStatusDetail>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All LeadStatusDetailes-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/leadStatusDetail/", method = RequestMethod.DELETE)
	public ResponseEntity<LeadStatusDetail> deleteAllLeadStatusDetailes() {
		logger.info("Deleting All LeadStatusDetailes");

		leadStatusDetailService.deleteAllLeadStatusDetails();
		return new ResponseEntity<LeadStatusDetail>(HttpStatus.NO_CONTENT);
	}

}