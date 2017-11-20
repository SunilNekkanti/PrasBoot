package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.Gender;

public interface GenderService {

	Gender findById(Byte id);

	Gender findByCode(char code);

	Gender findByDescription(String code);

	void saveGender(Gender gender);

	void updateGender(Gender gender);

	void deleteGenderById(Byte id);

	void deleteAllGenders();

	List<Gender> findAllGenders();

	boolean isGenderExist(Gender gender);
}