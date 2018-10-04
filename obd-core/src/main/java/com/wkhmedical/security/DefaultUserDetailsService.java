/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wkhmedical.po.YunUser;
import com.wkhmedical.repository.jpa.YunUserRepository;

/**
 * The Class DefaultUserDetailsService.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Service
public class DefaultUserDetailsService implements UserDetailsService {

	@Autowired
	YunUserRepository userRepository;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TUserDetails userDetails = new TUserDetails();
		//TODO

		// 正常登录模式
		YunUser user = userRepository.findByUserIdCardOrUserMobi(username, username);
		if (user == null) {
			throw new UsernameNotFoundException("账号不存在");
		}
		if (user.getState() < 0) {
			throw new UsernameNotFoundException("账号已禁用");
		}
		userDetails.setUid(user.getId() + "");
		userDetails.setUsername(StringUtils.isNotBlank(user.getUserIdCard()) ? user.getUserIdCard() : user.getUserMobi());
		userDetails.setPassword(user.getUserPwd());
		userDetails.setSalt(user.getUserPwdSalt());
		userDetails.setRealName(user.getUserName());
		userDetails.setEnabled(true);
		
		return userDetails;
	}

}
