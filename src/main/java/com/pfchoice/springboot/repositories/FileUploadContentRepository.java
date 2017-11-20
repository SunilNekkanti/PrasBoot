package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FileUploadContent;

@Repository
public interface FileUploadContentRepository extends JpaRepository<FileUploadContent, Integer> {

	public FileUploadContent findByFileName(String fileName);
}
