package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Role;

public interface RoleService {

	Role findById(Integer id);

	Role findByRole(String name);

	void saveRole(Role role);

	void updateRole(Role role);

	void deleteRoleById(Integer id);

	void deleteAllRoles();

	List<Role> findAllRoles();

	Page<Role> findAllRolesByPage(Specification<Role> spec, Pageable pageable);

	boolean isRoleExist(Role role);

	List<Role> findDistinctRoles();

}