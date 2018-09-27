package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "绑定人员对象", description = "用于添加或修改相关绑定人员信息")
public class BindUserBody implements Serializable {

	@ApiModelProperty(value = "人员ID")
	private Long id;

	@ApiModelProperty(value = "人员分类", required = true)
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

}
