package com.wkhmedical.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YunuserBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2102111319718874315L;

	private String uid;

	private String userMobi;

	private String idCard;

}
