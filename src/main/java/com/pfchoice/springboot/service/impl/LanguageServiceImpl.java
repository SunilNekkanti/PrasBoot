package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.Language;
import com.pfchoice.springboot.repositories.LanguageRepository;
import com.pfchoice.springboot.service.LanguageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("LanguageService")
@Transactional
public class LanguageServiceImpl implements LanguageService {

	@Autowired
	private LanguageRepository languageRepository;

	public Language findById(Short id) {
		return languageRepository.findById(id);
	}

	public Language findByDescription(String description) {
		return languageRepository.findByDescription(description);
	}

	public void saveLanguage(Language language) {
		languageRepository.save(language);
	}

	public void updateLanguage(Language language) {
		saveLanguage(language);
	}

	public void deleteLanguageById(Short id) {
		languageRepository.delete(id);
	}

	public void deleteAllLanguages() {
		languageRepository.deleteAll();
	}

	public List<Language> findAllLanguages() {
		return (List<Language>) languageRepository.findAll();
	}

	public Page<Language> findAllLanguagesByPage(Specification<Language> spec, Pageable pageable) {
		return languageRepository.findAll(spec, pageable);
	}

	public boolean isLanguageExist(Language language) {
		return findByDescription(language.getDescription()) != null;
	}

}
