package com.pfchoice.springboot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pfchoice.springboot.model.Role;
import com.pfchoice.springboot.repositories.CurrentUserRepository;

/**
 *
 * @author Sarath
 */

@Service
@Transactional
@Primary
public class AuthenticationServiceImpl implements UserDetailsService {

	@Autowired
	private CurrentUserRepository currentUserRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.pfchoice.springboot.model.CurrentUser user = currentUserRepository.findByUsername(username);
		if(user!= null  && user.getActiveInd() == 'Y'){
			Role role = user.getRole();
			List<GrantedAuthority> authorities = new ArrayList<>();
			GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getRole());
			authorities.add(authority);

			return new User(user.getUsername(), user.getPassword(), authorities);
		} 
			throw new UsernameNotFoundException("could not find the user '" + username + "'");

	}
}
