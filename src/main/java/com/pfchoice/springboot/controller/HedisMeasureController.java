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

import com.pfchoice.springboot.model.HedisMeasure;
import com.pfchoice.springboot.repositories.specifications.HedisMeasureSpecifications;
import com.pfchoice.springboot.service.HedisMeasureService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class HedisMeasureController {

	public static final Logger logger = LoggerFactory.getLogger(HedisMeasureController.class);

	@Autowired
	HedisMeasureService hedisMeasureService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// HedisMeasures---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasure/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllHedisMeasures(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<HedisMeasure> spec = new HedisMeasureSpecifications( search);
		Page<HedisMeasure> hedisMeasures = hedisMeasureService.findAllHedisMeasuresByPage(spec, pageRequest);
		
		if (hedisMeasures.getTotalElements() == 0) {
			System.out.println("no hedisMeasures");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<HedisMeasure>>(hedisMeasures, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// HedisMeasure------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasure/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getHedisMeasure(@PathVariable("id") int id) {
		logger.info("Fetching HedisMeasure with id {}", id);
		HedisMeasure hedisMeasure = hedisMeasureService.findById(id);
		if (hedisMeasure == null) {
			logger.error("HedisMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("HedisMeasure with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<HedisMeasure>(hedisMeasure, HttpStatus.OK);
	}

	// -------------------Create a
	// HedisMeasure-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasure/", method = RequestMethod.POST)
	public ResponseEntity<?> createHedisMeasure(@RequestBody HedisMeasure hedisMeasure, UriComponentsBuilder ucBuilder) {
		logger.info("Creating HedisMeasure : {}", hedisMeasure);

		if (hedisMeasureService.isHedisMeasureExist(hedisMeasure)) {
			logger.error("Unable to create. A HedisMeasure with name {} already exist", hedisMeasure.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A HedisMeasure with name " + hedisMeasure.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		hedisMeasure.setCreatedBy("sarath");
		hedisMeasure.setUpdatedBy("sarath");
		hedisMeasureService.saveHedisMeasure(hedisMeasure);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/hedisMeasure/{id}").buildAndExpand(hedisMeasure.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a HedisMeasure
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasure/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateHedisMeasure(@PathVariable("id") int id, @RequestBody HedisMeasure hedisMeasure) {
		logger.info("Updating HedisMeasure with id {}", id);

		HedisMeasure currentHedisMeasure = hedisMeasureService.findById(id);

		if (currentHedisMeasure == null) {
			logger.error("Unable to update. HedisMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. HedisMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentHedisMeasure.setCode(hedisMeasure.getCode());
		currentHedisMeasure.setDescription(hedisMeasure.getDescription());

		hedisMeasureService.updateHedisMeasure(currentHedisMeasure);
		return new ResponseEntity<HedisMeasure>(currentHedisMeasure, HttpStatus.OK);
	}

	// ------------------- Delete a
	// HedisMeasure-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasure/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteHedisMeasure(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting HedisMeasure with id {}", id);

		HedisMeasure hedisMeasure = hedisMeasureService.findById(id);
		if (hedisMeasure == null) {
			logger.error("Unable to delete. HedisMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. HedisMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		hedisMeasureService.deleteHedisMeasureById(id);
		return new ResponseEntity<HedisMeasure>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All HedisMeasures-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/hedisMeasure/", method = RequestMethod.DELETE)
	public ResponseEntity<HedisMeasure> deleteAllHedisMeasures() {
		logger.info("Deleting All HedisMeasures");

		hedisMeasureService.deleteAllHedisMeasures();
		return new ResponseEntity<HedisMeasure>(HttpStatus.NO_CONTENT);
	}

}