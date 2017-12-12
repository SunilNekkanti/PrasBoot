package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.File;
import com.pfchoice.springboot.repositories.FileRepository;
import com.pfchoice.springboot.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("fileService")
@Transactional
public class FileServiceImpl implements FileService {

	@Autowired
	private FileRepository fileRepository;

	public File findById(Integer id) {
		return fileRepository.findOne(id);
	}

	public File findByFileName(String name) {
		return fileRepository.findByFileName(name);
	}

	public void saveFile(File file) {
		fileRepository.save(file);
	}

	public void updateFile(File file) {
		saveFile(file);
	}

	public void deleteFileById(Integer id) {
		fileRepository.delete(id);
	}

	public void deleteAllFiles() {
		fileRepository.deleteAll();
	}

	public Page<File> findAllFilesByPage(Specification<File> spec, Pageable pageable) {
		return fileRepository.findAll(spec, pageable);
	}

	public boolean isFileExist(File file) {
		return findByFileName(file.getFileName()) != null;
	}

}
