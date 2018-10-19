package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "绑定人员对象", description = "用于检索车辆或设备的绑定人员列表用")
@Getter
@Setter
public class BindUserBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主KEY")
	private String id;

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

	@ApiModelProperty(value = "人员分类：1：运营；2：维护")
	private Integer utype;

	@ApiModelProperty(value = "人员姓名（支持模糊查询）")
	private String unameLike;

	@ApiModelProperty(value = "人员电话（支持模糊查询）")
	private String telLike;

	@ApiModelProperty(value = "人员姓名/电话（模糊OR查询）")
	private String orUnameTel;

	@ApiModelProperty(value = "是否默认")
	private Integer isDefault;

	@ApiModelProperty(value = "性别查询数组 All|M|F|N")
	private String[] sexSel;

	@ApiModelProperty(value = "日期查询区分。1：添加时间；2：最后修改时间")
	private Integer timeSel;

	@ApiModelProperty(value = "查询开始日期")
	private String timeStart;

	@ApiModelProperty(value = "查询结束日期")
	private String timeEnd;

	@ApiModelProperty(value = "所属部门数组")
	private Integer[] utypeSel;

}
