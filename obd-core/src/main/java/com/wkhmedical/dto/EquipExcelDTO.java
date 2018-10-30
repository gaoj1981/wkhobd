/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "设备Excel解析", description = "返回的设备Excel解析对象")
public class EquipExcelDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String eid;
	private Integer type;
	private String equipId;
	private String name;
	private String bhNum;
	private String xhNum;
	private String factoryId;
	private String factory;
	private String birthDate;
	private String version;
	private String countNum;
	private String note;

}
