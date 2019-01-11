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
@ApiModel(value = "体检人数统计", description = "返回云巡诊车体检人数统计")
public class CheckPeopleTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long yesterdayData;
	private Long weekData;
	private Long monthData;
	private Long yearData;

}
