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

import com.pfchoice.springboot.model.Role;
import com.pfchoice.springboot.repositories.specifications.RoleSpecifications;
import com.pfchoice.springboot.service.RoleService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RoleController {

	public static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	RoleService roleService; // Service which will do all data
								// retrieval/manipulation work

	// -------------------Retrieve All
	// Roles---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/role/", method = RequestMethod.GET)
	public ResponseEntity<Page<Role>> listAllRoles(@PageableDefault(page = 0, size = 100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search) {

		Specification<Role> spec = new RoleSpecifications(search);
		Page<Role> roles = roleService.findAllRolesByPage(spec, pageRequest);

		if (roles.getTotalElements() == 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<Role>>(roles, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Role------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
	@RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getRole(@PathVariable("id") int id) {
		logger.info("Fetching Role with id {}", id);
		Role role = roleService.findById(id);
		if (role == null) {
			logger.error("Role with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Role with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}

	// -------------------Create a
	// Role-------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/role/", method = RequestMethod.POST)
	public ResponseEntity<?> createRole(@RequestBody Role role, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Role : {}", role);

		if (roleService.isRoleExist(role)) {
			logger.error("Unable to create. A Role with name {} already exist", role.getRole());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Role with name " + role.getRole() + " already exist."),
					HttpStatus.CONFLICT);
		}
		role.setCreatedBy("sarath");
		role.setUpdatedBy("sarath");
		roleService.saveRole(role);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/role/{id}").buildAndExpand(role.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Role
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateRole(@PathVariable("id") int id, @RequestBody Role role) {
		logger.info("Updating Role with id {}", id);

		Role currentRole = roleService.findById(id);

		if (currentRole == null) {
			logger.error("Unable to update. Role with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Role with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentRole.setRole(role.getRole());

		roleService.updateRole(currentRole);
		return new ResponseEntity<Role>(currentRole, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Role-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRole(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Role with id {}", id);

		Role role = roleService.findById(id);
		if (role == null) {
			logger.error("Unable to delete. Role with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Role with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		roleService.deleteRoleById(id);
		return new ResponseEntity<Role>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Roles-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/role/", method = RequestMethod.DELETE)
	public ResponseEntity<Role> deleteAllRoles() {
		logger.info("Deleting All Roles");

		roleService.deleteAllRoles();
		return new ResponseEntity<Role>(HttpStatus.NO_CONTENT);
	}

}