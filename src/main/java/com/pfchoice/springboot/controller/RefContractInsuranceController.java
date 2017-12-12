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

import com.pfchoice.springboot.model.RefContractInsurance;
import com.pfchoice.springboot.repositories.specifications.RefContractInsuranceSpecifications;
import com.pfchoice.springboot.service.RefContractInsuranceService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RefContractInsuranceController {

	public static final Logger logger = LoggerFactory.getLogger(RefContractInsuranceController.class);

	@Autowired
	RefContractInsuranceService refContractInsuranceService; // Service which
																// will do all
																// data
	// retrieval/manipulation work

	// -------------------Retrieve All
	// Insurances---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/refContractInsurance/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllInsurances(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<RefContractInsurance> spec = new RefContractInsuranceSpecifications(search);
		Page<RefContractInsurance> insurances = refContractInsuranceService.findAllRefContractInsurancesByPage(spec,
				pageRequest);

		if (insurances.getTotalElements() == 0) {
			System.out.println("no insurances");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<RefContractInsurance>>(insurances, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// RefContractInsurance------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/refContractInsurance/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getInsurance(@PathVariable("id") int id) {
		logger.info("Fetching RefContractInsurance with id {}", id);
		RefContractInsurance refContractInsurance = refContractInsuranceService.findById(id);
		if (refContractInsurance == null) {
			logger.error("RefContractInsurance with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("RefContractInsurance with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RefContractInsurance>(refContractInsurance, HttpStatus.OK);
	}

	// -------------------Create a
	// RefContractInsurance-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/refContractInsurance/", method = RequestMethod.POST)
	public ResponseEntity<?> createInsurance(@RequestBody RefContractInsurance refContractInsurance,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating RefContractInsurance : {}", refContractInsurance);

		if (refContractInsuranceService.isRefContractInsuranceExist(refContractInsurance)) {
			logger.error("Unable to create. A RefContractInsurance with name {} already exist",
					refContractInsurance.getId());
			return new ResponseEntity(new CustomErrorType("Unable to create. A RefContractInsurance with name "
					+ refContractInsurance.getId() + " already exist."), HttpStatus.CONFLICT);
		}
		refContractInsurance.setCreatedBy("sarath");
		refContractInsurance.setUpdatedBy("sarath");
		refContractInsuranceService.saveRefContractInsurance(refContractInsurance);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/refContractInsurance/{id}").buildAndExpand(refContractInsurance.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a RefContractInsurance
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/refContractInsurance/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateInsurance(@PathVariable("id") int id,
			@RequestBody RefContractInsurance refContractInsurance) {
		logger.info("Updating RefContractInsurance with id {}", id);

		RefContractInsurance currentInsurance = refContractInsuranceService.findById(id);

		if (currentInsurance == null) {
			logger.error("Unable to update. RefContractInsurance with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. RefContractInsurance with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentInsurance.setName(refContractInsurance.getName());
		currentInsurance.setPlanType(refContractInsurance.getPlanType());

		refContractInsuranceService.updateRefContractInsurance(currentInsurance);
		return new ResponseEntity<RefContractInsurance>(currentInsurance, HttpStatus.OK);
	}

	// ------------------- Delete a
	// RefContractInsurance-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/refContractInsurance/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteInsurance(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting RefContractInsurance with id {}", id);

		RefContractInsurance refContractInsurance = refContractInsuranceService.findById(id);
		if (refContractInsurance == null) {
			logger.error("Unable to delete. RefContractInsurance with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. RefContractInsurance with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		refContractInsuranceService.deleteRefContractInsuranceById(id);
		return new ResponseEntity<RefContractInsurance>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Insurances-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/refContractInsurance/", method = RequestMethod.DELETE)
	public ResponseEntity<RefContractInsurance> deleteAllInsurances() {
		logger.info("Deleting All Insurances");

		refContractInsuranceService.deleteAllRefContractInsurances();
		return new ResponseEntity<RefContractInsurance>(HttpStatus.NO_CONTENT);
	}

}