package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "系统用户", description = "返回当前会话系统用户信息")
public class SessionUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "姓名")
	private String name;
	
	@ApiModelProperty(value = "头像")
	private String avatar;
	
	@ApiModelProperty(value = "用户ID")
	private String userid;
	
	@ApiModelProperty(value = "邮箱")
	private String email;
	
	@ApiModelProperty(value = "个性签名")
	private String signature;
	
	@ApiModelProperty(value = "职位级别")
	private String title;
	
	@ApiModelProperty(value = "所属组织")
	private String group;
	
	@ApiModelProperty(value = "标签")
	private List<Map<String, Object>> tags;
	
	@ApiModelProperty(value = "系统通知")
	private Integer notifyCount;
	
	@ApiModelProperty(value = "国家")
	private String country;
	
	@ApiModelProperty(value = "地区")
	private Map<String, Object> geographic;
	
	@ApiModelProperty(value = "地址")
	private String address;
	
	@ApiModelProperty(value = "电话")
	private String phone;

}
