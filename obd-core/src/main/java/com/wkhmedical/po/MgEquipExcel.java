/**
 * 
 */
package com.wkhmedical.po;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@Document(collection = "equip_excel")
@CompoundIndexes({ @CompoundIndex(name = "idx_delFlag_excelPath", def = "{'delFlag': 1, 'excelPath': -1}") })
public class MgEquipExcel {

	/**
	 * 主Key
	 */
	@Id
	private String id;

	@CreatedDate
	private Date insTime;

	private String eid;

	private Integer type;
	private String equipId;
	// 总设备名-用于对应车辆名称
	private String equipName;
	// antd图标-用于表示设备
	private String icon;
	private String name;
	private String bhNum;
	private String xhNum;
	private String factoryId;
	private String factory;
	private String birthDate;
	private String version;
	private String countNum;
	private String note;
	private String description;
	@Indexed
	private String excelPath;
	private Integer delFlag;

}
