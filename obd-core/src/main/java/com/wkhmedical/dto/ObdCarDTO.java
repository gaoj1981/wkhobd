package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "车辆OBD信息", description = "返回车辆的实时信息")
public class ObdCarDTO implements Serializable {

	@ApiModelProperty(value = "车辆ID")
	private String eid;

	@ApiModelProperty(value = "发动机转速")
	private Integer engineTurnSpeed;

	@ApiModelProperty(value = "车辆速度  KM/H")
	private Integer vehicleSpeed;

	@ApiModelProperty(value = "故障灯状态")
	private String faultLightStatus;

	@ApiModelProperty(value = "故障灯个数 个")
	private Integer faultLigthNumber;

	@ApiModelProperty(value = "故障内容描述")
	private String faulContext;

	@ApiModelProperty(value = "剩余油量")
	private Integer remainingGasValue;

	@ApiModelProperty(value = "汽车仪表总里程 KM")
	private Double dashboardTotalMileage;

}
