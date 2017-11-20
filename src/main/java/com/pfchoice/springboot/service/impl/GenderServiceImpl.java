package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.Gender;
import com.pfchoice.springboot.repositories.GenderRepository;
import com.pfchoice.springboot.service.GenderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("genderService")
@Transactional
public class GenderServiceImpl implements GenderService {

	@Autowired
	private GenderRepository genderRepository;

	public Gender findById(Byte id) {
		return genderRepository.findOne(id);
	}

	public Gender findByCode(char name) {
		return genderRepository.findByCode(name);
	}

	public Gender findByDescription(String name) {
		return genderRepository.findByDescription(name);
	}

	public void saveGender(Gender gender) {
		genderRepository.save(gender);
	}

	public void updateGender(Gender gender) {
		saveGender(gender);
	}

	public void deleteGenderById(Byte id) {
		genderRepository.delete(id);
	}

	public void deleteAllGenders() {
		genderRepository.deleteAll();
	}

	public List<Gender> findAllGenders() {
		return genderRepository.findAll();
	}

	public boolean isGenderExist(Gender gender) {
		return findById(gender.getId()) != null;
	}

}
