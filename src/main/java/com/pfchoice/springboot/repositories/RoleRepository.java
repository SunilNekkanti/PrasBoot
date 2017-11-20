package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Role;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

	Role findByRole(String name);

}
