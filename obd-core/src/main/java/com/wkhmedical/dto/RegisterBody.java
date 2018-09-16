package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class RegisterBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 961248127150511301L;

	@NotBlank(message = "手机号必须")
	private String userMobi;

	@NotBlank(message = "手机验证码必须")
	private String valiCode;

	@NotBlank(message = "身份证号必须")
	@Length(min = 18, max = 18, message = "身份证号必须为18位")
	private String idCard;

	@NotBlank(message = "真实姓名必须")
	@Length(min = 2, max = 15, message = "真实姓名非法")
	private String userName;

	private String userPwd;

	public String getUserMobi() {
		return userMobi;
	}

	public void setUserMobi(String userMobi) {
		this.userMobi = userMobi;
	}

	public String getValiCode() {
		return valiCode;
	}

	public void setValiCode(String valiCode) {
		this.valiCode = valiCode;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

}
