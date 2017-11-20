package com.pfchoice.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.County;
import com.pfchoice.springboot.service.CountyService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CountyController {

	public static final Logger logger = LoggerFactory.getLogger(CountyController.class);

	@Autowired
	CountyService countyService; // Service which will do all data
									// retrieval/manipulation work

	// -------------------Retrieve All
	// Countys---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/county/", method = RequestMethod.GET)
	public ResponseEntity<List<County>> listAllCountys() {
		List<County> countys = countyService.findAllCountys();
		if (countys.isEmpty()) {
			System.out.println("no county");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<County>>(countys, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// County------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/county/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCounty(@PathVariable("id") int id) {
		logger.info("Fetching County with id {}", id);
		County county = countyService.findById(id);
		if (county == null) {
			logger.error("County with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("County with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<County>(county, HttpStatus.OK);
	}

	// -------------------Create a
	// County-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/county/", method = RequestMethod.POST)
	public ResponseEntity<?> createCounty(@RequestBody County county, UriComponentsBuilder ucBuilder) {
		logger.info("Creating County : {}", county);

		if (countyService.isCountyExist(county)) {
			logger.error("Unable to create. A County with name {} already exist", county.getCode());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A County with name " + county.getCode() + " already exist."),
					HttpStatus.CONFLICT);
		}
		county.setCreatedBy("sarath");
		county.setUpdatedBy("sarath");
		countyService.saveCounty(county);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/county/{id}").buildAndExpand(county.getCode()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a County
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/county/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCounty(@PathVariable("id") int id, @RequestBody County county) {
		logger.info("Updating County with id {}", id);

		County currentCounty = countyService.findById(id);

		if (currentCounty == null) {
			logger.error("Unable to update. County with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. County with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentCounty.setCode(county.getCode());
		currentCounty.setDescription(county.getDescription());

		countyService.updateCounty(currentCounty);
		return new ResponseEntity<County>(currentCounty, HttpStatus.OK);
	}

	// ------------------- Delete a
	// County-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/county/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCounty(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting County with id {}", id);

		County county = countyService.findById(id);
		if (county == null) {
			logger.error("Unable to delete. County with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. County with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		countyService.deleteCountyById(id);
		return new ResponseEntity<County>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Countys-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/county/", method = RequestMethod.DELETE)
	public ResponseEntity<County> deleteAllCountys() {
		logger.info("Deleting All Countys");

		countyService.deleteAllCountys();
		return new ResponseEntity<County>(HttpStatus.NO_CONTENT);
	}

}