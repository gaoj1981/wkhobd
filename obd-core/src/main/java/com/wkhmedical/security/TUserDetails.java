/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.security;

import com.taoxeo.boot.security.CustomUserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class TUserDetails.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Getter
@Setter
public class TUserDetails extends CustomUserDetails {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3813085076513347956L;
	
	private String realName;
	
}
