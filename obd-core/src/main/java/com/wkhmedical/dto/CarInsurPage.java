package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "车辆保险对象", description = "用于检索车辆保险列表用")
@Getter
@Setter
public class CarInsurPage implements Serializable {

	@ApiModelProperty(value = "内存页码", required = true)
	@NotNull
	@Min(1)
	private Integer paging;

	private Long id;

}
