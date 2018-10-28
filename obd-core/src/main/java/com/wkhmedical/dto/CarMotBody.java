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
	@NotBlank(groups = { ValiEdit.class })
	private String id;

	@ApiModelProperty(value = "车辆ID")
	@NotBlank(groups = { ValiAdd.class })
	private String eid;

	@ApiModelProperty(value = "年检日期")
	@NotNull(groups = { ValiAdd.class })
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date motDate;

	@ApiModelProperty(value = "有效截止日期")
	@NotNull(groups = { ValiAdd.class, ValiEdit.class })
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

	@ApiModelProperty(value = "车辆ID（支持模糊查询）")
	private String eidLike;

	@ApiModelProperty(value = "区县ID")
	private Integer areaId;

	@ApiModelProperty(value = "日期查询区分。1：添加时间；2：最后修改时间；3：年检有效期；4：年检执行日期；")
	private Integer timeSel;

	@ApiModelProperty(value = "查询开始日期")
	private String timeStart;

	@ApiModelProperty(value = "查询结束日期")
	private String timeEnd;

	@ApiModelProperty(value = "年检到期范围。1:30天内到期；2：60天内到期；3：90天内到期；4：已过期")
	@Min(0)
	@Max(4)
	private Integer expDayFlag;

}