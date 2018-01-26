package com.pfchoice.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.FileType;

public interface FileTypeService {

	FileType findById(Integer id);

	FileType findByDescription(String description);
	
	FileType findByDescriptionAndInsId(String description, Integer insId);

	void saveFileType(FileType fileType);

	void updateFileType(FileType fileType);

	void deleteFileTypeById(Integer id);

	void deleteAllFileTypes();

	Page<FileType> findAllFileTypesByPage(Specification<FileType> spec, Pageable pageable);

	boolean isFileTypeExist(FileType fileType);

}