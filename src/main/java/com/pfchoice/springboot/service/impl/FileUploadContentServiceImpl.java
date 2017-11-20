package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.FileUploadContent;
import com.pfchoice.springboot.repositories.FileUploadContentRepository;
import com.pfchoice.springboot.service.FileUploadContentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("fileUploadContentService")
@Transactional
public class FileUploadContentServiceImpl implements FileUploadContentService {

	@Autowired
	private FileUploadContentRepository fileUploadContentRepository;

	public FileUploadContent findById(Integer id) {
		return fileUploadContentRepository.findOne(id);
	}

	public FileUploadContent findByFileName(String fileName) {
		return fileUploadContentRepository.findByFileName(fileName);
	}

	public void saveFileUploadContent(FileUploadContent fileUploadContent) {
		fileUploadContentRepository.save(fileUploadContent);
	}

	public void updateFileUploadContent(FileUploadContent fileUploadContent) {
		saveFileUploadContent(fileUploadContent);
	}

	public void deleteFileUploadContentById(Integer id) {
		fileUploadContentRepository.delete(id);
	}

	public void deleteAllFileUploadContents() {
		fileUploadContentRepository.deleteAll();
	}

	public List<FileUploadContent> findAllFileUploadContents() {
		return fileUploadContentRepository.findAll();
	}

	public boolean isFileUploadContentExists(FileUploadContent fileUploadContent) {
		return findByFileName(fileUploadContent.getFileName()) != null;
	}

}
