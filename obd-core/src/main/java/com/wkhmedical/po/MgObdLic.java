package com.wkhmedical.po;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wkhmedical.constant.LicStatus;

@Document(collection = "obd_lic")
@CompoundIndexes({ @CompoundIndex(name = "idx_eid_sn", def = "{'eid': 1, 'sn': -1}", unique = true),
		@CompoundIndex(name = "idx_eid_status", def = "{'eid': 1, 'status': -1}") })
public class MgObdLic {

	@Id
	private String id;

	@Indexed
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

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public LicStatus getStatus() {
		return status;
	}

	public void setStatus(LicStatus status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getV() {
		return v;
	}

	public void setV(Long v) {
		this.v = v;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	public Long getUt() {
		return ut;
	}

	public void setUt(Long ut) {
		this.ut = ut;
	}

	public Long getCt() {
		return ct;
	}

	public void setCt(Long ct) {
		this.ct = ct;
	}

	public Map<String, Integer> getConf() {
		return conf;
	}

	public void setConf(Map<String, Integer> conf) {
		this.conf = conf;
	}

	public Map<String, Integer> getStats() {
		return stats;
	}

	public void setStats(Map<String, Integer> stats) {
		this.stats = stats;
	}

}
