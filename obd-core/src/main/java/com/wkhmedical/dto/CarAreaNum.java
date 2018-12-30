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
public class CarAreaNum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long areaId;

	private String areaName;

	private Long number;
}
