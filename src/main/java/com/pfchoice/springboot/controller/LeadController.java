package com.pfchoice.springboot.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

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

import com.pfchoice.springboot.model.CurrentUser;
import com.pfchoice.springboot.model.Email;
import com.pfchoice.springboot.model.LeadMembership;
import com.pfchoice.springboot.model.LeadNotes;
import com.pfchoice.springboot.model.User;
import com.pfchoice.springboot.repositories.specifications.LeadSpecifications;
import com.pfchoice.springboot.service.CurrentUserService;
import com.pfchoice.springboot.service.EmailService;
import com.pfchoice.springboot.service.LeadMembershipService;
import com.pfchoice.springboot.service.UserService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class LeadController {

	public static final Logger logger = LoggerFactory.getLogger(LeadController.class);

	@Autowired
	LeadMembershipService leadService; // Service which will do all data
										// retrieval/manipulation work

	@Autowired
	UserService userService; // Service which will do all data
								// retrieval/manipulation work
	@Autowired
	CurrentUserService currentUserService; // Service which will do all data
	// retrieval/manipulation work

	@Autowired
	EmailService emailService;

	// -------------------Retrieve All
	// LeadMemberships---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/lead/", method = RequestMethod.GET)
	public ResponseEntity<Page<LeadMembership>> listAllLeadMemberships(
			@PageableDefault(page=0 ,size=100) Pageable pageRequest,
			@RequestParam(value = "search", required = false) String search, @RequestParam(value = "selectedTab", required = false) String selectedTab,
			@ModelAttribute("userId") Integer userId, @ModelAttribute("roleName") String roleName, @ModelAttribute("username") String username)
			throws MessagingException, IOException {

		Specification<LeadMembership> spec = new LeadSpecifications(userId, username, roleName, search, selectedTab);
		Page<LeadMembership> leads = leadService.findAllLeadMembershipsByPage(spec, pageRequest);
		if (leads.getTotalElements() == 0) {
			logger.info("no leads");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Page<LeadMembership>>(leads, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// LeadMembership------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/lead/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getLeadMembership(@PathVariable("id") int id) {
		logger.info("Fetching LeadMembership with id {}", id);
		LeadMembership lead = leadService.findById(id);
		if (lead == null) {
			logger.error("LeadMembership with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("LeadMembership with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<LeadMembership>(lead, HttpStatus.OK);
	}

	// -------------------Create a
	// LeadMembership-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/lead/", method = RequestMethod.POST)
	public ResponseEntity<?> createLeadMembership(@RequestBody LeadMembership lead, UriComponentsBuilder ucBuilder,
			@ModelAttribute("userId") Integer userId, 
			@ModelAttribute("roleName") String roleName,@ModelAttribute("username") String username) throws Exception {
		logger.info("Creating LeadMembership : {}", lead);

		logger.info("lead"+lead.getStatus());
		final String firstName = lead.getFirstName();
		final String lastName = lead.getLastName();
		final String address1 = lead.getContact().getAddress1();
		final String phoneNumber = lead.getContact().getHomePhone();

		if (leadService.isLeadMembershipExists(firstName, lastName, address1, phoneNumber)) {
			logger.error("Unable to create. A LeadMembership with name {} already exist",
					lead.getFirstName() + lead.getLastName());
			return new ResponseEntity(new CustomErrorType("Unable to create. A LeadMembership with name "
					+ lead.getFirstName() + " " + lead.getLastName() + " already exist."), HttpStatus.CONFLICT);
		}
		StringBuffer emailBody = new StringBuffer();
		User user = userService.findById(userId);
		List<LeadNotes> leadNotes = lead.getLeadNotes();
		if (leadNotes != null) {
			leadNotes.forEach(ln -> {
				ln.setUser(user);
				ln.setLead(lead);
				ln.setUpdatedBy(username);
				ln.setCreatedBy(username);
				emailBody.append(ln.getNotes());
			});
		} else {
			emailBody.append("");
		}

		lead.setLeadNotes(leadNotes);
		lead.setCreatedBy(username);
		lead.setUpdatedBy(username);
		leadService.saveLeadMembership(lead);

		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		String currentLocalTime = sdf1.format((new Date()).getTime());
		
		/*
		 * String toEmailIds =
		 * lead.getEvent().getRepresentatives().stream().map(rep ->
		 * rep.getEmail()) .collect(Collectors.joining(","));
		 */
		Email mail = new Email();
		mail.setEmailTo("adonay@infocusonline.net");  
		mail.setEmailFrom("leadmanagement@infocusonline.net");
		mail.setEmailCc("leadmanagement@infocusonline.net");
		mail.setSubject("New lead : "+lead.getLastName()+","+lead.getFirstName()+" has been created:");

		String careCoordinator = "Adonay Fresnada";
		
		Map<String, Object> emailAttributes = new HashMap<>();
		emailAttributes.put("currentUser", user.getName());
		emailAttributes.put("currentUserRole", roleName);
		emailAttributes.put("currentLocalTime", currentLocalTime);
		emailAttributes.put("careCoordinator", careCoordinator);
		emailAttributes.put("firstName", lead.getFirstName());
		emailAttributes.put("lastName", lead.getLastName());
		
		String emailTemplateFileName = "lead_create_email_template_" + roleName + ".txt";
		mail.setBody(emailService.geContentFromTemplate(emailAttributes, emailTemplateFileName));

		emailService.sendMail(mail);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/lead/{id}").buildAndExpand(lead.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a LeadMembership
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/lead/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLeadMembership(@PathVariable("id") int id, @RequestBody LeadMembership lead,
			@ModelAttribute("userId") Integer userId, @ModelAttribute("roleName") String roleName,
			@ModelAttribute("username") String username) throws MessagingException, InterruptedException, IOException {
		logger.info("Updating LeadMembership with id {}", id);

		LeadMembership currentLeadMembership = leadService.findById(id);
		
		CurrentUser loginUser = currentUserService.findById(userId);
		if (currentLeadMembership == null) {
			logger.error("Unable to update. LeadMembership with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. LeadMembership with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentLeadMembership.setFirstName(lead.getFirstName());
		currentLeadMembership.setLastName(lead.getLastName());
		currentLeadMembership.setDob(lead.getDob());
		currentLeadMembership.setEthinicCode(lead.getEthinicCode());
		currentLeadMembership.setPlanType(lead.getPlanType());
		currentLeadMembership.setInitialInsurance(lead.getInitialInsurance());
		currentLeadMembership.setGender(lead.getGender());
		currentLeadMembership.setStatus(lead.getStatus());
		currentLeadMembership.setLanguage(lead.getLanguage());
		currentLeadMembership.setBestTimeToCall(lead.getBestTimeToCall());
		currentLeadMembership.setStatus(lead.getStatus());
		lead.getLeadMembershipFlag().setLead(currentLeadMembership);
		currentLeadMembership.setLeadMembershipFlag(lead.getLeadMembershipFlag());
	//	currentLeadMembership.setStatusDetail(lead.getStatusDetail());
		currentLeadMembership.setFileUpload(lead.getFileUpload());
		currentLeadMembership.setConsentFormSigned(lead.getConsentFormSigned());
		currentLeadMembership.setContact(lead.getContact());
		currentLeadMembership.setPrvdr(lead.getPrvdr());
		currentLeadMembership.setInsurance(lead.getInsurance());
		currentLeadMembership.setEffectiveFrom(lead.getEffectiveFrom());
		

		List<LeadNotes> leadNotes = new ArrayList<>();
		if (lead.getLeadNotes() != null) {
			for (LeadNotes ln : lead.getLeadNotes()) {
				if (!"".equals(ln.getNotes().trim())) {
					ln.setLead(currentLeadMembership);
					ln.setCreatedBy(loginUser.getUsername());
					ln.setUpdatedBy(loginUser.getUsername());
					leadNotes.add(ln);
				}
			}
		}

		if (leadNotes.size() > 0) {
			currentLeadMembership.getLeadNotes().clear();
			currentLeadMembership.getLeadNotes().addAll(leadNotes);
		}


		currentLeadMembership.setUpdatedBy(username);
		leadService.updateLeadMembership(currentLeadMembership);
		
		return new ResponseEntity<LeadMembership>(currentLeadMembership, HttpStatus.OK);
	}


	// ------------------- Delete a
	// LeadMembership-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/lead/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLeadMembership(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting LeadMembership with id {}", id);

		LeadMembership lead = leadService.findById(id);
		if (lead == null) {
			logger.error("Unable to delete. LeadMembership with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. LeadMembership with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		leadService.deleteLeadMembershipById(id);
		return new ResponseEntity<LeadMembership>(HttpStatus.NO_CONTENT);
	}

}