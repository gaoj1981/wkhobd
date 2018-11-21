package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "权限路径信息", description = "返回权限路径信息的信息")
public class OauthPriDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private String id;
	
	@ApiModelProperty(value = "")
	private String parentId;
	
	@ApiModelProperty(value = "")
	private String priName;
	
	@ApiModelProperty(value = "")
	private String priUrl;
	
	@ApiModelProperty(value = "是否有效。0：无效；1：有效")
	private Integer isAble;
	
	@ApiModelProperty(value = "0：不显示；1：显示")
	private Integer priDisplay;
	
}