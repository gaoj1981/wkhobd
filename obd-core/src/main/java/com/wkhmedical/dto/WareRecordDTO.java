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
@ApiModel(value = "维修保养", description = "返回维修保养的信息")
public class WareRecordDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主KEY")
	private Long id;

	@ApiModelProperty(value = "设备ID（等同车辆ID）")
	private String eid;

	@ApiModelProperty(value = "维修/保养的对象。1：设备；2：车辆")
	private Integer excRes;

	@ApiModelProperty(value = "执行类型。1：维修、2：保养、3：更换")
	private Integer excType;

	@ApiModelProperty(value = "操作详情")
	private String note;

	@ApiModelProperty(value = "操作时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date excDate;

	@ApiModelProperty(value = "操作人姓名")
	private String excUser;

	@ApiModelProperty(value = "操作人电话")
	private String excTel;

}