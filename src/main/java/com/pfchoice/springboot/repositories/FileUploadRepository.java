package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FileUpload;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Integer> {

	public FileUpload findByFileName(String fileName);
}
