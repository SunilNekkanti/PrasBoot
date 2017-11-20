package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.County;
import com.pfchoice.springboot.repositories.CountyRepository;
import com.pfchoice.springboot.service.CountyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("countyService")
@Transactional
public class CountyServiceImpl implements CountyService {

	@Autowired
	private CountyRepository countyRepository;

	public County findById(Integer id) {
		return countyRepository.findOne(id);
	}

	public County findByDescription(String name) {
		return countyRepository.findByDescription(name);
	}

	public void saveCounty(County county) {
		countyRepository.save(county);
	}

	public void updateCounty(County county) {
		saveCounty(county);
	}

	public void deleteCountyById(Integer id) {
		countyRepository.delete(id);
	}

	public void deleteAllCountys() {
		countyRepository.deleteAll();
	}

	public List<County> findAllCountys() {
		return countyRepository.findAll();
	}

	public boolean isCountyExist(County county) {
		return findByDescription(county.getDescription()) != null;
	}

}
