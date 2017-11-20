package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.File;

@Repository
public interface FileRepository extends PagingAndSortingRepository<File, Integer>, JpaSpecificationExecutor<File> {

	File findByFileName(String fileName);

}
