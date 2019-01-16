/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel(value = "日级汇总对象", description = "返回日级汇总的信息")
public class DeviceTimeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String eid;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dt;

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

	private Integer exprt;

	private Long rps;

	private Long wds;

	private String plateNum;
}
