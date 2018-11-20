package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "用户权限", description = "返回用户权限的信息")
public class OauthRoleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private String id;
	
	@ApiModelProperty(value = "")
	private String roleName;
	
	@ApiModelProperty(value = "是否有效。0：无效；1：有效")
	private Integer isAble;
	
	@ApiModelProperty(value = "")
	private String roleDesc;
	
}