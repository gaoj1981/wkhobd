package com.wkhmedical.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInfoPageParam {

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

	@ApiModelProperty(value = "省份ID")
	private Integer provId;

	@ApiModelProperty(value = "城市ID")
	private Integer cityId;

	@ApiModelProperty(value = "车辆ID（支持模糊查询）")
	private String eidLike;

	@ApiModelProperty(value = "车牌号（支持模糊查询）")
	private String plateNumLike;

	@ApiModelProperty(value = "车管人员（支持运营或维护）")
	private String buserId;
}
