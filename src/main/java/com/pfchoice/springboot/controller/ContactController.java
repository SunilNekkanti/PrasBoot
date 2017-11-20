package com.pfchoice.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.model.Contact;
import com.pfchoice.springboot.service.ContactService;
import com.pfchoice.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ContactController {

	public static final Logger logger = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	ContactService contactService; // Service which will do all data
									// retrieval/manipulation work

	// -------------------Retrieve All
	// Contacts---------------------------------------------

	@Secured({ "ROLE_ADMIN", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/contact/", method = RequestMethod.GET)
	public ResponseEntity<List<Contact>> listAllContacts() {
		List<Contact> contacts = contactService.findAllContacts();
		if (contacts.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Contact>>(contacts, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Contact------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getContact(@PathVariable("id") int id) {
		logger.info("Fetching Contact with id {}", id);
		Contact contact = contactService.findById(id);
		if (contact == null) {
			logger.error("Contact with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Contact with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Contact>(contact, HttpStatus.OK);
	}

	// -------------------Create a
	// Contact-------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/contact/", method = RequestMethod.POST)
	public ResponseEntity<?> createContact(@RequestBody Contact contact, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Contact : {}", contact);

		if (contactService.isContactExist(contact)) {
			logger.error("Unable to create. A Contact with name {} already exist", contact.getId());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Contact with name " + contact.getId() + " already exist."),
					HttpStatus.CONFLICT);
		}
		contactService.saveContact(contact);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/contact/{id}").buildAndExpand(contact.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Contact
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/contact/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateContact(@PathVariable("id") int id, @RequestBody Contact contact) {
		logger.info("Updating Contact with id {}", id);

		Contact currentContact = contactService.findById(id);

		if (currentContact == null) {
			logger.error("Unable to update. Contact with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Contact with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentContact.setAddress1(contact.getAddress1());
		currentContact.setAddress2(contact.getAddress2());
		currentContact.setCity(contact.getCity());
		currentContact.setContactPerson(contact.getContactPerson());
		currentContact.setEmail(contact.getEmail());
		currentContact.setExtension(contact.getExtension());
		currentContact.setHomePhone(contact.getHomePhone());
		currentContact.setMobilePhone(contact.getMobilePhone());
		currentContact.setStateCode(contact.getStateCode());
		contactService.updateContact(currentContact);
		return new ResponseEntity<Contact>(currentContact, HttpStatus.OK);
	}

	// ------------------- Delete a
	// Contact-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/contact/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteContact(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting Contact with id {}", id);

		Contact contact = contactService.findById(id);
		if (contact == null) {
			logger.error("Unable to delete. Contact with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Contact with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		contactService.deleteContactById(id);
		return new ResponseEntity<Contact>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Contacts-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/contact/", method = RequestMethod.DELETE)
	public ResponseEntity<Contact> deleteAllContacts() {
		logger.info("Deleting All Contacts");

		contactService.deleteAllContacts();
		return new ResponseEntity<Contact>(HttpStatus.NO_CONTENT);
	}

}