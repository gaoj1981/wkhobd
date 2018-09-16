/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The Class DefaultUserDetailsService.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Service
public class DefaultUserDetailsService implements UserDetailsService {

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TUserDetails userDetails = new TUserDetails();
		System.out.println("aaaaaaaaaaaaa");
		//TODO
		userDetails.setEnabled(true);
		return userDetails;
	}

}
