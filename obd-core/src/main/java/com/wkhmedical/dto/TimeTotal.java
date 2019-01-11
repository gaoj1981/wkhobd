/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel(value = "累积运营时长", description = "返回云巡诊车累积运营时长")
public class TimeTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long operationDuration;
}
