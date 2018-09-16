package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import com.wkhmedical.constant.Gender;

public class IdCardValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3755372109933646162L;

	private boolean isVali;
	private Date birthDay;
	private Gender sex;
	private String idCard;
	private String provinceCode;
	private String cityCode;
	private String errMsg;

	public boolean isVali() {
		return isVali;
	}

	public void setVali(boolean isVali) {
		this.isVali = isVali;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Gender getSex() {
		return sex;
	}

	public void setSex(Gender sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
