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

import com.pfchoice.springboot.model.File;
import com.pfchoice.springboot.repositories.specifications.FileSpecifications;
import com.pfchoice.springboot.service.FileService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class FileController {

	public static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	FileService fileService; // Service which will do all data
								// retrieval/manipulation work

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	// -------------------Retrieve Files as per page request
	// ---------------------------------------------

	@RequestMapping(value = "/file/", method = RequestMethod.GET)
	public ResponseEntity<Page<File>> listAllFiles(@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<File> spec = new FileSpecifications(search);
		Page<File> files = fileService.findAllFilesByPage(spec, pageRequest);
		
		if (files.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<File>>(files, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// File------------------------------------------
	@Secured({ "ROLE_ADMIN",  "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFile(@PathVariable("id") int id) {
		logger.info("Fetching File with id {}", id);
		File file = fileService.findById(id);
		if (file == null) {
			logger.error("File with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("File with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<File>(file, HttpStatus.OK);
	}

	// -------------------Create a
	// File-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/file/", method = RequestMethod.POST)
	public ResponseEntity<?> createFile(@RequestBody File file, UriComponentsBuilder ucBuilder,
			@ModelAttribute("username") String username) {
		logger.info("Creating File : {}", file);

		if (fileService.isFileExist(file)) {
			logger.error("Unable to create. A File with filename {} already exist", file.getFileName());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A File with filename " + file.getFileName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		// logger.info(" file.getRoles().size() :{} ", file.getRoles().size());
		file.setCreatedBy(username);
		file.setUpdatedBy(username);
		fileService.saveFile(file);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/file/{id}").buildAndExpand(file.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a File
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/file/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFile(@PathVariable("id") int id, @RequestBody File file,
			@ModelAttribute("username") String username) {
		logger.info("Updating File with id {}", id);

		File currentFile = fileService.findById(id);

		if (currentFile == null) {
			logger.error("Unable to update. File with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. File with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentFile.setFileName(file.getFileName());
		currentFile.setUpdatedBy(username);
		fileService.updateFile(currentFile);
		return new ResponseEntity<File>(currentFile, HttpStatus.OK);
	}

	// ------------------- Delete a
	// File-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/file/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFile(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting File with id {}", id);

		File file = fileService.findById(id);
		if (file == null) {
			logger.error("Unable to delete. File with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. File with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		fileService.deleteFileById(id);
		return new ResponseEntity<File>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Files-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/file/", method = RequestMethod.DELETE)
	public ResponseEntity<File> deleteAllFiles() {
		logger.info("Deleting All Files");

		fileService.deleteAllFiles();
		return new ResponseEntity<File>(HttpStatus.NO_CONTENT);
	}

}