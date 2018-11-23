package com.wkhmedical.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "车辆OBD信息", description = "返回车辆的实时信息")
public class ObdCarDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	@ApiModelProperty(value = "总油耗量 ML")
	private Integer totalGasUsed;

	@ApiModelProperty(value = "汽车仪表总里程 KM")
	private Double dashboardTotalMileage;

	@ApiModelProperty(value = "电瓶电压 V")
	private Double batteryvoltage;

	@ApiModelProperty(value = "位置经度")
	private BigDecimal lng;

	@ApiModelProperty(value = "位置纬度")
	private BigDecimal lat;

	@ApiModelProperty(value = "车辆环境温度")
	private Integer environmentTemperature;

	@ApiModelProperty(value = "冷却液温度")
	private Integer coolWaterTemperature;

	@ApiModelProperty(value = "进气口温度")
	private Integer airDoorTemperature;

	@ApiModelProperty(value = "车辆ID")
	private String eid;

	@ApiModelProperty(value = "车辆名称")
	private String carName;

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

}
