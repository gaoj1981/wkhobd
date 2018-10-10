package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "维修保养对象", description = "用于交互传输维修保养信息")
public class WareRecordBody implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	@NotNull(groups = { ValiEdit.class })
	private Long id;

	@ApiModelProperty(value = "设备ID（等同车辆ID）")
	@NotBlank(groups = { ValiAdd.class })
	private String eid;

	@ApiModelProperty(value = "维修/保养的对象。1：设备；2：车辆")
	@Min(value = 1, groups = { ValiAdd.class, ValiEdit.class })
	@Max(value = 2, groups = { ValiAdd.class, ValiEdit.class })
	private Integer excRes;

	@ApiModelProperty(value = "执行类型。1：维修、2：保养、3：更换")
	@Min(value = 1, groups = { ValiAdd.class, ValiEdit.class })
	@Max(value = 3, groups = { ValiAdd.class, ValiEdit.class })
	private Integer excType;

	@ApiModelProperty(value = "操作详情")
	@Length(min = 0, max = 500, groups = { ValiAdd.class, ValiEdit.class })
	private String note;

	@ApiModelProperty(value = "操作时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date excDate;

	@ApiModelProperty(value = "操作时间最大范围")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date excDateMax;

	@ApiModelProperty(value = "操作时间最小范围")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date excDateMin;

	@ApiModelProperty(value = "操作人姓名")
	@Length(min = 0, max = 15, groups = { ValiAdd.class, ValiEdit.class })
	private String excUser;

	@ApiModelProperty(value = "操作人电话")
	@Length(min = 0, max = 11, groups = { ValiAdd.class, ValiEdit.class })
	private String excTel;

}