package com.pfchoice.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

import com.pfchoice.springboot.model.Membership;
import com.pfchoice.springboot.repositories.specifications.MembershipSpecifications;
import com.pfchoice.springboot.service.FileService;
import com.pfchoice.springboot.service.FileTypeService;
import com.pfchoice.springboot.service.MembershipService;
import com.pfchoice.springboot.util.CustomErrorType;
import com.pfchoice.springboot.util.PrasUtil;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MembershipController {

	public static final Logger logger = LoggerFactory.getLogger(MembershipController.class);

	@Value("classpath:static/sql/membership_load.sql")
	private Resource membershipLoadResource;

	@Value("classpath:static/sql/membership_insert.sql")
	private Resource membershipInsertResource;

	@Value("classpath:static/sql/membershipCapReport_load.sql")
	private Resource membershipCapReportLoadResource;

	@Value("classpath:static/sql/membershipCapReport_insert.sql")
	private Resource membershipCapReportInsertResource;

	@Value("classpath:static/sql/truncateTable.sql")
	private Resource truncateTable;

	@Autowired
	MembershipService membershipService; // Service which will do all data
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
	@RequestMapping(value = "/membership/", method = RequestMethod.GET)
	public ResponseEntity<Page<Membership>> listAllMemberships(
			@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "insId", required = false) Integer insId,
			@RequestParam(value = "prvdrId", required = false) Integer prvdrId,
			@RequestParam(value = "effectiveYear", required = false) Integer effectiveYear,
			@RequestParam(value = "problemIds", required = false) List<Integer> problemIds,
			@RequestParam(value = "search", required = false) String search) {

		Specification<Membership> spec = new MembershipSpecifications(search, insId, prvdrId, effectiveYear,
				problemIds);
		Page<Membership> memberships = membershipService.findAllMembershipsByPage(spec, pageRequest);

		if (memberships.getTotalElements() == 0) {
			System.out.println("no memberships");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Membership>>(memberships, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Membership------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membership/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMembership(@PathVariable("id") int id) {
		logger.info("Fetching Membership with id {}", id);
		final Membership membership = membershipService.findById(id);
		if (membership == null) {
			logger.error("Membership with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Membership with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Membership>(membership, HttpStatus.OK);
	}

	// -------------------Create a
	// Membership-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membership/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembership(@RequestBody Membership membership, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Membership : {}", membership);

		if (membershipService.isMembershipExist(membership)) {
			logger.error("Unable to create. A Membership with name {} already exist", membership.getFirstName());
			return new ResponseEntity(new CustomErrorType(
					"Unable to create. A Membership with name " + membership.getFirstName() + " already exist."),
					HttpStatus.CONFLICT);
		}

		logger.info("Creating Membership : before save");
		membershipService.saveMembership(membership);
		logger.info("Creating Membership : after save");

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/membership/{id}").buildAndExpand(membership.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Membership
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membership/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMembership(@PathVariable("id") int id, @RequestBody Membership membership) {
		logger.info("Updating Membership with id {}", id);

		Membership currentMembership = membershipService.findById(id);

		if (currentMembership == null) {
			logger.error("Unable to update. Membership with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Membership with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMembership.setFirstName(membership.getFirstName());
		currentMembership.setLastName(membership.getLastName());
		currentMembership.setDob(membership.getDob());

		membershipService.updateMembership(currentMembership);
		return new ResponseEntity<Membership>(currentMembership, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Membership-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membership/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMembership(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Membership with id {}", id);

		Membership membership = membershipService.findById(id);
		if (membership == null) {
			logger.error("Unable to delete. Membership with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Membership with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		membershipService.deleteMembershipById(id);
		return new ResponseEntity<Membership>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Memberships-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membership/", method = RequestMethod.DELETE)
	public ResponseEntity<Membership> deleteAllMemberships() {
		logger.info("Deleting All Memberships");

		membershipService.deleteAllMemberships();
		return new ResponseEntity<Membership>(HttpStatus.NO_CONTENT);
	}

}