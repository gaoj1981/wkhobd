package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "设备详情", description = "返回设备详情的信息")
public class EquipInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private String id;
	
	@ApiModelProperty(value = "设备ID（等同车辆编号）")
	private String eid;
	
	@ApiModelProperty(value = "分类。0：主料；1：辅料")
	private Integer type;
	
	@ApiModelProperty(value = "设备ID备用")
	private String equipId;
	
	@ApiModelProperty(value = "名称")
	private String name;
	
	@ApiModelProperty(value = "编号")
	private String bhNum;
	
	@ApiModelProperty(value = "型号")
	private String xhNum;
	
	@ApiModelProperty(value = "厂家ID备用")
	private String factoryId;
	
	@ApiModelProperty(value = "生产厂家")
	private String factory;
	
	@ApiModelProperty(value = "生产日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;
	
	@ApiModelProperty(value = "版本")
	private String version;
	
	@ApiModelProperty(value = "数量")
	private Long countNum;
	
	@ApiModelProperty(value = "备注")
	private String note;
	
}