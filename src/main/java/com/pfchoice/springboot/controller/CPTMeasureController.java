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

import com.pfchoice.springboot.model.CPTMeasure;
import com.pfchoice.springboot.repositories.specifications.CPTMeasureSpecifications;
import com.pfchoice.springboot.service.CPTMeasureService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CPTMeasureController {

	public static final Logger logger = LoggerFactory.getLogger(CPTMeasureController.class);

	@Autowired
	CPTMeasureService cptMeasureService; // Service which will do all data
											// retrieval/manipulation work

	// -------------------Retrieve All
	// CPTMeasures---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/cptMeasure/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllCPTMeasures(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<CPTMeasure> spec = new CPTMeasureSpecifications(search);
		Page<CPTMeasure> cptMeasures = cptMeasureService.findAllCPTMeasuresByPage(spec, pageRequest);

		if (cptMeasures.getTotalElements() == 0) {
			System.out.println("no cptMeasures");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<CPTMeasure>>(cptMeasures, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// CPTMeasure------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/cptMeasure/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCPTMeasure(@PathVariable("id") int id) {
		logger.info("Fetching CPTMeasure with id {}", id);
		CPTMeasure cptMeasure = cptMeasureService.findById(id);
		if (cptMeasure == null) {
			logger.error("CPTMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("CPTMeasure with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<CPTMeasure>(cptMeasure, HttpStatus.OK);
	}

	// -------------------Create a
	// CPTMeasure-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/cptMeasure/", method = RequestMethod.POST)
	public ResponseEntity<?> createCPTMeasure(@RequestBody CPTMeasure cptMeasure, UriComponentsBuilder ucBuilder) {
		logger.info("Creating CPTMeasure : {}", cptMeasure);

		if (cptMeasureService.isCPTMeasureExist(cptMeasure)) {
			logger.error("Unable to create. A CPTMeasure with name {} already exist", cptMeasure.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A CPTMeasure with name " + cptMeasure.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		cptMeasure.setCreatedBy("sarath");
		cptMeasure.setUpdatedBy("sarath");
		cptMeasureService.saveCPTMeasure(cptMeasure);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/cptMeasure/{id}").buildAndExpand(cptMeasure.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a CPTMeasure
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/cptMeasure/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCPTMeasure(@PathVariable("id") int id, @RequestBody CPTMeasure cptMeasure) {
		logger.info("Updating CPTMeasure with id {}", id);

		CPTMeasure currentCPTMeasure = cptMeasureService.findById(id);

		if (currentCPTMeasure == null) {
			logger.error("Unable to update. CPTMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. CPTMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentCPTMeasure.setCode(cptMeasure.getCode());
		currentCPTMeasure.setDescription(cptMeasure.getDescription());

		cptMeasureService.updateCPTMeasure(currentCPTMeasure);
		return new ResponseEntity<CPTMeasure>(currentCPTMeasure, HttpStatus.OK);
	}

	// ------------------- Delete a
	// CPTMeasure-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/cptMeasure/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCPTMeasure(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting CPTMeasure with id {}", id);

		CPTMeasure cptMeasure = cptMeasureService.findById(id);
		if (cptMeasure == null) {
			logger.error("Unable to delete. CPTMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. CPTMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		cptMeasureService.deleteCPTMeasureById(id);
		return new ResponseEntity<CPTMeasure>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All CPTMeasures-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/cptMeasure/", method = RequestMethod.DELETE)
	public ResponseEntity<CPTMeasure> deleteAllCPTMeasures() {
		logger.info("Deleting All CPTMeasures");

		cptMeasureService.deleteAllCPTMeasures();
		return new ResponseEntity<CPTMeasure>(HttpStatus.NO_CONTENT);
	}

}