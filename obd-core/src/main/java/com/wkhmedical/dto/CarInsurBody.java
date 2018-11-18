package com.wkhmedical.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "车辆保险对象", description = "用于检索车辆保险列表用")
@Getter
@Setter
public class CarInsurBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主KEY")
	private String id;

	@ApiModelProperty(value = "车辆ID")
	private String eid;

	@ApiModelProperty(value = "保险类别；1：交强险；2：商业险")
	private Integer insurType;

	@ApiModelProperty(value = "保单号")
	private String insurNum;

	@ApiModelProperty(value = "获取类型。0:未生效；1:已生效；2：已失效")
	@Min(0)
	@Max(2)
	private Integer valiType;

	// ===========查询扩展=============

	@ApiModelProperty(value = "车辆ID（支持模糊查询）")
	private String eidLike;

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

	@ApiModelProperty(value = "日期查询区分。1：添加时间；2：最后修改时间；3：保险失效日期；4：保险生效日期；")
	private Integer timeSel;

	@ApiModelProperty(value = "查询开始日期")
	private String timeStart;

	@ApiModelProperty(value = "查询结束日期")
	private String timeEnd;

	@ApiModelProperty(value = "保险到期范围。1:30天内到期；2：60天内到期；3：90天内到期；4：已过期")
	@Min(0)
	@Max(4)
	private Integer expDayFlag;

	@ApiModelProperty(value = "保单编号（支持模糊查询）")
	private String insurNumLike;

}
