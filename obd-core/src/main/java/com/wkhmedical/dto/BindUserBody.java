package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "绑定人员对象", description = "用于检索车辆或设备的绑定人员列表用")
@Getter
@Setter
public class BindUserBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主KEY")
	private String id;

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

	@ApiModelProperty(value = "人员分类：1：运营；2：维护")
	private Integer utype;

}
