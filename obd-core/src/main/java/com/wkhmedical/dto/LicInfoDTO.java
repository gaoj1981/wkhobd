package com.wkhmedical.dto;

import java.util.Map;

import com.wkhmedical.constant.LicStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicInfoDTO {

	private String sn;
	private Long exp;
	private Map<String, Integer> conf;
	private LicStatus status;
	private Long v;

}
