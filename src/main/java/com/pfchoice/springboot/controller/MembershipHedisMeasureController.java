package com.pfchoice.springboot.controller;

import java.util.List;

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

import com.pfchoice.springboot.model.MembershipHedisMeasure;
import com.pfchoice.springboot.repositories.specifications.MembershipHedisMeasureSpecifications;
import com.pfchoice.springboot.service.FileService;
import com.pfchoice.springboot.service.FileTypeService;
import com.pfchoice.springboot.service.MembershipHedisMeasureService;
import com.pfchoice.springboot.util.CustomErrorType;
import com.pfchoice.springboot.util.PrasUtil;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MembershipHedisMeasureController {

	public static final Logger logger = LoggerFactory.getLogger(MembershipHedisMeasureController.class);

	@Autowired
	MembershipHedisMeasureService membershipHedisMeasureService; // Service which will do all data
										// retrieval/manipulation work

	@Autowired
	FileTypeService fileTypeService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	PrasUtil prasUtil;
	
	
	// -------------------Retrieve All
	// Memberships---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipHedisMeasure/", method = RequestMethod.GET)
	public ResponseEntity<Page<MembershipHedisMeasure>> listAllMemberships(
			@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "mbrId", required = false) Integer mbrId,
			@RequestParam(value = "hedisRules", required = false) List<Integer> hedisRules,
			@RequestParam(value = "search", required = false) String search) {

		
		Specification<MembershipHedisMeasure> spec   = new MembershipHedisMeasureSpecifications(search, mbrId, hedisRules);
		Page<MembershipHedisMeasure> membershipHedisMeasures = membershipHedisMeasureService.findAllMembershipHedisMeasuresByPage(spec, pageRequest);
		
		if (membershipHedisMeasures.getTotalElements() == 0) {
			System.out.println("no memberships");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<MembershipHedisMeasure>>(membershipHedisMeasures, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// MembershipHedisMeasure------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipHedisMeasure/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMembership(@PathVariable("id") int id) {
		logger.info("Fetching MembershipHedisMeasure with id {}", id);
		final MembershipHedisMeasure membershipHedisMeasure = membershipHedisMeasureService.findById(id);
		if (membershipHedisMeasure == null) {
			logger.error("MembershipHedisMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("MembershipHedisMeasure with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MembershipHedisMeasure>(membershipHedisMeasure, HttpStatus.OK);
	}

	// -------------------Create a
	// MembershipHedisMeasure-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipHedisMeasure/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembership(@RequestBody MembershipHedisMeasure membershipHedisMeasure, UriComponentsBuilder ucBuilder) {
		logger.info("Creating MembershipHedisMeasure : {}", membershipHedisMeasure);

		if (membershipHedisMeasureService.isMembershipHedisMeasureExist(membershipHedisMeasure)) {
			logger.error("Unable to create. A MembershipHedisMeasure with name {} already exist", membershipHedisMeasure.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A MembershipHedisMeasure with name " + membershipHedisMeasure.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}

		logger.info("Creating MembershipHedisMeasure : before save");
		membershipHedisMeasureService.saveMembershipHedisMeasure(membershipHedisMeasure);
		logger.info("Creating MembershipHedisMeasure : after save");

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/membershipHedisMeasure/{id}").buildAndExpand(membershipHedisMeasure.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a MembershipHedisMeasure
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipHedisMeasure/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMembership(@PathVariable("id") int id, @RequestBody MembershipHedisMeasure membershipHedisMeasure) {
		logger.info("Updating MembershipHedisMeasure with id {}", id);

		MembershipHedisMeasure currentMembershipHedisMeasure = membershipHedisMeasureService.findById(id);

		if (currentMembershipHedisMeasure == null) {
			logger.error("Unable to update. MembershipHedisMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. MembershipHedisMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMembershipHedisMeasure.setDos(membershipHedisMeasure.getDos());
		currentMembershipHedisMeasure.setActiveInd('N');
		membershipHedisMeasureService.updateMembershipHedisMeasure(currentMembershipHedisMeasure);
		return new ResponseEntity<MembershipHedisMeasure>(currentMembershipHedisMeasure, HttpStatus.OK);
	}

	// ------------------- Delete a
	// MembershipHedisMeasure-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipHedisMeasure/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMembership(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting MembershipHedisMeasure with id {}", id);

		MembershipHedisMeasure membershipHedisMeasure = membershipHedisMeasureService.findById(id);
		if (membershipHedisMeasure == null) {
			logger.error("Unable to delete. MembershipHedisMeasure with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. MembershipHedisMeasure with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		membershipHedisMeasureService.deleteMembershipHedisMeasureById(id);
		return new ResponseEntity<MembershipHedisMeasure>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Memberships-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipHedisMeasure/", method = RequestMethod.DELETE)
	public ResponseEntity<MembershipHedisMeasure> deleteAllMemberships() {
		logger.info("Deleting All Memberships");

		membershipHedisMeasureService.deleteAllMembershipHedisMeasures();
		return new ResponseEntity<MembershipHedisMeasure>(HttpStatus.NO_CONTENT);
	}
	
}