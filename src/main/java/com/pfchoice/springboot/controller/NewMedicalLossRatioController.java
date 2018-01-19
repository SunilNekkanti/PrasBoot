package com.pfchoice.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pfchoice.springboot.model.NewMedicalLossRatio;
import com.pfchoice.springboot.repositories.specifications.NewMedicalLossRatioSpecifications;
import com.pfchoice.springboot.service.NewMedicalLossRatioService;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class NewMedicalLossRatioController {

	public static final Logger logger = LoggerFactory.getLogger(NewMedicalLossRatioController.class);

	@Autowired
	NewMedicalLossRatioService newMedicalLossRatioService; // Service which will
															// do all data
	// retrieval/manipulation work

	// -------------------Retrieve All
	// NewMedicalLossRatios---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/newMedicalLossRatio/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllNewMedicalLossRatios(@PageableDefault(page = 0, size = 5000) Pageable pageRequest,
			@RequestParam(value = "insId", required = true) Integer insId,
			@RequestParam(value = "prvdrIds", required = true) List<Integer> prvdrIds,
			@RequestParam(value = "reportMonths", required = true) List<Integer> reportMonths,
			@RequestParam(value = "activityMonths", required = true) List<Integer> activityMonths,
			@RequestParam(value = "isSummary", required = true) boolean isSummary,
			@RequestParam(value = "search", required = false) String search) {

		Page<NewMedicalLossRatio> newMedicalLossRatios;
		if (isSummary) {
			newMedicalLossRatios = newMedicalLossRatioService.findSummary(insId, prvdrIds, reportMonths, activityMonths, pageRequest);
		} else {
			Specification<NewMedicalLossRatio> spec = new NewMedicalLossRatioSpecifications(search, insId, prvdrIds,
					reportMonths, activityMonths);
			newMedicalLossRatios = newMedicalLossRatioService.findAllNewMedicalLossRatios(spec, pageRequest);
		}

		if (newMedicalLossRatios.getTotalElements() == 0) {
			System.out.println("no newMedicalLossRatios");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<NewMedicalLossRatio>>(newMedicalLossRatios, HttpStatus.OK);
	}

	// ------------------- Delete All
	// NewMedicalLossRatios-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/newMedicalLossRatio/", method = RequestMethod.DELETE)
	public ResponseEntity<NewMedicalLossRatio> deleteAllNewMedicalLossRatios() {
		logger.info("Deleting All NewMedicalLossRatios");

		newMedicalLossRatioService.deleteAllNewMedicalLossRatios();
		return new ResponseEntity<NewMedicalLossRatio>(HttpStatus.NO_CONTENT);
	}

	
	// -------------------Retrieve All
	// ReportMonths---------------------------------------------

	@SuppressWarnings("unchecked")
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/newMedicalLossRatio/reportingYears/", method = RequestMethod.GET)
	public ResponseEntity<List<String>> listAllReportingYears() {
		List<String> reportYears = newMedicalLossRatioService.findAllReportingYears();

		if (reportYears.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<String>>(reportYears, HttpStatus.OK);
	}

}