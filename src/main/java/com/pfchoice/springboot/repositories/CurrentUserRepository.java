package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.CurrentUser;

@Repository
public interface CurrentUserRepository extends PagingAndSortingRepository<CurrentUser, Integer>, JpaSpecificationExecutor<CurrentUser> {

	CurrentUser findByUsername(String name);

}
