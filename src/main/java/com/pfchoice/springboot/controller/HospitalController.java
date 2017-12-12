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

import com.pfchoice.springboot.model.Hospital;
import com.pfchoice.springboot.repositories.specifications.HospitalSpecifications;
import com.pfchoice.springboot.service.HospitalService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class HospitalController {

	public static final Logger logger = LoggerFactory.getLogger(HospitalController.class);

	@Autowired
	HospitalService hospitalService; // Service which will do all data
	// retrieval/manipulation work

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	// -------------------Retrieve Hospitals as per page request
	// ---------------------------------------------

	@RequestMapping(value = "/hospital/", method = RequestMethod.GET)
	public ResponseEntity<Page<Hospital>> listAllHospitals(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<Hospital> spec = new HospitalSpecifications(search);
		Page<Hospital> hospitals = hospitalService.findAllHospitalsByPage(spec, pageRequest);

		if (hospitals.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Hospital>>(hospitals, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Hospital------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/hospital/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getHospital(@PathVariable("id") int id) {
		logger.info("Fetching Hospital with id {}", id);
		Hospital hospital = hospitalService.findById(id);
		if (hospital == null) {
			logger.error("Hospital with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Hospital with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Hospital>(hospital, HttpStatus.OK);
	}

	// -------------------Create a
	// Hospital-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/hospital/", method = RequestMethod.POST)
	public ResponseEntity<?> createHospital(@RequestBody Hospital hospital, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating Hospital : {}", hospital);

		if (hospitalService.isHospitalExist(hospital)) {
			logger.error("Unable to create. A Hospital with name {} already exist", hospital.getName());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A Hospital with name " + hospital.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		// logger.info(" hospital.getRoles().size() :{} ",
		// hospital.getRoles().size());
		hospital.setCreatedBy(username);
		hospital.setUpdatedBy(username);
		hospitalService.saveHospital(hospital);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/hospital/{id}").buildAndExpand(hospital.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Hospital
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/hospital/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateHospital(@PathVariable("id") int id, @RequestBody Hospital hospital,
			@ModelAttribute("username") String username) {
		logger.info("Updating Hospital with id {}", id);

		Hospital currentHospital = hospitalService.findById(id);

		if (currentHospital == null) {
			logger.error("Unable to update. Hospital with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Hospital with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentHospital.setName(hospital.getName());
		currentHospital.setCode(hospital.getCode());
		currentHospital.setUpdatedBy(username);
		hospitalService.updateHospital(currentHospital);
		return new ResponseEntity<Hospital>(currentHospital, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Hospital-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/hospital/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteHospital(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Hospital with id {}", id);

		Hospital hospital = hospitalService.findById(id);
		if (hospital == null) {
			logger.error("Unable to delete. Hospital with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Hospital with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		hospitalService.deleteHospitalById(id);
		return new ResponseEntity<Hospital>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Hospitals-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/hospital/", method = RequestMethod.DELETE)
	public ResponseEntity<Hospital> deleteAllHospitals() {
		logger.info("Deleting All Hospitals");

		hospitalService.deleteAllHospitals();
		return new ResponseEntity<Hospital>(HttpStatus.NO_CONTENT);
	}

}