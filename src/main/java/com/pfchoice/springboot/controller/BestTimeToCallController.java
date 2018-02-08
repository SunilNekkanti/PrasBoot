package com.pfchoice.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pfchoice.springboot.model.BestTimeToCall;
import com.pfchoice.springboot.service.BestTimeToCallService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BestTimeToCallController {

	public static final Logger logger = LoggerFactory.getLogger(BestTimeToCallController.class);

	@Autowired
	BestTimeToCallService bestTimeToCallService; // Service which will do all
													// data
													// retrieval/manipulation
													// work

	// -------------------Retrieve All
	// BestTimeToCalls---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/bestTimeToCall/", method = RequestMethod.GET)
	public ResponseEntity<List<BestTimeToCall>> listAllBestTimeToCalls() {
		List<BestTimeToCall> bestTimeToCalls = bestTimeToCallService.findAllBestTimeToCalls();
		if (bestTimeToCalls.isEmpty()) {
			System.out.println("no bestTimeToCalls");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<BestTimeToCall>>(bestTimeToCalls, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// BestTimeToCall------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/bestTimeToCall/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getBestTimeToCall(@PathVariable("id") short id) {
		logger.info("Fetching BestTimeToCall with id {}", id);
		BestTimeToCall bestTimeToCall = bestTimeToCallService.findById(id);
		if (bestTimeToCall == null) {
			logger.error("BestTimeToCall with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("BestTimeToCall with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BestTimeToCall>(bestTimeToCall, HttpStatus.OK);
	}

	// ------------------- Delete a
	// BestTimeToCall-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/bestTimeToCall/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBestTimeToCall(@PathVariable("id") short id) {
		logger.info("Fetching & Deleting BestTimeToCall with id {}", id);

		BestTimeToCall bestTimeToCall = bestTimeToCallService.findById(id);
		if (bestTimeToCall == null) {
			logger.error("Unable to delete. BestTimeToCall with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. BestTimeToCall with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		bestTimeToCallService.deleteBestTimeToCallById(id);
		return new ResponseEntity<BestTimeToCall>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// BestTimeToCalls-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/bestTimeToCall/", method = RequestMethod.DELETE)
	public ResponseEntity<BestTimeToCall> deleteAllBestTimeToCalls() {
		logger.info("Deleting All BestTimeToCalls");

		bestTimeToCallService.deleteAllBestTimeToCalls();
		return new ResponseEntity<BestTimeToCall>(HttpStatus.NO_CONTENT);
	}

}