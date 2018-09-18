package com.wkhmedical.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObdLicDTO {

	private String eid;
	private Long st;

	private LicInfoDTO lic;

}
