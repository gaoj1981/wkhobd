/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.message;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;

/**
 * The Class WaveData.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
@ApiModel(value = "生理数据对象", description = "根据具体业务数据，补充定义...")
public class WaveData {

	/** 病人ID */
	private Long uid;

	/** 批次ID */
	private String bid;

	/** 采集时间 */
	private Long colTime;

	/** The late. */
	private String late;

	/** The sequence. */
	private String sequence;

	/** The list wave. */
	private List<Map<String, String>> listWave;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getColTime() {
		return colTime;
	}

	public void setColTime(Long colTime) {
		this.colTime = colTime;
	}

	public String getLate() {
		return late;
	}

	public void setLate(String late) {
		this.late = late;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public List<Map<String, String>> getListWave() {
		return listWave;
	}

	public void setListWave(List<Map<String, String>> listWave) {
		this.listWave = listWave;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

}
