package com.wkhmedical.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "obd_lic_req")
public class MgObdLicReq {

	@Id
	private String id;

	@Indexed(unique = true)
	private String eid;

	private Long st;

}
