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

import com.pfchoice.springboot.model.FileType;
import com.pfchoice.springboot.repositories.specifications.FileTypeSpecifications;
import com.pfchoice.springboot.service.FileTypeService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class FileTypeController {

	public static final Logger logger = LoggerFactory.getLogger(FileTypeController.class);

	@Autowired
	FileTypeService fileTypeService; // Service which will do all data
	// retrieval/manipulation work

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	// -------------------Retrieve FileTypes as per page request
	// ---------------------------------------------

	@RequestMapping(value = "/fileType/", method = RequestMethod.GET)
	public ResponseEntity<Page<FileType>> listAllFileTypes(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<FileType> spec = new FileTypeSpecifications(search);
		Page<FileType> fileTypes = fileTypeService.findAllFileTypesByPage(spec, pageRequest);

		if (fileTypes.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<FileType>>(fileTypes, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// FileType------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/fileType/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFileType(@PathVariable("id") int id) {
		logger.info("Fetching FileType with id {}", id);
		FileType fileType = fileTypeService.findById(id);
		if (fileType == null) {
			logger.error("FileType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("FileType with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<FileType>(fileType, HttpStatus.OK);
	}

	// -------------------Create a
	// FileType-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/fileType/", method = RequestMethod.POST)
	public ResponseEntity<?> createFileType(@RequestBody FileType fileType, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating FileType : {}", fileType);

		if (fileTypeService.isFileTypeExist(fileType)) {
			logger.error("Unable to create. A FileType with description {} already exist", fileType.getDescription());
			return new ResponseEntity(new CustomErrorType(
					"Unable to create. A FileType with description " + fileType.getDescription() + " already exist."),
					HttpStatus.CONFLICT);
		}
		// logger.info(" fileType.getRoles().size() :{} ",
		// fileType.getRoles().size());
		fileType.setCreatedBy(username);
		fileType.setUpdatedBy(username);
		fileTypeService.saveFileType(fileType);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/fileType/{id}").buildAndExpand(fileType.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a FileType
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/fileType/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFileType(@PathVariable("id") int id, @RequestBody FileType fileType,
			@ModelAttribute("username") String username) {
		logger.info("Updating FileType with id {}", id);

		FileType currentFileType = fileTypeService.findById(id);

		if (currentFileType == null) {
			logger.error("Unable to update. FileType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. FileType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentFileType.setDescription(fileType.getDescription());
		currentFileType.setUpdatedBy(username);
		fileTypeService.updateFileType(currentFileType);
		return new ResponseEntity<FileType>(currentFileType, HttpStatus.OK);
	}

	// ------------------- Delete a
	// FileType-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/fileType/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFileType(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting FileType with id {}", id);

		FileType fileType = fileTypeService.findById(id);
		if (fileType == null) {
			logger.error("Unable to delete. FileType with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. FileType with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		fileTypeService.deleteFileTypeById(id);
		return new ResponseEntity<FileType>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All FileTypes-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/fileType/", method = RequestMethod.DELETE)
	public ResponseEntity<FileType> deleteAllFileTypes() {
		logger.info("Deleting All FileTypes");

		fileTypeService.deleteAllFileTypes();
		return new ResponseEntity<FileType>(HttpStatus.NO_CONTENT);
	}

}