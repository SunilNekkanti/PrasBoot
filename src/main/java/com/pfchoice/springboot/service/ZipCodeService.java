package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.ZipCode;

public interface ZipCodeService {

	ZipCode findByCode(Integer code);

	void saveZipCode(ZipCode zipcode);

	void updateZipCode(ZipCode zipcode);

	void deleteZipCodeById(Integer id);

	void deleteAllZipCodes();

	List<ZipCode> findAllZipCodes();

	boolean isZipCodeExist(ZipCode zipcode);
}