package com.pfchoice.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.pfchoice.springboot.model.RiskScore;
import com.pfchoice.springboot.repositories.specifications.RiskScoreSpecifications;
import com.pfchoice.springboot.service.RiskScoreService;
import com.pfchoice.springboot.util.PrasUtil;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class RiskScoreController {

	public static final Logger logger = LoggerFactory.getLogger(RiskScoreController.class);

	@Autowired
	PrasUtil prasUtil;
	
	@Autowired
	RiskScoreService RiskScoreService; // Service which will do all data
	// retrieval/manipulation work

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	// -------------------Retrieve RiskScores as per page request
	// ---------------------------------------------

	@RequestMapping(value = "/riskScore/", method = RequestMethod.GET)
	public ResponseEntity<Page<RiskScore>> listAllRiskScores(
			@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<RiskScore> spec = new RiskScoreSpecifications(search);
		Page<RiskScore> RiskScores = RiskScoreService.findAllRiskScoresByPage(spec, pageRequest);

		if (RiskScores.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<RiskScore>>(RiskScores, HttpStatus.OK);
	}




	// ------------------- Delete All
	// RiskScores-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/riskScore/", method = RequestMethod.DELETE)
	public ResponseEntity<RiskScore> deleteAllRiskScores() {
		logger.info("Deleting All RiskScores");

		RiskScoreService.deleteAllRiskScores();
		return new ResponseEntity<RiskScore>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All
		// ReportMonths---------------------------------------------

		@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
		@RequestMapping(value = "/paymentYears/", method = RequestMethod.GET)
		public ResponseEntity<List<String>> listAllPaymentYears() {
			List<String> paymentYears = RiskScoreService.findAllPaymentYears();
			if (paymentYears.isEmpty()) {
				System.out.println("no membershipClaimReportMonths");
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
			return new ResponseEntity<List<String>>(paymentYears, HttpStatus.OK);
		}
		
		@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
		@RequestMapping(value = "/calculateRiskScore/", method = RequestMethod.GET)
		public ResponseEntity<List<Object[]>> calculateRiskScore(@RequestParam(value = "icdcodes", required = true) String icdcodes,
				@RequestParam(value = "instOrComm", required = true) String instOrComm, @RequestParam(value = "effYear", required = true) Integer effYear,
				@RequestParam(value = "gender", required = true) String gender, @RequestParam(value = "dob", required = true) String dob,
				@RequestParam(value = "medicaid", required = true) String medicaid, @RequestParam(value = "isAgedOrDisabled", required = true) String isAgedOrDisabled,
				@RequestParam(value = "originallyDueToDis", required = true) String originallyDueToDis) {
			
			Map<String, Object> params = new HashMap<>();
			params.put("icdcodes", icdcodes);
			params.put("instOrComm", instOrComm);
			params.put("effYear", effYear);
			params.put("sex", gender);
			params.put("dob", dob);
			params.put("isMedicaid", medicaid);
			params.put("isAgedOrDisabled", isAgedOrDisabled);
			params.put("isOriginallyDisabled", originallyDueToDis);
			
			List<Object[]> riskscores = prasUtil.executeStoredProcedure("calculateRiskScore", params);
			if (riskscores.isEmpty()) {
				System.out.println("no riskscore");
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
			return new ResponseEntity<List<Object[]>>(riskscores, HttpStatus.OK);
		}
		
}