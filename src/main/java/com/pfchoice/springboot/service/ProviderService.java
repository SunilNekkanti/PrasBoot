package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Provider;

public interface ProviderService {

	Provider findById(Integer id);

	Provider findByName(String name);

	void saveProvider(Provider provider);

	void updateProvider(Provider provider);

	void deleteProviderById(Integer id);

	void deleteAllProviders();

	List<Provider> findAllProviders();

	Page<Provider> findAllProvidersByPage(Specification<Provider> spec, Pageable pageable);

	boolean isProviderExist(Provider provider);
}