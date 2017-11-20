package com.pfchoice.springboot.service;


import com.pfchoice.springboot.model.CurrentUser;

public interface CurrentUserService {

	CurrentUser findById(Integer id);

	CurrentUser findByCurrentUsername(String name);

	boolean isCurrentUserExist(CurrentUser user);

}