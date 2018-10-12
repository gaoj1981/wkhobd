/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.dto;

import java.util.Date;

import com.wkhmedical.constant.Ethnic;
import com.wkhmedical.constant.Gender;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UserDTO.
 */
@Getter
@Setter
public class UserDTO {

	/** The user id. */
	private String userId;

	/** The user name. */
	private String userName;

	/** The user id card. */
	private String userIdCard;

	/** The user mobi. */
	private String userMobi;

	/** The user birth. */
	private Date userBirth;

	/** The state. */
	private Integer state;

	/** The user sex. */
	private Gender userSex;

	/** The user ethnic. */
	private Ethnic userEthnic;

}
