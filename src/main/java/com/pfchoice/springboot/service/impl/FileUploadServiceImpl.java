package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.FileUpload;
import com.pfchoice.springboot.repositories.FileUploadRepository;
import com.pfchoice.springboot.service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("fileUploadService")
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

	@Autowired
	private FileUploadRepository fileUploadRepository;

	public FileUpload findById(Integer id) {
		return fileUploadRepository.findOne(id);
	}

	public FileUpload findByFileName(String fileName) {
		return fileUploadRepository.findByFileName(fileName);
	}

	public void saveFileUpload(FileUpload fileUpload) {
		fileUploadRepository.save(fileUpload);
	}

	public void updateFileUpload(FileUpload fileUpload) {
		saveFileUpload(fileUpload);
	}

	public void deleteFileUploadById(Integer id) {
		fileUploadRepository.delete(id);
	}

	public void deleteAllFileUploads() {
		fileUploadRepository.deleteAll();
	}

	public List<FileUpload> findAllFileUploads() {
		return fileUploadRepository.findAll();
	}

	public boolean isFileUploadExists(FileUpload fileUpload) {
		return findByFileName(fileUpload.getFileName()) != null;
	}

}
