package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.ZipCode;
import com.pfchoice.springboot.repositories.ZipCodeRepository;
import com.pfchoice.springboot.service.ZipCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("zipCodeService")
@Transactional
public class ZipCodeServiceImpl implements ZipCodeService {

	@Autowired
	private ZipCodeRepository zipCodeRepository;

	public ZipCode findByCode(Integer code) {
		return zipCodeRepository.findByCode(code);
	}

	public void saveZipCode(ZipCode zipCode) {
		zipCodeRepository.save(zipCode);
	}

	public void updateZipCode(ZipCode zipCode) {
		saveZipCode(zipCode);
	}

	public void deleteZipCodeById(Integer id) {
		zipCodeRepository.delete(id);
	}

	public void deleteAllZipCodes() {
		zipCodeRepository.deleteAll();
	}

	public List<ZipCode> findAllZipCodes() {
		return zipCodeRepository.findAll();
	}

	public boolean isZipCodeExist(ZipCode zipCode) {
		return findByCode(zipCode.getCode()) != null;
	}

}
