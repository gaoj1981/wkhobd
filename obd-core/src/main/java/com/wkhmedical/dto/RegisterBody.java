package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
