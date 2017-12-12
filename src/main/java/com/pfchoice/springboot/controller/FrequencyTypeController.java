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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.FrequencyType;
import com.pfchoice.springboot.repositories.specifications.FrequencyTypeSpecifications;
import com.pfchoice.springboot.service.FrequencyTypeService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class FrequencyTypeController {

	public static final Logger logger = LoggerFactory.getLogger(FrequencyTypeController.class);

	@Autowired
	FrequencyTypeService frequencyTypeService; // Service which will do all data
	// retrieval/manipulation work

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	// -------------------Retrieve FrequencyTypes as per page request
	// ---------------------------------------------

	@RequestMapping(value = "/frequencyType/", method = RequestMethod.GET)
	public ResponseEntity<Page<FrequencyType>> listAllFrequencyTypes(
			@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<FrequencyType> spec = new FrequencyTypeSpecifications(search);
		Page<FrequencyType> frequencyTypes = frequencyTypeService.findAllFrequencyTypesByPage(spec, pageRequest);

		if (frequencyTypes.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<FrequencyType>>(frequencyTypes, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// FrequencyType------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/frequencyType/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFrequencyType(@PathVariable("id") int id) {
		logger.info("Fetching FrequencyType with id {}", id);
		FrequencyType frequencyType = frequencyTypeService.findById(id);
		if (frequencyType == null) {
			logger.error("FrequencyType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("FrequencyType with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<FrequencyType>(frequencyType, HttpStatus.OK);
	}

	// -------------------Create a
	// FrequencyType-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/frequencyType/", method = RequestMethod.POST)
	public ResponseEntity<?> createFrequencyType(@RequestBody FrequencyType frequencyType,
			UriComponentsBuilder ucBuilder, @ModelAttribute("username") String username) {
		logger.info("Creating FrequencyType : {}", frequencyType);

		if (frequencyTypeService.isFrequencyTypeExist(frequencyType)) {
			logger.error("Unable to create. A FrequencyType with description {} already exist",
					frequencyType.getDescription());
			return new ResponseEntity(new CustomErrorType("Unable to create. A FrequencyType with description "
					+ frequencyType.getDescription() + " already exist."), HttpStatus.CONFLICT);
		}
		// logger.info(" frequencyType.getRoles().size() :{} ",
		// frequencyType.getRoles().size());
		frequencyType.setCreatedBy(username);
		frequencyType.setUpdatedBy(username);
		frequencyTypeService.saveFrequencyType(frequencyType);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/frequencyType/{id}").buildAndExpand(frequencyType.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a FrequencyType
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/frequencyType/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFrequencyType(@PathVariable("id") int id, @RequestBody FrequencyType frequencyType,
			@ModelAttribute("username") String username) {
		logger.info("Updating FrequencyType with id {}", id);

		FrequencyType currentFrequencyType = frequencyTypeService.findById(id);

		if (currentFrequencyType == null) {
			logger.error("Unable to update. FrequencyType with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. FrequencyType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentFrequencyType.setDescription(frequencyType.getDescription());
		currentFrequencyType.setUpdatedBy(username);
		frequencyTypeService.updateFrequencyType(currentFrequencyType);
		return new ResponseEntity<FrequencyType>(currentFrequencyType, HttpStatus.OK);
	}

	// ------------------- Delete a
	// FrequencyType-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/frequencyType/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFrequencyType(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting FrequencyType with id {}", id);

		FrequencyType frequencyType = frequencyTypeService.findById(id);
		if (frequencyType == null) {
			logger.error("Unable to delete. FrequencyType with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. FrequencyType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		frequencyTypeService.deleteFrequencyTypeById(id);
		return new ResponseEntity<FrequencyType>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// FrequencyTypes-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/frequencyType/", method = RequestMethod.DELETE)
	public ResponseEntity<FrequencyType> deleteAllFrequencyTypes() {
		logger.info("Deleting All FrequencyTypes");

		frequencyTypeService.deleteAllFrequencyTypes();
		return new ResponseEntity<FrequencyType>(HttpStatus.NO_CONTENT);
	}

}