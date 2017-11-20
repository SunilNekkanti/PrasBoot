package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.File;

public interface FileService {

	File findById(Integer id);

	File findByFileName(String name);

	void saveFile(File file);

	void updateFile(File file);

	void deleteFileById(Integer id);

	void deleteAllFiles();

	Page<File> findAllFilesByPage(Specification<File> spec, Pageable pageable);

	boolean isFileExist(File frequencyType);

}