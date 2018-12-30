package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @ApiModel(value = "绑定人员对象", description = "用于添加相关绑定人员信息")
public class BindUserAddBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "区县ID", required = true)
	@Min(value = 100000000000l, message = "非法参数")
	@Max(value = 999999999999l, message = "非法参数")
	private Long areaId;

	@ApiModelProperty(value = "人员分类：1：运营；2：维护", required = true)
	@Min(value = 1, message = "非法参数")
	@Max(value = 2, message = "非法参数")
	private Integer utype;

	@ApiModelProperty(value = "姓名")
	@Length(min = 0, max = 15, message = "姓名不能超过15个字符")
	private String uname;

	@ApiModelProperty(value = "公司")
	@Length(min = 0, max = 100, message = "职位不能超过100个字符")
	private String ltd;

	@ApiModelProperty(value = "职位")
	@Length(min = 0, max = 20, message = "职位不能超过15个字符")
	private String job;

	@ApiModelProperty(value = "电话")
	@Pattern(regexp = "^1\\d{10}$", message = "联系电话格式不正确")
	private String tel;

	@ApiModelProperty(value = "紧急联系人")
	@Length(min = 0, max = 15, message = "紧急联系人不能超过15个字符")
	private String urName;

	@ApiModelProperty(value = "紧急联系电话")
	@Pattern(regexp = "^1\\d{10}$", message = "紧急联系电话格式不正确")
	private String urTel;

	@ApiModelProperty(value = "是否为该区县默认负责人。0：否；1：是", required = true)
	@Min(value = 0, message = "非法参数")
	@Max(value = 1, message = "非法参数")
	private Integer isDefault;

}
