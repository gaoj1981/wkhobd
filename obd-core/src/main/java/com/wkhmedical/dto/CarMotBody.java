package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "车辆年检对象", description = "用于交互传输车辆年检信息")
public class CarMotBody implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主KEY")
	@NotNull(groups = { ValiEdit.class })
	private Long id;
	
	@ApiModelProperty(value = "车辆ID")
	@NotBlank(groups = { ValiAdd.class })
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

	@ApiModelProperty(value = "获取类型。1:有效；2：失效")
	@Min(1)
	@Max(2)
	private Integer valiType;
}