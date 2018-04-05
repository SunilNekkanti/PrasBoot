package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FileUploadContent;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface FileUploadContentRepository extends JpaRepository<FileUploadContent, Integer>, RecordDetailsAwareRepository<FileUploadContent, Integer> {

	public FileUploadContent findByFileName(String fileName);
}
