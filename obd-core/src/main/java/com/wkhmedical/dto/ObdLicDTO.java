package com.wkhmedical.dto;

public class ObdLicDTO {

	private String id;
	private Long st;

	private LicInfoDTO lic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSt() {
		return st;
	}

	public void setSt(Long st) {
		this.st = st;
	}

	public LicInfoDTO getLic() {
		return lic;
	}

	public void setLic(LicInfoDTO lic) {
		this.lic = lic;
	}

}
