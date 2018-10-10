package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "维修保养分页检索对象", description = "用于检索维修保养分页列表用")
@Getter
@Setter
public class WareRecordPage extends PageSearchBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "业务检索对象")
	private WareRecordBody query;

}
