package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.User;
import com.pfchoice.springboot.repositories.UserRepository;
import com.pfchoice.springboot.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	public User findById(Integer id) {
		return userRepository.findOne(id);
	}

	public User findByUsername(String name) {
		return userRepository.findByUsername(name);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public void updateUser(User user) {
		saveUser(user);
	}

	public void deleteUserById(Integer id) {
		userRepository.delete(id);
	}

	public void deleteAllUsers() {
		userRepository.deleteAll();
	}

	public Page<User> findAllUsersByPage(Specification<User> spec, Pageable pageable) {
		return userRepository.findAll(spec, pageable);
	}

	public boolean isUserExist(User user) {
		return findByUsername(user.getUsername()) != null;
	}

}
