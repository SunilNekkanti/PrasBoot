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

import com.pfchoice.springboot.model.Insurance;
import com.pfchoice.springboot.repositories.specifications.InsuranceSpecifications;
import com.pfchoice.springboot.service.InsuranceService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class InsuranceController {

	public static final Logger logger = LoggerFactory.getLogger(InsuranceController.class);

	@Autowired
	InsuranceService insuranceService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// Insurances---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/insurance/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllInsurances(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "currentScreen", required = false) String currentScreen,
			@RequestParam(value = "search", required = false) String search) {

		Specification<Insurance> spec = new InsuranceSpecifications(search,currentScreen);
		Page<Insurance> insurances = insuranceService.findAllInsurancesByPage(spec, pageRequest);

		if (insurances.getTotalElements() == 0) {
			System.out.println("no insurances");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Insurance>>(insurances, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Insurance------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/insurance/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getInsurance(@PathVariable("id") int id) {
		logger.info("Fetching Insurance with id {}", id);
		Insurance insurance = insuranceService.findById(id);
		if (insurance == null) {
			logger.error("Insurance with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Insurance with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Insurance>(insurance, HttpStatus.OK);
	}

	// -------------------Create a
	// Insurance-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/insurance/", method = RequestMethod.POST)
	public ResponseEntity<?> createInsurance(@RequestBody Insurance insurance, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Insurance : {}", insurance);

		if (insuranceService.isInsuranceExist(insurance)) {
			logger.error("Unable to create. A Insurance with name {} already exist", insurance.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A Insurance with name " + insurance.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		insurance.setCreatedBy("sarath");
		insurance.setUpdatedBy("sarath");
		insuranceService.saveInsurance(insurance);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/insurance/{id}").buildAndExpand(insurance.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Insurance
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/insurance/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateInsurance(@PathVariable("id") int id, @RequestBody Insurance insurance) {
		logger.info("Updating Insurance with id {}", id);

		Insurance currentInsurance = insuranceService.findById(id);

		if (currentInsurance == null) {
			logger.error("Unable to update. Insurance with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Insurance with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentInsurance.setName(insurance.getName());
		currentInsurance.setContact(insurance.getContact());
		currentInsurance.setPlanType(insurance.getPlanType());
		currentInsurance.getContracts().clear();
		currentInsurance.setContracts(insurance.getContracts());

		insuranceService.updateInsurance(currentInsurance);
		return new ResponseEntity<Insurance>(currentInsurance, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Insurance-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/insurance/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteInsurance(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Insurance with id {}", id);

		Insurance insurance = insuranceService.findById(id);
		if (insurance == null) {
			logger.error("Unable to delete. Insurance with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Insurance with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		insuranceService.deleteInsuranceById(id);
		return new ResponseEntity<Insurance>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Insurances-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/insurance/", method = RequestMethod.DELETE)
	public ResponseEntity<Insurance> deleteAllInsurances() {
		logger.info("Deleting All Insurances");

		insuranceService.deleteAllInsurances();
		return new ResponseEntity<Insurance>(HttpStatus.NO_CONTENT);
	}

}