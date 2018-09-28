package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "车辆保险对象", description = "用于添加车辆保险信息")
public class CarInsurBodyAdd implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "车辆ID（等同设备ID）", required = true)
	@NotNull(message = "车辆ID必须")
	private String eid;

	@ApiModelProperty(value = "保险类别；1：交强险；2：商业险", required = true)
	@NotNull(message = "保险类别必须")
	private Integer insurType;

	@ApiModelProperty(value = "保单号", required = true)
	@NotNull(message = "保单号必须")
	private String insurNum;

	@ApiModelProperty(value = "投保公司")
	private String insurLtd;

	@ApiModelProperty(value = "客服电话")
	private String servTel;

	@ApiModelProperty(value = "生效日期（格式：yyyy-MM-dd）", required = true)
	@NotNull(message = "生效日期必须")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date effectDate;

	@ApiModelProperty(value = "失效日期（格式：yyyy-MM-dd）", required = true)
	@NotNull(message = "失效日期必须")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date expDate;

	@ApiModelProperty(value = "业务员姓名")
	private String salesName;

	@ApiModelProperty(value = "业务员电话")
	private String salesTel;

	@ApiModelProperty(value = "保单备份")
	private String insurImgs;

}