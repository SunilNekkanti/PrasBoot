package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FileType;

@Repository
public interface FileTypeRepository extends PagingAndSortingRepository<FileType, Integer>, JpaSpecificationExecutor<FileType> {

	FileType findByDescription(String description);

}
