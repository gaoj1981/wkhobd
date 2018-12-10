/**
 * 
 */
package com.wkhmedical.po;

import java.io.Serializable;
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
@Table(name = "device_time_temp")
public class DeviceTimeTemp implements Serializable {

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

	private Integer flag;

}
