package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "车辆保险对象", description = "用于检索车辆保险列表用")
@Getter
@Setter
public class CarInsurBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主KEY")
	private String id;

	@ApiModelProperty(value = "车辆ID")
	private String eid;

	@ApiModelProperty(value = "保险类别；1：交强险；2：商业险")
	private Integer insurType;

	@ApiModelProperty(value = "保单号")
	private String insurNum;

	@ApiModelProperty(value = "获取类型。0:未生效；1:已生效；2：已失效")
	@Min(0)
	@Max(2)
	private Integer valiType;
}
