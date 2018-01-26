package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.FileType;
import com.pfchoice.springboot.repositories.FileTypeRepository;
import com.pfchoice.springboot.service.FileTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("fileTypeService")
@Transactional
public class FileTypeServiceImpl implements FileTypeService {

	@Autowired
	private FileTypeRepository fileTypeRepository;

	public FileType findById(Integer id) {
		return fileTypeRepository.findOne(id);
	}

	public FileType findByDescription(String description) {
		return fileTypeRepository.findByDescription(description);
	}
	
	public FileType findByDescriptionAndInsId(String description, Integer insId){
		return fileTypeRepository.findByDescriptionEndingWithIgnoreCaseAndInsId(description,insId);
	}
	

	public void saveFileType(FileType fileType) {
		fileTypeRepository.save(fileType);
	}

	public void updateFileType(FileType fileType) {
		saveFileType(fileType);
	}

	public void deleteFileTypeById(Integer id) {
		fileTypeRepository.delete(id);
	}

	public void deleteAllFileTypes() {
		fileTypeRepository.deleteAll();
	}

	public Page<FileType> findAllFileTypesByPage(Specification<FileType> spec, Pageable pageable) {
		return fileTypeRepository.findAll(spec, pageable);
	}

	public boolean isFileTypeExist(FileType fileType) {
		return findByDescription(fileType.getDescription()) != null;
	}

}
