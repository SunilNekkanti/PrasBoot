package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Provider;

@Repository
public interface ProviderRepository
		extends PagingAndSortingRepository<Provider, Integer>, JpaSpecificationExecutor<Provider> {

	public Provider findById(Integer id);

	public Provider findByName(String name);

}
