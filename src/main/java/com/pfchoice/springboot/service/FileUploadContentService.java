package com.pfchoice.springboot.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

	CompletableFuture<?> asyncMbrRosterOrCapFileUploadProcessing(String username, Integer insId, Integer fileTypeId, Integer activityMonth,
			Integer reportMonth, String fileName) throws IOException, InterruptedException;
	

	CompletableFuture<?> asyncMbrClaimsFileUploadProcessing(String username, Integer insId, Integer fileTypeId, Integer activityMonth,
			Integer reportMonth, String fileName) throws IOException, InterruptedException;
	
	CompletableFuture<?> asyncMbrLevelOrPrvdrAdjustFileUploadProcessing(String username, Integer insId, Integer fileTypeId,
			Integer activityMonth, Integer reportMonth, String fileName) throws IOException, InterruptedException;
	
	CompletableFuture<?> asyncMbrMedicalRiskAdjustFileUploadProcessing(final String username, final Integer insId, final Integer fileTypeId,
			final Integer activityMonth, final Integer reportMonth, final String fileName) throws IOException, InterruptedException;
	
	CompletableFuture<?> asyncMbrPharmacyClaimsFileUploadProcessing(final String username, final Integer insId, final Integer fileTypeId,
				final Integer activityMonth, final Integer reportMonth, final String fileName) throws IOException, InterruptedException;
}