package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.FileUpload;

public interface FileUploadService {

	FileUpload findById(Integer id);

	FileUpload findByFileName(String fileName);

	void saveFileUpload(FileUpload fileUpload);

	void updateFileUpload(FileUpload fileUpload);

	void deleteFileUploadById(Integer id);

	void deleteAllFileUploads();

	List<FileUpload> findAllFileUploads();

	boolean isFileUploadExists(FileUpload fileUpload);
}