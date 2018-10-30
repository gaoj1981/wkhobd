/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "上传结果", description = "返回文件上传的结果对象信息")
public class UploadResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "文件唯一标识")
	private String fid;

	@ApiModelProperty(value = "服务端文件名")
	private String fname;

	@ApiModelProperty(value = "服务端文件路径")
	private String fdir;

	@ApiModelProperty(value = "客户端原文件名")
	private String oname;

	@ApiModelProperty(value = "是否成功上传。0：上传失败；1：上传成功")
	private Boolean isSuc = true;

	@ApiModelProperty(value = "上传失败信息")
	private String errMsg;
}
