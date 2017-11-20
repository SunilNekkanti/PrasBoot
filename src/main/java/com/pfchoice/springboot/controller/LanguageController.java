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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.Language;
import com.pfchoice.springboot.repositories.specifications.LanguageSpecifications;
import com.pfchoice.springboot.service.LanguageService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LanguageController {

	public static final Logger logger = LoggerFactory.getLogger(LanguageController.class);

	@Autowired
	LanguageService languageService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// Languages---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/language/", method = RequestMethod.GET)
	public ResponseEntity<Page<Language>> listAllLanguages(
			@RequestParam(value = "page", required = false) Integer pageNo,
			@RequestParam(value = "size", required = false) Integer pageSize,
			@RequestParam(value = "search", required = false) String search) {
		pageNo = (pageNo == null) ? 0 : pageNo;
		pageSize = (pageSize == null) ? 1000 : pageSize;

		PageRequest pageRequest = new PageRequest(pageNo, pageSize);
		Specification<Language> spec = null;
		// if(!"".equals(search))
		spec = new LanguageSpecifications(search);
		Page<Language> languages = languageService.findAllLanguagesByPage(spec, pageRequest);
		if (languages.getTotalElements() == 0) {
			System.out.println("no Languages");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Language>>(languages, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Language------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/language/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getLanguage(@PathVariable("id") short id) {
		logger.info("Fetching Language with id {}", id);
		Language Language = languageService.findById(id);
		if (Language == null) {
			logger.error("Language with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Language with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Language>(Language, HttpStatus.OK);
	}

	// -------------------Create a
	// Language-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/language/", method = RequestMethod.POST)
	public ResponseEntity<?> createLanguage(@RequestBody Language language, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Language : {}", language);

		if (languageService.isLanguageExist(language)) {
			logger.error("Unable to create. A Language with name {} already exist", language.getId());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A Language with name " + language.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		languageService.saveLanguage(language);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/Language/{id}").buildAndExpand(language.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Language
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/language/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLanguage(@PathVariable("id") short id, @RequestBody Language language) {
		logger.info("Updating Language with id {}", id);

		Language currentLanguage = languageService.findById(id);

		if (currentLanguage == null) {
			logger.error("Unable to update. Language with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Language with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentLanguage.setDescription(language.getDescription());

		languageService.updateLanguage(currentLanguage);
		return new ResponseEntity<Language>(currentLanguage, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Language-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/language/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLanguage(@PathVariable("id") short id) {
		logger.info("Fetching & Deleting Language with id {}", id);

		Language Language = languageService.findById(id);
		if (Language == null) {
			logger.error("Unable to delete. Language with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Language with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		languageService.deleteLanguageById(id);
		return new ResponseEntity<Language>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Languages-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/language/", method = RequestMethod.DELETE)
	public ResponseEntity<Language> deleteAllLanguages() {
		logger.info("Deleting All Languages");

		languageService.deleteAllLanguages();
		return new ResponseEntity<Language>(HttpStatus.NO_CONTENT);
	}

}