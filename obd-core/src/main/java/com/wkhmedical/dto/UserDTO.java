/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.dto;

import java.util.Date;

import com.wkhmedical.constant.Ethnic;
import com.wkhmedical.constant.Gender;

/**
 * The Class UserDTO.
 */
public class UserDTO {

	/** The user id. */
	private Long userId;

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

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the user id card.
	 *
	 * @return the user id card
	 */
	public String getUserIdCard() {
		return userIdCard;
	}

	/**
	 * Sets the user id card.
	 *
	 * @param userIdCard the new user id card
	 */
	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}

	/**
	 * Gets the user mobi.
	 *
	 * @return the user mobi
	 */
	public String getUserMobi() {
		return userMobi;
	}

	/**
	 * Sets the user mobi.
	 *
	 * @param userMobi the new user mobi
	 */
	public void setUserMobi(String userMobi) {
		this.userMobi = userMobi;
	}

	/**
	 * Gets the user birth.
	 *
	 * @return the user birth
	 */
	public Date getUserBirth() {
		return userBirth;
	}

	/**
	 * Sets the user birth.
	 *
	 * @param userBirth the new user birth
	 */
	public void setUserBirth(Date userBirth) {
		this.userBirth = userBirth;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * Gets the user sex.
	 *
	 * @return the user sex
	 */
	public Gender getUserSex() {
		return userSex;
	}

	/**
	 * Sets the user sex.
	 *
	 * @param userSex the new user sex
	 */
	public void setUserSex(Gender userSex) {
		this.userSex = userSex;
	}

	public Ethnic getUserEthnic() {
		return userEthnic;
	}

	public void setUserEthnic(Ethnic userEthnic) {
		this.userEthnic = userEthnic;
	}

}
