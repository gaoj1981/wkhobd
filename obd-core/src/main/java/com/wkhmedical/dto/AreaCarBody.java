/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
public class AreaCarBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long provId;
	private Long cityId;
	private Long areaId;
	private Long townId;
	private Long villId;
	private String eid;

}
