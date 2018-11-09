/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.message;

import io.swagger.annotations.ApiModel;

/**
 * The Class BodyData.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
@ApiModel(value = "体征对象", description = "根据具体业务数据，补充定义...")
public class BodyData {

	/** 病人ID */
	private Long uid;

	/** 批次ID */
	private String bid;

	/** 采集时间 */
	private Long colTime;

	/** 心率 */
	private String hr;

	/** 血压状态码：0--测量中，瞬时值 (bp)；1--测量完成，收缩压/舒张 压/平均压(sys/diamap) */
	private Integer bpStatus;

	/** 收缩压 */
	private String sys;

	/** 舒张压 */
	private String dia;

	/** 平均压 */
	private String map;

	/** 血压瞬时值 */
	private String bp;

	/** 血氧含量 */
	private String spo2;

	/** 脉率 */
	private String pr;

	/** 呼吸率 */
	private String rr;

	/** 温度 */
	private String temp;

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

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getHr() {
		return hr;
	}

	public void setHr(String hr) {
		this.hr = hr;
	}

	public Integer getBpStatus() {
		return bpStatus;
	}

	public void setBpStatus(Integer bpStatus) {
		this.bpStatus = bpStatus;
	}

	public String getSys() {
		return sys;
	}

	public void setSys(String sys) {
		this.sys = sys;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public String getSpo2() {
		return spo2;
	}

	public void setSpo2(String spo2) {
		this.spo2 = spo2;
	}

	public String getPr() {
		return pr;
	}

	public void setPr(String pr) {
		this.pr = pr;
	}

	public String getRr() {
		return rr;
	}

	public void setRr(String rr) {
		this.rr = rr;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

}
