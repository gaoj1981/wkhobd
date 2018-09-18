package com.wkhmedical.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "obd_lic_req")
public class MgObdLicReq {

	@Id
	private String id;

	@Indexed(unique = true)
	private String eid;

	private Long st;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public Long getSt() {
		return st;
	}

	public void setSt(Long st) {
		this.st = st;
	}

}
