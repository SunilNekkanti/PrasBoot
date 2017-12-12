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

import com.pfchoice.springboot.model.HedisMeasureRule;
import com.pfchoice.springboot.repositories.specifications.HedisMeasureRuleSpecifications;
import com.pfchoice.springboot.service.HedisMeasureRuleService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class HedisMeasureRuleController {

	public static final Logger logger = LoggerFactory.getLogger(HedisMeasureRuleController.class);

	@Autowired
	HedisMeasureRuleService hedisMeasureRuleService; // Service which will do
														// all data
	// retrieval/manipulation work

	// -------------------Retrieve All
	// Problems---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureRule/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllProblems(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "insId", required = false) Integer insId,
			@RequestParam(value = "effectiveYear", required = false) Integer effectiveYear) {

		Specification<HedisMeasureRule> spec = new HedisMeasureRuleSpecifications(search, insId, effectiveYear);
		Page<HedisMeasureRule> hedisMeasureRules = hedisMeasureRuleService.findAllHedisMeasureRulesByPage(spec,
				pageRequest);

		if (hedisMeasureRules.getTotalElements() == 0) {
			System.out.println("no hedisMeasureRules");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<HedisMeasureRule>>(hedisMeasureRules, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// HedisMeasureRule------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureRule/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProblem(@PathVariable("id") int id) {
		logger.info("Fetching HedisMeasureRule with id {}", id);
		HedisMeasureRule hedisMeasureRule = hedisMeasureRuleService.findById(id);
		if (hedisMeasureRule == null) {
			logger.error("HedisMeasureRule with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("HedisMeasureRule with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<HedisMeasureRule>(hedisMeasureRule, HttpStatus.OK);
	}

	// -------------------Create a
	// HedisMeasureRule-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureRule/", method = RequestMethod.POST)
	public ResponseEntity<?> createProblem(@RequestBody HedisMeasureRule hedisMeasureRule,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating HedisMeasureRule : {}", hedisMeasureRule);

		if (hedisMeasureRuleService.isHedisMeasureRuleExist(hedisMeasureRule)) {
			logger.error("Unable to create. A HedisMeasureRule with name {} already exist", hedisMeasureRule.getId());
			return new ResponseEntity(new CustomErrorType(
					"Unable to create. A HedisMeasureRule with name " + hedisMeasureRule.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		hedisMeasureRule.setCreatedBy("sarath");
		hedisMeasureRule.setUpdatedBy("sarath");
		hedisMeasureRuleService.saveHedisMeasureRule(hedisMeasureRule);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/hedisMeasureRule/{id}").buildAndExpand(hedisMeasureRule.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a HedisMeasureRule
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureRule/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProblem(@PathVariable("id") int id, @RequestBody HedisMeasureRule hedisMeasureRule) {
		logger.info("Updating HedisMeasureRule with id {}", id);

		HedisMeasureRule currentProblem = hedisMeasureRuleService.findById(id);

		if (currentProblem == null) {
			logger.error("Unable to update. HedisMeasureRule with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. HedisMeasureRule with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentProblem.setDescription(hedisMeasureRule.getDescription());
		currentProblem.setEffectiveYear(hedisMeasureRule.getEffectiveYear());
		currentProblem.setInsId(hedisMeasureRule.getInsId());
		currentProblem.getIcdCodes().clear();
		currentProblem.getIcdCodes().addAll(hedisMeasureRule.getIcdCodes());

		hedisMeasureRuleService.updateHedisMeasureRule(currentProblem);
		return new ResponseEntity<HedisMeasureRule>(currentProblem, HttpStatus.OK);
	}

	// ------------------- Delete a
	// HedisMeasureRule-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisMeasureRule/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProblem(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting HedisMeasureRule with id {}", id);

		HedisMeasureRule hedisMeasureRule = hedisMeasureRuleService.findById(id);
		if (hedisMeasureRule == null) {
			logger.error("Unable to delete. HedisMeasureRule with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. HedisMeasureRule with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		hedisMeasureRuleService.deleteHedisMeasureRuleById(id);
		return new ResponseEntity<HedisMeasureRule>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Problems-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/hedisMeasureRule/", method = RequestMethod.DELETE)
	public ResponseEntity<HedisMeasureRule> deleteAllProblems() {
		logger.info("Deleting All Problems");

		hedisMeasureRuleService.deleteAllHedisMeasureRules();
		return new ResponseEntity<HedisMeasureRule>(HttpStatus.NO_CONTENT);
	}

}