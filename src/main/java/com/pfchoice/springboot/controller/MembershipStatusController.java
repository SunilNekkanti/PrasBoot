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
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.MembershipStatus;
import com.pfchoice.springboot.repositories.specifications.MembershipStatusSpecifications;
import com.pfchoice.springboot.service.MembershipStatusService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MembershipStatusController {

	public static final Logger logger = LoggerFactory.getLogger(MembershipStatusController.class);

	@Autowired
	MembershipStatusService membershipStatusService; // Service which will do all data
											// retrieval/manipulation work

	// -------------------Retrieve All
	// MembershipStatuses---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipStatus/", method = RequestMethod.GET)
	public ResponseEntity<Page<MembershipStatus>> listAllMembershipStatuses(
			@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<MembershipStatus> spec =  new MembershipStatusSpecifications(search);
		Page<MembershipStatus> membershipStatuss = membershipStatusService.findAllMembershipStatusesByPage(spec, pageRequest);
		
		if (membershipStatuss.getTotalElements() == 0) {
			System.out.println("no membershipStatuss");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<MembershipStatus>>(membershipStatuss, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// MembershipStatus------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipStatus/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMembershipStatus(@PathVariable("id") byte id) {
		logger.info("Fetching MembershipStatus with id {}", id);
		MembershipStatus membershipStatus = membershipStatusService.findById(id);
		if (membershipStatus == null) {
			logger.error("MembershipStatus with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("MembershipStatus with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MembershipStatus>(membershipStatus, HttpStatus.OK);
	}

	// -------------------Create a
	// MembershipStatus-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipStatus/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembershipStatus(@RequestBody MembershipStatus membershipStatus, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating MembershipStatus : {}", membershipStatus);

		if (membershipStatusService.isMembershipStatusExist(membershipStatus)) {
			logger.error("Unable to create. A MembershipStatus with name {} already exist", membershipStatus.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A MembershipStatus with name " + membershipStatus.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		membershipStatus.setCreatedBy(username);
		membershipStatus.setUpdatedBy(username);
		membershipStatusService.saveMembershipStatus(membershipStatus);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/membershipStatus/{id}").buildAndExpand(membershipStatus.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a MembershipStatus
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipStatus/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMembershipStatus(@PathVariable("id") byte id, @RequestBody MembershipStatus membershipStatus,
			@ModelAttribute("username") String username) {
		logger.info("Updating MembershipStatus with id {}", id);

		MembershipStatus currentMembershipStatus = membershipStatusService.findById(id);

		if (currentMembershipStatus == null) {
			logger.error("Unable to update. MembershipStatus with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. MembershipStatus with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMembershipStatus.setDescription(membershipStatus.getDescription());
		currentMembershipStatus.setUpdatedBy(username);
		membershipStatusService.updateMembershipStatus(currentMembershipStatus);
		return new ResponseEntity<MembershipStatus>(currentMembershipStatus, HttpStatus.OK);
	}

	// ------------------- Delete a
	// MembershipStatus-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipStatus/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMembershipStatus(@PathVariable("id") byte id) {
		logger.info("Fetching & Deleting MembershipStatus with id {}", id);

		MembershipStatus membershipStatus = membershipStatusService.findById(id);
		if (membershipStatus == null) {
			logger.error("Unable to delete. MembershipStatus with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. MembershipStatus with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		membershipStatusService.deleteMembershipStatusById(id);
		return new ResponseEntity<MembershipStatus>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All MembershipStatuses-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipStatus/", method = RequestMethod.DELETE)
	public ResponseEntity<MembershipStatus> deleteAllMembershipStatuses() {
		logger.info("Deleting All MembershipStatuses");

		membershipStatusService.deleteAllMembershipStatuses();
		return new ResponseEntity<MembershipStatus>(HttpStatus.NO_CONTENT);
	}

}