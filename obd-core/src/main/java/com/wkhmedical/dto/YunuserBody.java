package com.wkhmedical.dto;

import java.io.Serializable;

public class YunuserBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2102111319718874315L;

	private Long uid;

	private String userMobi;

	private String idCard;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserMobi() {
		return userMobi;
	}

	public void setUserMobi(String userMobi) {
		this.userMobi = userMobi;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

}
