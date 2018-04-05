package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.File;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface FileRepository extends PagingAndSortingRepository<File, Integer>, JpaSpecificationExecutor<File>, RecordDetailsAwareRepository<File, Integer>  {

	File findByFileName(String fileName);

}
