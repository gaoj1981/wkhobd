package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.List;

import com.wkhmedical.po.EquipInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "设备列表详情", description = "返回设备列表详情的信息")
public class EquipDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "固定设备")
	private List<EquipInfo> lstFixed;

	@ApiModelProperty(value = "辅助设备")
	private List<EquipInfo> lstAssist;
}