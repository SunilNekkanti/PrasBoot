package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.County;

public interface CountyService {

	County findById(Integer id);

	County findByDescription(String code);

	void saveCounty(County county);

	void updateCounty(County county);

	void deleteCountyById(Integer id);

	void deleteAllCountys();

	List<County> findAllCountys();

	boolean isCountyExist(County county);
}