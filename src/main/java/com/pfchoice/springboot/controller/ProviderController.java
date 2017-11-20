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

import com.pfchoice.springboot.model.Provider;
import com.pfchoice.springboot.repositories.specifications.ProviderSpecifications;
import com.pfchoice.springboot.service.ProviderService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProviderController {

	public static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

	@Autowired
	ProviderService providerService; // Service which will do all data
										// retrieval/manipulation work

	// -------------------Retrieve All
	// Providers---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/provider/", method = RequestMethod.GET)
	public ResponseEntity<Page<Provider>> listAllProviders(
			@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<Provider> spec  = new ProviderSpecifications(search);
		Page<Provider> providers = providerService.findAllProvidersByPage(spec, pageRequest);
		
		if (providers.getTotalElements() == 0) {
			System.out.println("no providers");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Provider>>(providers, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Provider------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/provider/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProvider(@PathVariable("id") int id) {
		logger.info("Fetching Provider with id {}", id);
		Provider provider = providerService.findById(id);
		if (provider == null) {
			logger.error("Provider with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Provider with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Provider>(provider, HttpStatus.OK);
	}

	// -------------------Create a
	// Provider-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/provider/", method = RequestMethod.POST)
	public ResponseEntity<?> createProvider(@RequestBody Provider provider, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Provider : {}", provider);

		if (providerService.isProviderExist(provider)) {
			logger.error("Unable to create. A Provider with name {} already exist", provider.getName());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A Provider with name " + provider.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}

		logger.info("Creating Provider : before save");
		providerService.saveProvider(provider);
		logger.info("Creating Provider : after save");

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/provider/{id}").buildAndExpand(provider.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Provider
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/provider/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProvider(@PathVariable("id") int id, @RequestBody Provider provider) {
		logger.info("Updating Provider with id {}", id);

		Provider currentProvider = providerService.findById(id);

		if (currentProvider == null) {
			logger.error("Unable to update. Provider with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Provider with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentProvider.setName(provider.getName());
		currentProvider.getLanguages().clear();
		currentProvider.setLanguages(provider.getLanguages());

		providerService.updateProvider(currentProvider);
		return new ResponseEntity<Provider>(currentProvider, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Provider-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/provider/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProvider(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Provider with id {}", id);

		Provider provider = providerService.findById(id);
		if (provider == null) {
			logger.error("Unable to delete. Provider with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Provider with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		providerService.deleteProviderById(id);
		return new ResponseEntity<Provider>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Providers-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/provider/", method = RequestMethod.DELETE)
	public ResponseEntity<Provider> deleteAllProviders() {
		logger.info("Deleting All Providers");

		providerService.deleteAllProviders();
		return new ResponseEntity<Provider>(HttpStatus.NO_CONTENT);
	}

}