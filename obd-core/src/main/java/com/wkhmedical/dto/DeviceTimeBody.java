/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel(value = "日汇总对象", description = "用于交互传输日汇总参数信息")
public class DeviceTimeBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long provId;
	private Long cityId;
	private Long areaId;
	private Long townId;
	private Long villId;

	private String eid;
	private String plateNum;

	private Date sdt;
	private Date edt;
}
