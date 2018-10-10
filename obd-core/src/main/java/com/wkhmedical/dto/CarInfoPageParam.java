package com.wkhmedical.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInfoPageParam {

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

	@ApiModelProperty(value = "车辆ID（支持模糊查询）")
	private String eidLike;
}
