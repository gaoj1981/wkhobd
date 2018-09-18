package com.wkhmedical.po;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "obd_lic_sum")
public class MgObdLicSum {

	@Id
	private String id;

	@Indexed(unique = true)
	private String eid;

	private Long ut;

	private Map<String, Integer> stats;

	@CreatedDate
	private Date createTime;

}
