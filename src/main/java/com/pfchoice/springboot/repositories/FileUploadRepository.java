package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FileUpload;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Integer>, RecordDetailsAwareRepository<FileUpload, Integer>  {

	public FileUpload findByFileName(String fileName);
}
