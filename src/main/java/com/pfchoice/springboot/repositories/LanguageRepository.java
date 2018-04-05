package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Language;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface LanguageRepository
		extends PagingAndSortingRepository<Language, Short>, JpaSpecificationExecutor<Language>, RecordDetailsAwareRepository<Language, Short>{

	public Language findById(Short id);

	public Language findByDescription(String description);

}
