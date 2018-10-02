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
@ApiModel(value = "车辆年检", description = "返回车辆年检的信息")
public class CarMotDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "车辆ID（等同设备ID）")
	private String eid;

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