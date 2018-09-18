package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import com.wkhmedical.constant.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
