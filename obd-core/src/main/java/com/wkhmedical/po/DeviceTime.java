/**
 * 
 */
package com.wkhmedical.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@Entity
@Table(name = "device_time")
public class DeviceTime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String eid;

	private Date dt;

	private Date sdt;

	private Date edt;

	private Long ts;

	private BigDecimal dis;

	private Integer flag;

	private Long provId;

	private Long cityId;

	private Long areaId;

	private Long townId;

	private Long villId;

	private Long pts;

	private Long cks;

	private Long exprt;

	private Long rps;

	private Integer wds;

}
