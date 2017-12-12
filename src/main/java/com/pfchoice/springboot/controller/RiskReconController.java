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

import com.pfchoice.springboot.model.RiskRecon;
import com.pfchoice.springboot.service.RiskReconService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RiskReconController {

	public static final Logger logger = LoggerFactory.getLogger(RiskReconController.class);

	@Autowired
	RiskReconService riskReconService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// RiskRecons---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/riskRecon/", method = RequestMethod.GET)
	public ResponseEntity<List<RiskRecon>> listAllRiskRecons() {
		List<RiskRecon> riskRecons = riskReconService.findAllRiskRecons();
		if (riskRecons.isEmpty()) {
			System.out.println("no riskRecons");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<RiskRecon>>(riskRecons, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// RiskRecon------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/riskRecon/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getRiskRecon(@PathVariable("id") int id) {
		logger.info("Fetching RiskRecon with id {}", id);
		RiskRecon riskRecon = riskReconService.findById(id);
		if (riskRecon == null) {
			logger.error("RiskRecon with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("RiskRecon with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RiskRecon>(riskRecon, HttpStatus.OK);
	}

	// -------------------Create a
	// RiskRecon-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/riskRecon/", method = RequestMethod.POST)
	public ResponseEntity<?> createRiskRecon(@RequestBody RiskRecon riskRecon, UriComponentsBuilder ucBuilder) {
		logger.info("Creating RiskRecon : {}", riskRecon);

		if (riskReconService.isRiskReconExist(riskRecon)) {
			logger.error("Unable to create. A RiskRecon with name {} already exist", riskRecon.getName());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A RiskRecon with name " + riskRecon.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		riskRecon.setCreatedBy("sarath");
		riskRecon.setUpdatedBy("sarath");
		riskReconService.saveRiskRecon(riskRecon);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/riskRecon/{id}").buildAndExpand(riskRecon.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a RiskRecon
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/riskRecon/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateRiskRecon(@PathVariable("id") int id, @RequestBody RiskRecon riskRecon) {
		logger.info("Updating RiskRecon with id {}", id);

		RiskRecon currentRiskRecon = riskReconService.findById(id);

		if (currentRiskRecon == null) {
			logger.error("Unable to update. RiskRecon with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. RiskRecon with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentRiskRecon.setName(riskRecon.getName());

		riskReconService.updateRiskRecon(currentRiskRecon);
		return new ResponseEntity<RiskRecon>(currentRiskRecon, HttpStatus.OK);
	}

	// ------------------- Delete a
	// RiskRecon-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/riskRecon/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRiskRecon(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting RiskRecon with id {}", id);

		RiskRecon riskRecon = riskReconService.findById(id);
		if (riskRecon == null) {
			logger.error("Unable to delete. RiskRecon with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. RiskRecon with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		riskReconService.deleteRiskReconById(id);
		return new ResponseEntity<RiskRecon>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All RiskRecons-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/riskRecon/", method = RequestMethod.DELETE)
	public ResponseEntity<RiskRecon> deleteAllRiskRecons() {
		logger.info("Deleting All RiskRecons");

		riskReconService.deleteAllRiskRecons();
		return new ResponseEntity<RiskRecon>(HttpStatus.NO_CONTENT);
	}

}