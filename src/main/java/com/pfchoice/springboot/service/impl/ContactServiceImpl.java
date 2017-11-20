package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.Contact;
import com.pfchoice.springboot.repositories.ContactRepository;
import com.pfchoice.springboot.service.ContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("contactService")
@Transactional
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository userRepository;

	public Contact findById(Integer id) {
		return userRepository.findOne(id);
	}

	public void saveContact(Contact contact) {
		userRepository.save(contact);
	}

	public void updateContact(Contact contact) {
		saveContact(contact);
	}

	public void deleteContactById(Integer id) {
		userRepository.delete(id);
	}

	public void deleteAllContacts() {
		userRepository.deleteAll();
	}

	public List<Contact> findAllContacts() {
		return userRepository.findAll();
	}

	public boolean isContactExist(Contact contact) {
		return findById(contact.getId()) != null;
	}

}
