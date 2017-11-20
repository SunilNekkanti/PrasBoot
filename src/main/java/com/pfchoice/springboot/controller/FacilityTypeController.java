package com.pfchoice.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

import com.pfchoice.springboot.model.FacilityType;
import com.pfchoice.springboot.repositories.specifications.FacilityTypeSpecifications;
import com.pfchoice.springboot.service.FacilityTypeService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class FacilityTypeController {

	public static final Logger logger = LoggerFactory.getLogger(FacilityTypeController.class);

	@Autowired
	FacilityTypeService facilityTypeService; // Service which will do all data
												// retrieval/manipulation work

	// -------------------Retrieve All
	// FacilityTypes---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/facilityType/", method = RequestMethod.GET)
	public ResponseEntity<Page<FacilityType>> listAllFacilityTypes(
			@RequestParam(value = "page", required = false) Integer pageNo,
			@RequestParam(value = "size", required = false) Integer pageSize,
			@RequestParam(value = "search", required = false) String search) {
		pageNo = (pageNo == null) ? 0 : pageNo;
		pageSize = (pageSize == null) ? 1000 : pageSize;
		PageRequest pageRequest = new PageRequest(pageNo, pageSize);

		Specification<FacilityType> spec = null;
		if (!"".equals(search))
			spec = new FacilityTypeSpecifications(search);

		Page<FacilityType> facilityTypes = facilityTypeService.findAllFacilityTypesByPage(spec, pageRequest);
		if (facilityTypes.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<FacilityType>>(facilityTypes, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// FacilityType------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/facilityType/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFacilityType(@PathVariable("id") int id) {
		logger.info("Fetching FacilityType with id {}", id);
		FacilityType facilityType = facilityTypeService.findById(id);
		if (facilityType == null) {
			logger.error("FacilityType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("FacilityType with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<FacilityType>(facilityType, HttpStatus.OK);
	}

	// -------------------Create a
	// FacilityType-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/facilityType/", method = RequestMethod.POST)
	public ResponseEntity<?> createFacilityType(@RequestBody FacilityType facilityType, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating FacilityType : {}", facilityType);

		if (facilityTypeService.isFacilityTypeExist(facilityType)) {
			logger.error("Unable to create. A FacilityType with name {} already exist", facilityType.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A FacilityType with name " + facilityType.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		facilityType.setCreatedBy(username);
		facilityType.setUpdatedBy(username);
		facilityTypeService.saveFacilityType(facilityType);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/facilityType/{id}").buildAndExpand(facilityType.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a FacilityType
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/facilityType/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFacilityType(@PathVariable("id") int id, @RequestBody FacilityType facilityType,
			@ModelAttribute("username") String username) {
		logger.info("Updating FacilityType with id {}", id);

		FacilityType currentFacilityType = facilityTypeService.findById(id);

		if (currentFacilityType == null) {
			logger.error("Unable to update. FacilityType with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. FacilityType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentFacilityType.setId(facilityType.getId());
		currentFacilityType.setDescription(facilityType.getDescription());
		currentFacilityType.setUpdatedBy(username);
		facilityTypeService.updateFacilityType(currentFacilityType);
		return new ResponseEntity<FacilityType>(currentFacilityType, HttpStatus.OK);
	}

	// ------------------- Delete a
	// FacilityType-----------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/facilityType/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFacilityType(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting FacilityType with id {}", id);

		FacilityType facilityType = facilityTypeService.findById(id);
		if (facilityType == null) {
			logger.error("Unable to delete. FacilityType with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. FacilityType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		facilityTypeService.deleteFacilityTypeById(id);
		return new ResponseEntity<FacilityType>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All FacilityTypes-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/facilityType/", method = RequestMethod.DELETE)
	public ResponseEntity<FacilityType> deleteAllFacilityTypes() {
		logger.info("Deleting All FacilityTypes");

		facilityTypeService.deleteAllFacilityTypes();
		return new ResponseEntity<FacilityType>(HttpStatus.NO_CONTENT);
	}

}