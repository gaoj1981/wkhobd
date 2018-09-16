package com.wkhmedical.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "biz")
public class BizProperties {

	private Long dbWorkerId;

	private Long dbDatacenterId;

	private String rsaPrivate;
	private String rsaPublic;

	public Long getDbWorkerId() {
		return dbWorkerId;
	}

	public void setDbWorkerId(Long dbWorkerId) {
		this.dbWorkerId = dbWorkerId;
	}

	public Long getDbDatacenterId() {
		return dbDatacenterId;
	}

	public void setDbDatacenterId(Long dbDatacenterId) {
		this.dbDatacenterId = dbDatacenterId;
	}

	public String getRsaPrivate() {
		return rsaPrivate;
	}

	public void setRsaPrivate(String rsaPrivate) {
		this.rsaPrivate = rsaPrivate;
	}

	public String getRsaPublic() {
		return rsaPublic;
	}

	public void setRsaPublic(String rsaPublic) {
		this.rsaPublic = rsaPublic;
	}

}
