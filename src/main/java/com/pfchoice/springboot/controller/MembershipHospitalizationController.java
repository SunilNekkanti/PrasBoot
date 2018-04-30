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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.Membership;
import com.pfchoice.springboot.model.MembershipHospitalization;
import com.pfchoice.springboot.repositories.specifications.MembershipHospitalizationSpecifications;
import com.pfchoice.springboot.service.ICDMeasureService;
import com.pfchoice.springboot.service.MembershipHospitalizationService;
import com.pfchoice.springboot.service.MembershipService;
import com.pfchoice.springboot.util.CustomErrorType;
import com.pfchoice.springboot.util.PrasUtil;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class MembershipHospitalizationController {

	public static final Logger logger = LoggerFactory.getLogger(MembershipHospitalizationController.class);

	@Autowired
	MembershipHospitalizationService membershipHospitalizationService; // Service which will do all  retrieval/manipulation work
					
	@Autowired
	MembershipService membershipService; // Service which will do all data  retrieval/manipulation work

	@Autowired
	ICDMeasureService icdMeasureService; // Service which will do all data  retrieval/manipulation work

	// data
	// retrieval/manipulation work
	@Autowired
	PrasUtil prasUtil;

	// -------------------Retrieve All
	// MembershipHospitalizations---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_USER","ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipHospitalization/", method = RequestMethod.GET)
	public ResponseEntity<Page<MembershipHospitalization>> listAllMembershipHospitalizations(
			@PageableDefault(page = 0, size = 100) Pageable pageable,
			@RequestParam(value = "insIds", required = true) List<Integer> insIds,
			@RequestParam(value = "prvdrIds", required = true) List<Integer> prvdrIds,
			@RequestParam(value = "search", required = false) String search) {
		
		Specification<MembershipHospitalization> spec = new MembershipHospitalizationSpecifications(search, insIds, prvdrIds);
		Page<MembershipHospitalization> membershipHospitalizations = membershipHospitalizationService.findAllMembershipHospitalizationsByPage(spec, pageable);
		if (membershipHospitalizations.getTotalElements() ==0) {
			System.out.println("no membershipHospitalizations");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<MembershipHospitalization>>(membershipHospitalizations, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// MembershipHospitalization------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipHospitalization/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMembershipHospitalization(@PathVariable("id") int id) {
		logger.info("Fetching MembershipHospitalization with id {}", id);
		MembershipHospitalization membershipHospitalization = membershipHospitalizationService.findById(id);
		if (membershipHospitalization == null) {
			logger.error("MembershipHospitalization with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("MembershipHospitalization with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MembershipHospitalization>(membershipHospitalization, HttpStatus.OK);
	}

 
	// -------------------Create a
	// MembershipHospitalization-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/membershipHospitalization/", method = RequestMethod.POST)
	public ResponseEntity<?> createMembershipHospitalization(@RequestBody List<MembershipHospitalization> membershipHospitalizations,
			@RequestBody Integer membershipId,
			@ModelAttribute("username") String username,	UriComponentsBuilder ucBuilder) {
		logger.info("Creating membershipId : {}",membershipId);
		logger.info("Creating MembershipHospitalizations : {}", membershipHospitalizations);

		Membership currentMembership = membershipService.findById(membershipId);
		
		  for(MembershipHospitalization membershipHospitalization : membershipHospitalizations){
			  
				membershipHospitalization.setCreatedBy(username);
				membershipHospitalization.setUpdatedBy(username);
				membershipHospitalizationService.saveMembershipHospitalization(membershipHospitalization);

		  }
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/membership/{membershipId}").buildAndExpand(currentMembership.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	// ------------------- Update a MembershipHospitalization
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipHospitalization/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMembershipHospitalization(@PathVariable("id") int id,
			@RequestBody MembershipHospitalization membershipHospitalization,	@ModelAttribute("username") String username) {
		logger.info("Updating MembershipHospitalization with id {}", id);

		MembershipHospitalization currentMembershipHospitalization = membershipHospitalizationService.findById(id);

		if (currentMembershipHospitalization == null) {
			logger.error("Unable to update. MembershipHospitalization with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. MembershipHospitalization with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentMembershipHospitalization.setUpdatedBy(username);
		membershipHospitalizationService.updateMembershipHospitalization(currentMembershipHospitalization);
		return new ResponseEntity<MembershipHospitalization>(currentMembershipHospitalization, HttpStatus.OK);
	}

	// ------------------- Delete a
	// MembershipHospitalization-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipHospitalization/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMembershipHospitalization(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting MembershipHospitalization with id {}", id);

		MembershipHospitalization membershipHospitalization = membershipHospitalizationService.findById(id);
		if (membershipHospitalization == null) {
			logger.error("Unable to delete. MembershipHospitalization with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. MembershipHospitalization with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		membershipHospitalizationService.deleteMembershipHospitalizationById(id);
		return new ResponseEntity<MembershipHospitalization>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// MembershipHospitalizations-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/membershipHospitalization/", method = RequestMethod.DELETE)
	public ResponseEntity<MembershipHospitalization> deleteAllMembershipHospitalizations() {
		logger.info("Deleting All MembershipHospitalizations");

		membershipHospitalizationService.deleteAllMembershipHospitalizations();
		return new ResponseEntity<MembershipHospitalization>(HttpStatus.NO_CONTENT);
	}


}