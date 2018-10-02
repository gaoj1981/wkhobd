package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.wkhmedical.constant.FuelType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "车辆增改对象", description = "用于添加或修改车辆信息")
public class CarInfoAddBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7039575575637524947L;

	@ApiModelProperty(value = "车辆唯一标识，修改记录用")
	private Long id;

	@ApiModelProperty(value = "车辆ID", required = true)
	@NotBlank(message = "车辆ID必须")
	private String eid;

	@ApiModelProperty(value = "区县ID", required = true)
	@NotNull(message = "区县ID必须")
	private Integer areaId;

	@ApiModelProperty(value = "OBD推送设备编号")
	private String deviceNumber;

	@ApiModelProperty(value = "车辆名称")
	@Length(min = 0, max = 50, message = "车辆名称不能超过50个字符")
	private String carName;

	@ApiModelProperty(value = "百公里油耗")
	private Double baiOilUsed;

	@ApiModelProperty(value = "车牌号")
	private String plateNum;

	@ApiModelProperty(value = "车型")
	private String carModel;

	@ApiModelProperty(value = "颜色")
	private String carColor;

	@ApiModelProperty(value = "尺寸")
	private String carSize;

	@ApiModelProperty(value = "发动机编号")
	private String engineNum;

	@ApiModelProperty(value = "车架号")
	private String frameNum;

	@ApiModelProperty(value = "发动机排量")
	private String enginePower;

	@ApiModelProperty(value = "燃料类型")
	private FuelType fuelType;

	@ApiModelProperty(value = "运营负责人ID")
	private Long prinId;

	@ApiModelProperty(value = "维护负责人ID")
	private Long maintId;

}
