package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.Provider;
import com.pfchoice.springboot.repositories.ProviderRepository;
import com.pfchoice.springboot.service.ProviderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("providerService")
@Transactional
public class ProviderServiceImpl implements ProviderService {

	@Autowired
	private ProviderRepository providerRepository;

	public Provider findById(Integer id) {
		return providerRepository.findOne(id);
	}

	public Provider findByName(String name) {
		return providerRepository.findByName(name);
	}

	public void saveProvider(Provider provider) {
		providerRepository.save(provider);
	}

	public void updateProvider(Provider provider) {
		saveProvider(provider);
	}

	public void deleteProviderById(Integer id) {
		providerRepository.delete(id);
	}

	public void deleteAllProviders() {
		providerRepository.deleteAll();
	}

	public List<Provider> findAllProviders() {
		return (List<Provider>) providerRepository.findAll();
	}

	public Page<Provider> findAllProvidersByPage(Specification<Provider> spec, Pageable pageable) {
		return providerRepository.findAll(spec, pageable);
	}

	public boolean isProviderExist(Provider provider) {
		return findByName(provider.getName()) != null;
	}

}
