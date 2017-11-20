package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.CurrentUser;
import com.pfchoice.springboot.repositories.CurrentUserRepository;
import com.pfchoice.springboot.service.CurrentUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("currentUserService")
@Transactional
public class CurrentUserServiceImpl implements CurrentUserService {

	@Autowired
	private CurrentUserRepository userRepository;

	public CurrentUser findById(Integer id) {
		return userRepository.findOne(id);
	}

	public CurrentUser findByCurrentUsername(String name) {
		return userRepository.findByUsername(name);
	}

	public boolean isCurrentUserExist(CurrentUser user) {
		return findByCurrentUsername(user.getUsername()) != null;
	}

}
