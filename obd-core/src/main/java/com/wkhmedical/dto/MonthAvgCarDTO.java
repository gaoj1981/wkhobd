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
@ApiModel(value = "月云巡诊车出车比例", description = "返回云巡诊车月云巡诊车出车比例对象")
public class MonthAvgCarDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer month;

	private Long outwardRunNum;

	private Long noDrivingOut;

}
