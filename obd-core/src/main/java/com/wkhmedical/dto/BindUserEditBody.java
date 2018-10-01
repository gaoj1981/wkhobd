package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "相关人员对象", description = "用于修改相关绑定人员信息")
public class BindUserEditBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "人员ID", required = true)
	@NotNull
	private Long id;

	@ApiModelProperty(value = "人员分类：1：运营；2：维护")
	@Min(value = 1, message = "非法参数")
	@Max(value = 2, message = "非法参数")
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
