package com.wkhmedical.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "device_month")
@DynamicInsert
@DynamicUpdate
public class DeviceMonth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private Integer month;

	private Date dtMonth;

	private String ymMonth;

	private Long ckNum;

	private Long carNum;

	private Long carDis;

	private Long carTime;

	@Column(insertable = false, updatable = false)
	private Date insTime;

	@Column(insertable = false, updatable = false)
	private Date updTime;
}
