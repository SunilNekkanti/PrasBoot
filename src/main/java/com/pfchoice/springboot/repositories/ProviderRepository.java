package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Provider;
import com.pfchoice.springboot.repositories.intf.ProviderAwareRepository;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface ProviderRepository
		extends PagingAndSortingRepository<Provider, Integer>, JpaSpecificationExecutor<Provider>, RecordDetailsAwareRepository<Provider, Integer>
, ProviderAwareRepository<Provider, Integer>{

	public Provider findById(Integer id);

	public Provider findByName(String name);

}
