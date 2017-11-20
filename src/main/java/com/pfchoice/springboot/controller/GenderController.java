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

import com.pfchoice.springboot.model.Gender;
import com.pfchoice.springboot.service.GenderService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class GenderController {

	public static final Logger logger = LoggerFactory.getLogger(GenderController.class);

	@Autowired
	GenderService genderService; // Service which will do all data
									// retrieval/manipulation work

	// -------------------Retrieve All
	// Genders---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/gender/", method = RequestMethod.GET)
	public ResponseEntity<List<Gender>> listAllGenders() {
		List<Gender> genders = genderService.findAllGenders();
		if (genders.isEmpty()) {
			System.out.println("no genders");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Gender>>(genders, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Gender------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/gender/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getGender(@PathVariable("id") byte id) {
		logger.info("Fetching Gender with id {}", id);
		Gender gender = genderService.findById(id);
		if (gender == null) {
			logger.error("Gender with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Gender with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Gender>(gender, HttpStatus.OK);
	}

	// -------------------Create a
	// Gender-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/gender/", method = RequestMethod.POST)
	public ResponseEntity<?> createGender(@RequestBody Gender gender, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Gender : {}", gender);

		if (genderService.isGenderExist(gender)) {
			logger.error("Unable to create. A Gender with name {} already exist", gender.getCode());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Gender with name " + gender.getCode() + " already exist."),
					HttpStatus.CONFLICT);
		}
		gender.setCreatedBy("sarath");
		gender.setUpdatedBy("sarath");
		genderService.saveGender(gender);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/gender/{id}").buildAndExpand(gender.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Gender
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/gender/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateGender(@PathVariable("id") byte id, @RequestBody Gender gender) {
		logger.info("Updating Gender with id {}", id);

		Gender currentGender = genderService.findById(id);

		if (currentGender == null) {
			logger.error("Unable to update. Gender with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Gender with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentGender.setCode(gender.getCode());
		currentGender.setDescription(gender.getDescription());

		genderService.updateGender(currentGender);
		return new ResponseEntity<Gender>(currentGender, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Gender-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/gender/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGender(@PathVariable("id") byte id) {
		logger.info("Fetching & Deleting Gender with id {}", id);

		Gender gender = genderService.findById(id);
		if (gender == null) {
			logger.error("Unable to delete. Gender with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Gender with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		genderService.deleteGenderById(id);
		return new ResponseEntity<Gender>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Genders-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/gender/", method = RequestMethod.DELETE)
	public ResponseEntity<Gender> deleteAllGenders() {
		logger.info("Deleting All Genders");

		genderService.deleteAllGenders();
		return new ResponseEntity<Gender>(HttpStatus.NO_CONTENT);
	}

}