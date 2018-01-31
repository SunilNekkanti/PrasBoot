package com.pfchoice.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.ICDMeasure;
import com.pfchoice.springboot.model.Membership;
import com.pfchoice.springboot.model.MembershipProblem;
import com.pfchoice.springboot.service.ICDMeasureService;
import com.pfchoice.springboot.service.MembershipProblemService;
import com.pfchoice.springboot.service.MembershipService;
import com.pfchoice.springboot.util.CustomErrorType;
import com.pfchoice.springboot.util.PrasUtil;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class MembershipProblemController {

	public static final Logger logger = LoggerFactory.getLogger(MembershipProblemController.class);

	@Autowired
	MembershipProblemService membershipProblemService; // Service which will do all  retrieval/manipulation work
					
	@Autowired
	MembershipService membershipService; // Service which will do all data  retrieval/manipulation work

	@Autowired
	ICDMeasureService icdMeasureService; // Service which will do all data  retrieval/manipulation work

	// data
	// retrieval/manipulation work
	@Autowired
	PrasUtil prasUtil;

	// -------------------Retrieve All
	// MembershipProblems---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipProblem/", method = RequestMethod.GET)
	public ResponseEntity<List<MembershipProblem>> listAllMembershipProblems() {
		List<MembershipProblem> membershipProblems = membershipProblemService.findAllMembershipProblems();
		if (membershipProblems.isEmpty()) {
			System.out.println("no membershipProblems");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<MembershipProblem>>(membershipProblems, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// MembershipProblem------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipProblem/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMembershipProblem(@PathVariable("id") int id) {
		logger.info("Fetching MembershipProblem with id {}", id);
		MembershipProblem membershipProblem = membershipProblemService.findById(id);
		if (membershipProblem == null) {
			logger.error("MembershipProblem with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("MembershipProblem with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MembershipProblem>(membershipProblem, HttpStatus.OK);
	}

/*	// -------------------Create a
	// MembershipProblem-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipProblem/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembershipProblem(@RequestBody MembershipProblem membershipProblem,
			@ModelAttribute("username") String username,	UriComponentsBuilder ucBuilder) {
		logger.info("Creating MembershipProblem : {}", membershipProblem);

		if (membershipProblemService.isMembershipProblemExist(membershipProblem)) {
			logger.error("Unable to create. A MembershipProblem with name {} already exist",
					membershipProblem.getICDMeasure());
			return new ResponseEntity(new CustomErrorType("Unable to create. A MembershipProblem with ICD "
					+ membershipProblem.getICDMeasure() + " already exist."), HttpStatus.CONFLICT);
		}
		membershipProblem.setCreatedBy(username);
		membershipProblem.setUpdatedBy(username);
		membershipProblemService.saveMembershipProblem(membershipProblem);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/membershipProblem/{id}").buildAndExpand(membershipProblem.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
*/
	// -------------------Create a
	// MembershipProblem-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipProblem/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembershipProblem(@RequestBody List<MembershipProblem> membershipProblems,
			@RequestBody Integer membershipId,
			@ModelAttribute("username") String username,	UriComponentsBuilder ucBuilder) {
		logger.info("Creating membershipId : {}",membershipId);
		logger.info("Creating MembershipProblems : {}", membershipProblems);

		Membership currentMembership = membershipService.findById(membershipId);
		
		  for(MembershipProblem membershipProblem : membershipProblems){
			  
			  if (membershipProblemService.isMembershipProblemExist(membershipProblem ,membershipId)) {
					logger.error("Unable to create. A MembershipProblem with name {} already exist",
							membershipProblem.getIcdMeasure());
					return new ResponseEntity(new CustomErrorType("Unable to create. A MembershipProblem with ICD "
							+ membershipProblem.getIcdMeasure() + " already exist."), HttpStatus.CONFLICT);
				}
			   ICDMeasure icdMeasure = icdMeasureService.findByCode(membershipProblem.getIcdMeasure().getCode());
			   membershipProblem.setIcdMeasure(icdMeasure);
		    	membershipProblem.setMbr(currentMembership);
				membershipProblem.setCreatedBy(username);
				membershipProblem.setUpdatedBy(username);
				membershipProblemService.saveMembershipProblem(membershipProblem);

		  }
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/membership/{membershipId}").buildAndExpand(currentMembership.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	// ------------------- Update a MembershipProblem
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipProblem/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMembershipProblem(@PathVariable("id") int id,
			@RequestBody MembershipProblem membershipProblem,	@ModelAttribute("username") String username) {
		logger.info("Updating MembershipProblem with id {}", id);

		MembershipProblem currentMembershipProblem = membershipProblemService.findById(id);

		if (currentMembershipProblem == null) {
			logger.error("Unable to update. MembershipProblem with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. MembershipProblem with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMembershipProblem.setIcdMeasure(membershipProblem.getIcdMeasure());
		currentMembershipProblem.setUpdatedBy(username);
		membershipProblemService.updateMembershipProblem(currentMembershipProblem);
		return new ResponseEntity<MembershipProblem>(currentMembershipProblem, HttpStatus.OK);
	}

	// ------------------- Delete a
	// MembershipProblem-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipProblem/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMembershipProblem(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting MembershipProblem with id {}", id);

		MembershipProblem membershipProblem = membershipProblemService.findById(id);
		if (membershipProblem == null) {
			logger.error("Unable to delete. MembershipProblem with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. MembershipProblem with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		membershipProblemService.deleteMembershipProblemById(id);
		return new ResponseEntity<MembershipProblem>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// MembershipProblems-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipProblem/", method = RequestMethod.DELETE)
	public ResponseEntity<MembershipProblem> deleteAllMembershipProblems() {
		logger.info("Deleting All MembershipProblems");

		membershipProblemService.deleteAllMembershipProblems();
		return new ResponseEntity<MembershipProblem>(HttpStatus.NO_CONTENT);
	}


}