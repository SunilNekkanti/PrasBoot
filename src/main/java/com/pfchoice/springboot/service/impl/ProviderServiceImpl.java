package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.Provider;
import com.pfchoice.springboot.model.keygenerator.PrasCacheKeyGenerator;
import com.pfchoice.springboot.repositories.ProviderRepository;
import com.pfchoice.springboot.service.ProviderService;

import java.util.List;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("providerService")
//@CacheDefaults(cacheName = "providers")
@Transactional
public class ProviderServiceImpl implements ProviderService {

	
	@Autowired
	private ProviderRepository providerRepository;

	//@CacheResult
	public Provider findById(Integer id) {
		System.out.println("***********checking providers cache******************************");
		return providerRepository.findOne(id);
	}

	//@CacheResult
	public Provider findByName(String name) {
		return providerRepository.findByName(name);
	}

	//@CachePut
	public void saveProvider(Provider provider) {
		providerRepository.save(provider);
	}

	//@CachePut
	public void updateProvider(Provider provider) {
		saveProvider(provider);
	}

	//@CacheRemove
	public void deleteProviderById(Integer id) {
		providerRepository.delete(id);
	}

	//@CacheRemoveAll
	public void deleteAllProviders() {
		providerRepository.deleteAll();
	}

	//@CacheResult
	public List<Provider> findAllProviders() {
		return (List<Provider>) providerRepository.findAll();
	}

	//@CacheResult(cacheKeyGenerator = PrasCacheKeyGenerator.class )
	public Page<Provider> findAllProvidersByPage(@CacheKey Specification<Provider> spec, Pageable pageable) {
		System.out.println("***********checking providers cache******************************");
		return providerRepository.findAll(spec, pageable);
	}

	public boolean isProviderExist(Provider provider) {
		return findByName(provider.getName()) != null;
	}
	
}
