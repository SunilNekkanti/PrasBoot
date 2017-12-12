package com.pfchoice.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pfchoice.springboot.service.MedicalLossRatioService;
import com.pfchoice.springboot.util.PrasUtil;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class ReportController {

	public static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	MedicalLossRatioService medicalLossRatioService; // Service which will do
														// all data
	// retrieval/manipulation work

	@Autowired
	PrasUtil prasUtil;

	// -------------------Retrieve All
	// MedicalLossRatios---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/medicalLossRatio/", method = RequestMethod.GET)
	public ResponseEntity<List<Object[]>> listAllMedicalLossRatios(
			@RequestParam(value = "insId", required = true) Integer insId,
			@RequestParam(value = "prvdrIds", required = true) String prvdrIds,
			@RequestParam(value = "reportMonths", required = true) String reportMonths,
			@RequestParam(value = "categories", required = true) String categories,
			@ModelAttribute("roleName") String roleName, @ModelAttribute("username") String username) {

		final char adminRole = ("ADMIN".equals(roleName)) ? 'Y' : 'N';
		logger.info(insId + " prvdrIds  " + prvdrIds + " reportMonths " + reportMonths + " categories " + categories
				+ " " + username + " adminRole" + adminRole);
		Map<String, Object> params = new HashMap<>();
		params.put("insId", insId);
		params.put("prvdrIds", prvdrIds);
		params.put("repMonths", reportMonths);
		params.put("categories", categories);
		params.put("adminRole", adminRole);
		params.put("tableName", username + "_MedicalLossRatio");

		List<Object[]> entities = prasUtil.executeStoredProcedure("mlr_report", params);

		if (entities.isEmpty()) {
			System.out.println("no data in medicalLossRatio");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		logger.info("size" + entities.size());
		return new ResponseEntity<List<Object[]>>(entities, HttpStatus.OK);
	}

	// -------------------Retrieve All
	// MedicalLossRatios---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/hedisReport/", method = RequestMethod.GET)
	public ResponseEntity<List<Object[]>> listAllMembershipsHedis(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "insId", required = false) Integer insId,
			@RequestParam(value = "prvdrId", required = false) Integer prvdrId,
			@RequestParam(value = "reportMonth", required = false) Integer reportMonth,
			@RequestParam(value = "hedisRules", required = false) String hedisRules,
			@RequestParam(value = "rosters", required = false) String rosters,
			@RequestParam(value = "caps", required = false) String caps,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "statuses", required = false) String statuses,
			@RequestParam(value = "search", required = false) String search,
			@ModelAttribute("roleName") String roleName, @ModelAttribute("username") String username) {

		logger.info(insId + " prvdrId  " + prvdrId + " reportMonth " + reportMonth + " hedisRules " + hedisRules + " "
				+ username + " startDate" + startDate + " endDate" + endDate + " statuses" + statuses + " rosters"
				+ rosters + " caps" + caps + "page" + page + "size" + size);
		Map<String, Object> params = new HashMap<>();
		params.put("insId", insId);
		params.put("prvdrId", prvdrId);
		params.put("reportMonth", reportMonth);
		params.put("hedisRuleList", hedisRules);
		params.put("roster", rosters);
		params.put("cap", caps);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("sstatus", statuses);
		params.put("sSearch", search);
		params.put("pageSize", size);
		params.put("pageNo", page);

		List<Object[]> entities = prasUtil.executeStoredProcedure("hedis_report", params);

		if (entities.isEmpty()) {
			System.out.println("no data in membershipHedisReport");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		logger.info("size" + entities.size());
		return new ResponseEntity<List<Object[]>>(entities, HttpStatus.OK);
	}

}