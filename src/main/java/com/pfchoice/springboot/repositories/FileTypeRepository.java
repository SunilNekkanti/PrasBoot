package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FileType;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface FileTypeRepository
		extends PagingAndSortingRepository<FileType, Integer>, JpaSpecificationExecutor<FileType>, RecordDetailsAwareRepository<FileType, Integer>   {

	FileType findByDescription(String description);
	
	FileType findByDescriptionEndingWithIgnoreCaseAndInsId(String description, Integer insId);

}
