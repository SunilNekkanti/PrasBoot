package com.pfchoice.springboot.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import com.pfchoice.springboot.model.FileUploadContent;

public interface FileUploadContentService {

	FileUploadContent findById(Integer id);

	FileUploadContent findByFileName(String fileName);

	void saveFileUploadContent(FileUploadContent fileUploadContent);

	void updateFileUploadContent(FileUploadContent fileUploadContent);

	void deleteFileUploadContentById(Integer id);

	void deleteAllFileUploadContents();

	List<FileUploadContent> findAllFileUploadContents();

	boolean isFileUploadContentExists(FileUploadContent fileUploadContent);

	Future<?> asyncFileUploadProcessing(String username, Integer insId, Integer fileTypeId, Integer activityMonth,
			Integer reportMonth, String fileName) throws IOException;
}