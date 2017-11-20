package com.pfchoice.springboot.service.impl;

import java.util.List;
import java.util.TreeSet;

import com.pfchoice.springboot.model.Role;
import com.pfchoice.springboot.repositories.RoleRepository;
import com.pfchoice.springboot.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public Role findById(Integer id) {
		return roleRepository.findOne(id);
	}

	public Role findByRole(String name) {
		return roleRepository.findByRole(name);
	}

	public void saveRole(Role role) {
		roleRepository.save(role);
	}

	public void updateRole(Role role) {
		saveRole(role);
	}

	public void deleteRoleById(Integer id) {
		roleRepository.delete(id);
	}

	public void deleteAllRoles() {
		roleRepository.deleteAll();
	}

	public Page<Role> findAllRolesByPage(Specification<Role> spec, Pageable pageable) {
		return roleRepository.findAll(spec, pageable);
	}

	public List<Role> findAllRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	public boolean isRoleExist(Role role) {
		return findByRole(role.getRole()) != null;
	}

	@SuppressWarnings("unchecked")
	public List<Role> findDistinctRoles() {
		List<Role> roles = findAllRoles();

		List<Role> uniqueRoles = roles.stream().collect(
				collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(Role::getId))), ArrayList::new));
		return uniqueRoles;
	}

}
