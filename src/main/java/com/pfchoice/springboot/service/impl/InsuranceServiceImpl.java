package com.pfchoice.springboot.service.impl;

import java.util.List;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;

import com.pfchoice.springboot.model.Insurance;
import com.pfchoice.springboot.repositories.InsuranceRepository;
import com.pfchoice.springboot.service.InsuranceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("insuranceService")
//@CacheDefaults(cacheName = "insurances")
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

	@Autowired
	private InsuranceRepository insuranceRepository;

//	@CacheResult
	public Insurance findById(Integer id) {
		System.out.println("checking insurance cache");
		return insuranceRepository.findOne(id);
	}

//	@CacheResult
	public Insurance findByName(String name) {
		return insuranceRepository.findByName(name);
	}

//	@CachePut(key="#result.id")
	public void saveInsurance(Insurance insurance) {
		insuranceRepository.save(insurance);
	}

//	@CachePut(key="#insurance.id")
	public void updateInsurance(Insurance insurance) {
		saveInsurance(insurance);
	}

//	@CacheRemove
	public void deleteInsuranceById(Integer id) {
		insuranceRepository.delete(id);
	}

//	@CacheRemoveAll
	public void deleteAllInsurances() {
		insuranceRepository.deleteAll();
	}

//	@CacheResult
	public List<Insurance> findAllInsurances() {
		return (List<Insurance>) insuranceRepository.findAll();
	}

//	@CacheResult
	public Page<Insurance> findAllInsurancesByPage(Specification<Insurance> spec, Pageable pageable) {
		System.out.println("***********checking insurances cache******************************");
		return insuranceRepository.findAll(spec, pageable);
	}

	public boolean isInsuranceExist(Insurance insurance) {
		return findByName(insurance.getName()) != null;
	}

}
