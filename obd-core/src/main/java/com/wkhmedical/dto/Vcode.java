package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

public class Vcode implements Serializable{
	private static final long serialVersionUID = 277605691262515838L;
	private String phone;
	private String code;
	private Date ct;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCt() {
		return ct;
	}

	public void setCt(Date ct) {
		this.ct = ct;
	}

}
