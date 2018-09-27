package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "绑定人员对象", description = "用于检索车辆或设备的绑定人员列表用")
@Getter
@Setter
public class BindUserPage implements Serializable {

	@ApiModelProperty(value = "内存页码", required = true)
	@NotNull
	@Min(1)
	private Integer paging;

	private Long id;

}
