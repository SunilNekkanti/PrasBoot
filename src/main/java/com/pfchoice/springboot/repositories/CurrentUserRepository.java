package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.CurrentUser;

@Repository
public interface CurrentUserRepository
		extends PagingAndSortingRepository<CurrentUser, Integer>, JpaSpecificationExecutor<CurrentUser> {

	CurrentUser findByUsername(String name);
	
	@Query(value="select u from com.pfchoice.springboot.model.CurrentUser u where id=:id")
	CurrentUser findByUserId(@Param("id") Integer id);

}
