package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "车辆参数对象", description = "用于检索车辆分页列表用")
@Getter
@Setter
public class CarInfoPageSearch extends PageSearchBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "业务检索对象")
	private CarInfoPageParam query;
}
