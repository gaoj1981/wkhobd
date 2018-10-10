package com.wkhmedical.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageSearchBody {

	@ApiModelProperty(value = "内存页码")
	@NotNull
	@Min(value = 1)
	private Integer page;

	@ApiModelProperty(value = "每页记录数")
	@Min(value = 1)
	@Max(value = 50)
	private Integer size;

}
