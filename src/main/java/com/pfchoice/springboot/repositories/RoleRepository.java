package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Role;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer>, JpaSpecificationExecutor<Role>, RecordDetailsAwareRepository<Role, Integer>  {

	Role findByRole(String name);

}
