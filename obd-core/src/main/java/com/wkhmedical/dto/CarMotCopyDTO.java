package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "车辆保险扩展复制", description = "返回车辆保险扩展复制的信息")
public class CarMotCopyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private String id;
	
	@ApiModelProperty(value = "车辆主KEY")
	private String cid;
	
	@ApiModelProperty(value = "年检日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date motDate;
	
	@ApiModelProperty(value = "有效截止日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date expDate;
	
	@ApiModelProperty(value = "经办单位")
	private String dealLtd;
	
	@ApiModelProperty(value = "年检备份")
	private String motImgs;
	
}