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
@ApiModel(value = "体检项数目统计", description = "返回云巡诊车体检项数目统计")
public class CheckTypeTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long inspectTotal;
	private Long tcmConstitutionTotal;
}
