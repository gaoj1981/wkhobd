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
 *
 */
@Getter
@Setter
@ApiModel(value = "累积服务里程", description = "返回云巡诊车累积服务里程")
public class DisTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal serviceMileage;
}
