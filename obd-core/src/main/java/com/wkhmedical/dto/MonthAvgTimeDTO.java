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
@ApiModel(value = "月平均运营时长", description = "返回云巡诊车月平均月运营时长对象")
public class MonthAvgTimeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer month;

	private Long operatDuration;

}
