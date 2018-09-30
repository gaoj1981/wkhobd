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
@ApiModel(value = "车辆保险", description = "返回车辆保险的信息")
public class CarInsurDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "车辆ID（等同设备ID）")
	private String eid;

	@ApiModelProperty(value = "保单号")
	private String insurNum;

	@ApiModelProperty(value = "投保公司")
	private String insurLtd;

	@ApiModelProperty(value = "客服电话")
	private String servTel;

	@ApiModelProperty(value = "生效日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date effectDate;

	@ApiModelProperty(value = "失效日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date expDate;

	@ApiModelProperty(value = "保险类别；1：交强险；2：商业险")
	private Integer insurType;

	@ApiModelProperty(value = "业务员姓名")
	private String salesName;

	@ApiModelProperty(value = "业务员电话")
	private String salesTel;

	@ApiModelProperty(value = "保单图片（多张以“,”分隔）")
	private String insurImgs;

}