package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

public class MobiVcode implements Serializable {
	private static final long serialVersionUID = -5404278390972399054L;

	private String userMobi;
	private String valiCode;
	private Integer valiType;
	private Integer sendTimes;
	private Date sendDate;

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

	public Integer getValiType() {
		return valiType;
	}

	public void setValiType(Integer valiType) {
		this.valiType = valiType;
	}

	public Integer getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(Integer sendTimes) {
		this.sendTimes = sendTimes;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

}
