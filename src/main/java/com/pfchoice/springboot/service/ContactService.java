package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.Contact;

public interface ContactService {

	Contact findById(Integer id);

	void saveContact(Contact contact);

	void updateContact(Contact contact);

	void deleteContactById(Integer id);

	void deleteAllContacts();

	List<Contact> findAllContacts();

	boolean isContactExist(Contact contact);
}