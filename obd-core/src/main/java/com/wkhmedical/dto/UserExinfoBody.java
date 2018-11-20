package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "用户扩展信息对象", description = "用于交互传输用户扩展信息信息")
public class UserExinfoBody implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	@NotNull(groups = { ValiEdit.class })
	private String id;
	
	@ApiModelProperty(value = "")
	private String nick;
	
	@ApiModelProperty(value = "")
	private String note;
	
	@ApiModelProperty(value = "")
	private String country;
	
	@ApiModelProperty(value = "")
	private Integer provId;
	
	@ApiModelProperty(value = "")
	private Integer cityId;
	
	@ApiModelProperty(value = "")
	private String addr;
	
	@ApiModelProperty(value = "")
	private String tel;
	
	@ApiModelProperty(value = "")
	private String avatar;
	
	@ApiModelProperty(value = "")
	private String dept;
	
	@ApiModelProperty(value = "")
	private String office;
	
	@ApiModelProperty(value = "")
	private String email;
	
	@ApiModelProperty(value = "")
	private String signature;
	
	@ApiModelProperty(value = "")
	private String labels;
	
}