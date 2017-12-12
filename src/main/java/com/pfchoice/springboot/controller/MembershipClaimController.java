package com.pfchoice.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.MembershipClaim;
import com.pfchoice.springboot.service.MembershipClaimService;
import com.pfchoice.springboot.util.CustomErrorType;
import com.pfchoice.springboot.util.PrasUtil;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class MembershipClaimController {

	public static final Logger logger = LoggerFactory.getLogger(MembershipClaimController.class);

	@Autowired
	MembershipClaimService membershipClaimService; // Service which will do all
													// data
	// retrieval/manipulation work
	@Autowired
	PrasUtil prasUtil;

	// -------------------Retrieve All
	// MembershipClaims---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipClaim/", method = RequestMethod.GET)
	public ResponseEntity<List<MembershipClaim>> listAllMembershipClaims() {
		List<MembershipClaim> membershipClaims = membershipClaimService.findAllMembershipClaims();
		if (membershipClaims.isEmpty()) {
			System.out.println("no membershipClaims");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<MembershipClaim>>(membershipClaims, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// MembershipClaim------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipClaim/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMembershipClaim(@PathVariable("id") int id) {
		logger.info("Fetching MembershipClaim with id {}", id);
		MembershipClaim membershipClaim = membershipClaimService.findById(id);
		if (membershipClaim == null) {
			logger.error("MembershipClaim with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("MembershipClaim with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MembershipClaim>(membershipClaim, HttpStatus.OK);
	}

	// -------------------Create a
	// MembershipClaim-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipClaim/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembershipClaim(@RequestBody MembershipClaim membershipClaim,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating MembershipClaim : {}", membershipClaim);

		if (membershipClaimService.isMembershipClaimExist(membershipClaim)) {
			logger.error("Unable to create. A MembershipClaim with name {} already exist",
					membershipClaim.getClaimNumber());
			return new ResponseEntity(new CustomErrorType("Unable to create. A MembershipClaim with name "
					+ membershipClaim.getClaimNumber() + " already exist."), HttpStatus.CONFLICT);
		}
		membershipClaim.setCreatedBy("sarath");
		membershipClaim.setUpdatedBy("sarath");
		membershipClaimService.saveMembershipClaim(membershipClaim);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/membershipClaim/{id}").buildAndExpand(membershipClaim.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a MembershipClaim
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipClaim/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMembershipClaim(@PathVariable("id") int id,
			@RequestBody MembershipClaim membershipClaim) {
		logger.info("Updating MembershipClaim with id {}", id);

		MembershipClaim currentMembershipClaim = membershipClaimService.findById(id);

		if (currentMembershipClaim == null) {
			logger.error("Unable to update. MembershipClaim with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. MembershipClaim with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMembershipClaim.setClaimNumber(membershipClaim.getClaimNumber());

		membershipClaimService.updateMembershipClaim(currentMembershipClaim);
		return new ResponseEntity<MembershipClaim>(currentMembershipClaim, HttpStatus.OK);
	}

	// ------------------- Delete a
	// MembershipClaim-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipClaim/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMembershipClaim(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting MembershipClaim with id {}", id);

		MembershipClaim membershipClaim = membershipClaimService.findById(id);
		if (membershipClaim == null) {
			logger.error("Unable to delete. MembershipClaim with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. MembershipClaim with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		membershipClaimService.deleteMembershipClaimById(id);
		return new ResponseEntity<MembershipClaim>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// MembershipClaims-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipClaim/", method = RequestMethod.DELETE)
	public ResponseEntity<MembershipClaim> deleteAllMembershipClaims() {
		logger.info("Deleting All MembershipClaims");

		membershipClaimService.deleteAllMembershipClaims();
		return new ResponseEntity<MembershipClaim>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All
	// ReportMonths---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipClaimReportMonth/", method = RequestMethod.GET)
	public ResponseEntity<List<String>> listAllMembershipClaimReportMonths() {
		List<String> membershipClaimReportMonths = membershipClaimService.findAllMembershipClaimReportMonths();
		if (membershipClaimReportMonths.isEmpty()) {
			System.out.println("no membershipClaimReportMonths");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<String>>(membershipClaimReportMonths, HttpStatus.OK);
	}

	// -------------------Retrieve All
	// ReportMonths---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipClaimRiskCategory/", method = RequestMethod.GET)
	public ResponseEntity<List<String>> listAllMembershipClaimRiskCategories() {
		List<String> membershipClaimRiskCategories = membershipClaimService.findAllMembershipClaimRiskCategories();
		if (membershipClaimRiskCategories.isEmpty()) {
			System.out.println("no membershipClaimRiskCategories");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<String>>(membershipClaimRiskCategories, HttpStatus.OK);
	}

	// -------------------Retrieve All
	// MedicalLossRatios---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipClaimsReport/", method = RequestMethod.GET)
	public ResponseEntity<List<Object[]>> listClaimsReport(
			@RequestParam(value = "insId", required = true) Integer insId,
			@RequestParam(value = "prvdrIds", required = true) String prvdrIds,
			@RequestParam(value = "mbrId", required = true) Integer mbrId,
			@RequestParam(value = "claimTypes", required = true) String claimTypes,
			@RequestParam(value = "reportMonth", required = true) Integer reportMonth,
			@RequestParam(value = "activityMonth", required = true) Integer activityMonth,
			@RequestParam(value = "categories", required = true) String categories,
			@RequestParam(value = "inCap", required = true) String caps,
			@RequestParam(value = "inRoster", required = true) String rosters,
			@RequestParam(value = "levelNo", required = true) Integer levelNo,
			@RequestParam(value = "maxReportMonth", required = true) Integer maxReportMonth,
			@ModelAttribute("roleName") String roleName, @ModelAttribute("username") String username) {

		logger.info("inside membershipClaims");

		Map<String, Object> params = new HashMap<>();
		params.put("tableName", username + "_claimsReport");
		params.put("insId", insId);
		params.put("prvdrId", prvdrIds);
		params.put("mbrId", mbrId);
		params.put("rptMonth", reportMonth);
		params.put("activityMonth", activityMonth);
		params.put("claimType", claimTypes);
		params.put("category", categories);
		params.put("roster", rosters);
		params.put("cap", caps);
		params.put("levelNo", levelNo);
		params.put("maxReportMonth", maxReportMonth);

		List<Object[]> entities = prasUtil.executeStoredProcedure("new_clm_report", params);
		if (entities.isEmpty()) {
			logger.info("no data in membershipClaims");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		logger.info("size" + entities.size());
		return new ResponseEntity<List<Object[]>>(entities, HttpStatus.OK);
	}

}