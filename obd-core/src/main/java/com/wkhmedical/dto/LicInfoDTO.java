package com.wkhmedical.dto;

import java.util.Map;

import com.wkhmedical.constant.LicStatus;

public class LicInfoDTO {

	private String sn;
	private Long exp;
	private Map<String, Integer> conf;
	private LicStatus status;
	private Long v;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	public Map<String, Integer> getConf() {
		return conf;
	}

	public void setConf(Map<String, Integer> conf) {
		this.conf = conf;
	}

	public LicStatus getStatus() {
		return status;
	}

	public void setStatus(LicStatus status) {
		this.status = status;
	}

	public Long getV() {
		return v;
	}

	public void setV(Long v) {
		this.v = v;
	}

}
