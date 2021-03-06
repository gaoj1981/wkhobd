package com.wkhmedical.po;

import java.io.Serializable;

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
@Table(name = "device_check")
@DynamicInsert
@DynamicUpdate
public class DeviceCheck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String eid;

	private String type;

	private Long number;

	private Integer status;

	private Long time;

	private Long provId;

	private Long cityId;

	private Long areaId;

	private Long townId;

	private Long villId;

}
