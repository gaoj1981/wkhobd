/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;
import java.util.List;

import com.wkhmedical.po.MgEquipExcel;

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

	private Integer total;
	private Integer sucNum;
	private Integer errNum;
	private List<MgEquipExcel> lstData;
}
