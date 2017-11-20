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

import com.pfchoice.springboot.model.ICDMeasure;
import com.pfchoice.springboot.repositories.specifications.ICDMeasureSpecifications;
import com.pfchoice.springboot.service.ICDMeasureService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ICDMeasureController {

	public static final Logger logger = LoggerFactory.getLogger(ICDMeasureController.class);

	@Autowired
	ICDMeasureService icdMeasureService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// ICDMeasures---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/icdMeasure/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllICDMeasures(@PageableDefault(page=0 ,size=20) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<ICDMeasure> spec = new ICDMeasureSpecifications( search);
		Page<ICDMeasure> icdMeasures = icdMeasureService.findAllICDMeasuresByPage(spec, pageRequest);
		
		if (icdMeasures.getTotalElements() == 0) {
			System.out.println("no icdMeasures");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<ICDMeasure>>(icdMeasures, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// ICDMeasure------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/icdMeasure/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getICDMeasure(@PathVariable("id") int id) {
		logger.info("Fetching ICDMeasure with id {}", id);
		ICDMeasure icdMeasure = icdMeasureService.findById(id);
		if (icdMeasure == null) {
			logger.error("ICDMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("ICDMeasure with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ICDMeasure>(icdMeasure, HttpStatus.OK);
	}

	// -------------------Create a
	// ICDMeasure-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/icdMeasure/", method = RequestMethod.POST)
	public ResponseEntity<?> createICDMeasure(@RequestBody ICDMeasure icdMeasure, UriComponentsBuilder ucBuilder) {
		logger.info("Creating ICDMeasure : {}", icdMeasure);

		if (icdMeasureService.isICDMeasureExist(icdMeasure)) {
			logger.error("Unable to create. A ICDMeasure with name {} already exist", icdMeasure.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A ICDMeasure with name " + icdMeasure.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		icdMeasure.setCreatedBy("sarath");
		icdMeasure.setUpdatedBy("sarath");
		icdMeasureService.saveICDMeasure(icdMeasure);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/icdMeasure/{id}").buildAndExpand(icdMeasure.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a ICDMeasure
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/icdMeasure/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateICDMeasure(@PathVariable("id") int id, @RequestBody ICDMeasure icdMeasure) {
		logger.info("Updating ICDMeasure with id {}", id);

		ICDMeasure currentICDMeasure = icdMeasureService.findById(id);

		if (currentICDMeasure == null) {
			logger.error("Unable to update. ICDMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. ICDMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentICDMeasure.setCode(icdMeasure.getCode());
		currentICDMeasure.setDescription(icdMeasure.getDescription());

		icdMeasureService.updateICDMeasure(currentICDMeasure);
		return new ResponseEntity<ICDMeasure>(currentICDMeasure, HttpStatus.OK);
	}

	// ------------------- Delete a
	// ICDMeasure-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/icdMeasure/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteICDMeasure(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting ICDMeasure with id {}", id);

		ICDMeasure icdMeasure = icdMeasureService.findById(id);
		if (icdMeasure == null) {
			logger.error("Unable to delete. ICDMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. ICDMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		icdMeasureService.deleteICDMeasureById(id);
		return new ResponseEntity<ICDMeasure>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All ICDMeasures-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/icdMeasure/", method = RequestMethod.DELETE)
	public ResponseEntity<ICDMeasure> deleteAllICDMeasures() {
		logger.info("Deleting All ICDMeasures");

		icdMeasureService.deleteAllICDMeasures();
		return new ResponseEntity<ICDMeasure>(HttpStatus.NO_CONTENT);
	}

}