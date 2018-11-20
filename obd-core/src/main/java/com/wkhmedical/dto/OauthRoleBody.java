package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "用户权限对象", description = "用于交互传输用户权限信息")
public class OauthRoleBody implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	@NotNull(groups = { ValiEdit.class })
	private String id;
	
	@ApiModelProperty(value = "")
	private String roleName;
	
	@ApiModelProperty(value = "是否有效。0：无效；1：有效")
	private Integer isAble;
	
	@ApiModelProperty(value = "")
	private String roleDesc;
	
}