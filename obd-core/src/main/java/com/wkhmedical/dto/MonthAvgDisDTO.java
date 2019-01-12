/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel(value = "月平均行驶距离", description = "返回云巡诊车月平均行驶距离对象")
public class MonthAvgDisDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer month;

	private BigDecimal averageDistance;

}
