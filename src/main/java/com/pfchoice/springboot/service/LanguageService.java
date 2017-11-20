package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Language;

public interface LanguageService {

	Language findById(Short id);

	Language findByDescription(String description);

	void saveLanguage(Language language);

	void updateLanguage(Language language);

	void deleteLanguageById(Short id);

	void deleteAllLanguages();

	List<Language> findAllLanguages();

	Page<Language> findAllLanguagesByPage(Specification<Language> spec, Pageable pageable);

	boolean isLanguageExist(Language language);
}