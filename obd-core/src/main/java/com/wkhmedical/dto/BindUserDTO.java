package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "相关人员", description = "返回的车辆或设备的相关人员信息")
public class BindUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "人员ID")
	private String id;

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

	@ApiModelProperty(value = "人员分类：1：运营；2：维护；")
	private Integer utype;

	@ApiModelProperty(value = "姓名")
	private String uname;

	@ApiModelProperty(value = "职位")
	private String job;

	@ApiModelProperty(value = "电话")
	private String tel;

	@ApiModelProperty(value = "紧急联系人")
	private String urName;

	@ApiModelProperty(value = "紧急联系电话")
	private String urTel;

	@ApiModelProperty(value = "是否为该区县默认负责人。0：否；1：是")
	private Integer isDefault;

}
