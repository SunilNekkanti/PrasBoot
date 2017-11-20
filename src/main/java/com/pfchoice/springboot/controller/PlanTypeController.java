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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.PlanType;
import com.pfchoice.springboot.repositories.specifications.PlanTypeSpecifications;
import com.pfchoice.springboot.service.PlanTypeService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PlanTypeController {

	public static final Logger logger = LoggerFactory.getLogger(PlanTypeController.class);

	@Autowired
	PlanTypeService planTypeService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// PlanTypes---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/planType/", method = RequestMethod.GET)
	public ResponseEntity<Page<PlanType>> listAllPlanTypes(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {
		
		Specification<PlanType> spec = new PlanTypeSpecifications( search);
		Page<PlanType> planTypes = planTypeService.findAllPlanTypesByPage(spec, pageRequest);

		
		if (planTypes.getTotalElements() == 0) {
			System.out.println("no planTypes");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<PlanType>>(planTypes, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// PlanType------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/planType/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPlanType(@PathVariable("id") int id) {
		logger.info("Fetching PlanType with id {}", id);
		PlanType planType = planTypeService.findById(id);
		if (planType == null) {
			logger.error("PlanType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("PlanType with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<PlanType>(planType, HttpStatus.OK);
	}

	// -------------------Create a
	// PlanType-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/planType/", method = RequestMethod.POST)
	public ResponseEntity<?> createPlanType(@RequestBody PlanType planType, UriComponentsBuilder ucBuilder) {
		logger.info("Creating PlanType : {}", planType);

		if (planTypeService.isPlanTypeExist(planType)) {
			logger.error("Unable to create. A PlanType with name {} already exist", planType.getCode());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A PlanType with name " + planType.getCode() + " already exist."),
					HttpStatus.CONFLICT);
		}
		planType.setCreatedBy("sarath");
		planType.setUpdatedBy("sarath");
		planTypeService.savePlanType(planType);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/planType/{id}").buildAndExpand(planType.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a PlanType
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/planType/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePlanType(@PathVariable("id") int id, @RequestBody PlanType planType) {
		logger.info("Updating PlanType with id {}", id);

		PlanType currentPlanType = planTypeService.findById(id);

		if (currentPlanType == null) {
			logger.error("Unable to update. PlanType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. PlanType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentPlanType.setCode(planType.getCode());
		currentPlanType.setDescription(planType.getDescription());

		planTypeService.updatePlanType(currentPlanType);
		return new ResponseEntity<PlanType>(currentPlanType, HttpStatus.OK);
	}

	// ------------------- Delete a
	// PlanType-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/planType/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePlanType(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting PlanType with id {}", id);

		PlanType planType = planTypeService.findById(id);
		if (planType == null) {
			logger.error("Unable to delete. PlanType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. PlanType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		planTypeService.deletePlanTypeById(id);
		return new ResponseEntity<PlanType>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All PlanTypes-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/planType/", method = RequestMethod.DELETE)
	public ResponseEntity<PlanType> deleteAllPlanTypes() {
		logger.info("Deleting All PlanTypes");

		planTypeService.deleteAllPlanTypes();
		return new ResponseEntity<PlanType>(HttpStatus.NO_CONTENT);
	}

}