/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date sdt;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date edt;
}
