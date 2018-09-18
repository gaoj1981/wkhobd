package com.wkhmedical.po;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wkhmedical.constant.LicStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "obd_lic")
@CompoundIndexes({ @CompoundIndex(name = "idx_eid_sn", def = "{'eid': 1, 'sn': -1}", unique = true),
		@CompoundIndex(name = "idx_eid_status", def = "{'eid': 1, 'status': -1}") })
public class MgObdLic {

	@Id
	private String id;

	private String eid;

	private String sn;

	private LicStatus status;

	private Integer type;

	private Long v;
	private Long exp;
	private Long ut;
	private Long ct;

	private Map<String, Integer> conf;
	private Map<String, Integer> stats;

}
