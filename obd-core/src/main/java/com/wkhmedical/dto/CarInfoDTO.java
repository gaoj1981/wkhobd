package com.wkhmedical.dto;

import java.io.Serializable;

import com.wkhmedical.constant.FuelType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "车辆对象", description = "返回的车辆信息")
public class CarInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "车辆ID")
	private String eid;

	@ApiModelProperty(value = "OBD推送设备编号")
	private String deviceNumber;

	@ApiModelProperty(value = "分组编号（省市区县ID联）")
	private String groupId;

	@ApiModelProperty(value = "车辆名称")
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

	@ApiModelProperty(value = "运营负责人姓名")
	private String prinName;

	@ApiModelProperty(value = "运营负责人职位")
	private String prinJob;

	@ApiModelProperty(value = "运营负责人电话")
	private String prinTel;

	@ApiModelProperty(value = "运营负责人的紧急联系人姓名")
	private String prinUrName;

	@ApiModelProperty(value = "运营负责人的紧急联系人电话")
	private String prinUrTel;

	@ApiModelProperty(value = "维护负责人ID")
	private Long maintId;

	@ApiModelProperty(value = "维护负责人姓名")
	private String maintName;

	@ApiModelProperty(value = "维护负责人电话")
	private String maintTel;

	@ApiModelProperty(value = "维护负责人的紧急联系人姓名")
	private String maintUrName;

	@ApiModelProperty(value = "维护负责人的紧急联系人电话")
	private String maintUrTel;

	@ApiModelProperty(value = "省ID")
	private Long provId;

	@ApiModelProperty(value = "市ID")
	private Long cityId;

	@ApiModelProperty(value = "区县ID")
	private Long areaId;
}
