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

import com.pfchoice.springboot.model.MembershipFollowup;
import com.pfchoice.springboot.service.MembershipFollowupService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class MembershipFollowupController {

	public static final Logger logger = LoggerFactory.getLogger(MembershipFollowupController.class);

	@Autowired
	MembershipFollowupService membershipFollowupService; // Service which will
															// do all data
	// retrieval/manipulation work

	// -------------------Retrieve All
	// MembershipFollowups---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipFollowup/", method = RequestMethod.GET)
	public ResponseEntity<List<MembershipFollowup>> listAllMembershipFollowups() {
		List<MembershipFollowup> membershipFollowups = membershipFollowupService.findAllMembershipFollowups();
		if (membershipFollowups.isEmpty()) {
			System.out.println("no membershipFollowups");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<MembershipFollowup>>(membershipFollowups, HttpStatus.OK);
	}

	// -------------------Retrieve All
	// MembershipFollowups---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipFollowupDetails/{mbrId}", method = RequestMethod.GET)
	public ResponseEntity<List<MembershipFollowup>> listAllMembershipFollowupsPerMbrId(
			@PathVariable("mbrId") int mbrId) {
		List<MembershipFollowup> membershipFollowups = membershipFollowupService
				.findAllMembershipFollowupsByMbrId(mbrId);
		if (membershipFollowups.isEmpty()) {
			System.out.println("no membershipFollowups");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<MembershipFollowup>>(membershipFollowups, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// MembershipFollowup------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipFollowup/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMembershipFollowup(@PathVariable("id") int id) {
		logger.info("Fetching MembershipFollowup with id {}", id);
		MembershipFollowup membershipFollowup = membershipFollowupService.findById(id);
		if (membershipFollowup == null) {
			logger.error("MembershipFollowup with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("MembershipFollowup with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MembershipFollowup>(membershipFollowup, HttpStatus.OK);
	}

	// -------------------Create a
	// MembershipFollowup-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipFollowup/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembershipFollowup(@RequestBody MembershipFollowup membershipFollowup,
			UriComponentsBuilder ucBuilder, @ModelAttribute("username") String username) {
		logger.info("Creating MembershipFollowup : {}", membershipFollowup);

		membershipFollowup.setCreatedBy(username);
		membershipFollowup.setUpdatedBy(username);
		membershipFollowupService.saveMembershipFollowup(membershipFollowup);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/membershipFollowup/{id}").buildAndExpand(membershipFollowup.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a MembershipFollowup
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipFollowup/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMembershipFollowup(@PathVariable("id") int id,
			@RequestBody MembershipFollowup membershipFollowup) {
		logger.info("Updating MembershipFollowup with id {}", id);

		MembershipFollowup currentMembershipFollowup = membershipFollowupService.findById(id);

		if (currentMembershipFollowup == null) {
			logger.error("Unable to update. MembershipFollowup with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. MembershipFollowup with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		membershipFollowupService.updateMembershipFollowup(currentMembershipFollowup);
		return new ResponseEntity<MembershipFollowup>(currentMembershipFollowup, HttpStatus.OK);
	}

	// ------------------- Delete a
	// MembershipFollowup-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipFollowup/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMembershipFollowup(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting MembershipFollowup with id {}", id);

		MembershipFollowup membershipFollowup = membershipFollowupService.findById(id);
		if (membershipFollowup == null) {
			logger.error("Unable to delete. MembershipFollowup with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. MembershipFollowup with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		membershipFollowupService.deleteMembershipFollowupById(id);
		return new ResponseEntity<MembershipFollowup>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// MembershipFollowups-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipFollowup/", method = RequestMethod.DELETE)
	public ResponseEntity<MembershipFollowup> deleteAllMembershipFollowups() {
		logger.info("Deleting All MembershipFollowups");

		membershipFollowupService.deleteAllMembershipFollowups();
		return new ResponseEntity<MembershipFollowup>(HttpStatus.NO_CONTENT);
	}

}