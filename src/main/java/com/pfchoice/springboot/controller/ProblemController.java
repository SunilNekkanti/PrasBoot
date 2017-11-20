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

import com.pfchoice.springboot.model.Problem;
import com.pfchoice.springboot.repositories.specifications.ProblemSpecifications;
import com.pfchoice.springboot.service.ProblemService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProblemController {

	public static final Logger logger = LoggerFactory.getLogger(ProblemController.class);

	@Autowired
	ProblemService problemService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// Problems---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/problem/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllProblems(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "insId", required = false) Integer insId,
			@RequestParam(value = "effectiveYear", required = false) Integer effectiveYear) {

		Specification<Problem> spec = new ProblemSpecifications(search, insId, effectiveYear);
		Page<Problem> problems = problemService.findAllProblemsByPage(spec, pageRequest);
		
		if (problems.getTotalElements() == 0) {
			System.out.println("no problems");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Problem>>(problems, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Problem------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/problem/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProblem(@PathVariable("id") int id) {
		logger.info("Fetching Problem with id {}", id);
		Problem problem = problemService.findById(id);
		if (problem == null) {
			logger.error("Problem with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Problem with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Problem>(problem, HttpStatus.OK);
	}

	// -------------------Create a
	// Problem-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/problem/", method = RequestMethod.POST)
	public ResponseEntity<?> createProblem(@RequestBody Problem problem, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Problem : {}", problem);

		if (problemService.isProblemExist(problem)) {
			logger.error("Unable to create. A Problem with name {} already exist", problem.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A Problem with name " + problem.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		problem.setCreatedBy("sarath");
		problem.setUpdatedBy("sarath");
		problemService.saveProblem(problem);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/problem/{id}").buildAndExpand(problem.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Problem
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/problem/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProblem(@PathVariable("id") int id, @RequestBody Problem problem) {
		logger.info("Updating Problem with id {}", id);

		Problem currentProblem = problemService.findById(id);

		if (currentProblem == null) {
			logger.error("Unable to update. Problem with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Problem with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentProblem.setDescription(problem.getDescription());
		currentProblem.setEffectiveYear(problem.getEffectiveYear());
		currentProblem.setInsId(problem.getInsId());
		currentProblem.getIcdCodes().clear();
		currentProblem.getIcdCodes().addAll(problem.getIcdCodes());

		problemService.updateProblem(currentProblem);
		return new ResponseEntity<Problem>(currentProblem, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Problem-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/problem/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProblem(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Problem with id {}", id);

		Problem problem = problemService.findById(id);
		if (problem == null) {
			logger.error("Unable to delete. Problem with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Problem with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		problemService.deleteProblemById(id);
		return new ResponseEntity<Problem>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Problems-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/problem/", method = RequestMethod.DELETE)
	public ResponseEntity<Problem> deleteAllProblems() {
		logger.info("Deleting All Problems");

		problemService.deleteAllProblems();
		return new ResponseEntity<Problem>(HttpStatus.NO_CONTENT);
	}

}