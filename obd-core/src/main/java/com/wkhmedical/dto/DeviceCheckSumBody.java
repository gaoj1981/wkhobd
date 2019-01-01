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
public class DeviceCheckSumBody implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String eid;

	private Long provId;

	private Long cityId;

	private Long areaId;

	private Long townId;

	private Long villId;

	private Boolean bcabnm = false;

	private Boolean bcexam = false;

	private Boolean bioabnm = false;

	private Boolean bioexam = false;

	private Boolean bscanabnm = false;

	private Boolean bscanexam = false;

	private Boolean ecgabnm = false;

	private Boolean ecgexam = false;

	private Boolean bpabnm = false;

	private Boolean bpexam = false;

	private Boolean report = false;

	private Boolean urineabnm = false;

	private Boolean urineexam = false;

	private Boolean persontime = false;

	private Boolean healthexam = false;

	private Boolean tcmexam = false;

}
