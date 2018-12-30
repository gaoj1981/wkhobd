/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel(value = "区域巡诊车", description = "返回云巡诊车总数和覆盖乡镇总数")
public class AreaCarDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long queryId;

	private Long vehicleTotal;

	private Long townshipsTotal;

	private List<CarAreaNum> data;
}
