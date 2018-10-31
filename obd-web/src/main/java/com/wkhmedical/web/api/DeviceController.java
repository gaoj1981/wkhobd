/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.web.api;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.taoxeo.boot.security.CurrentUser;
import com.wkhmedical.config.ConfigProperties;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.EquipExcelDTO;
import com.wkhmedical.dto.ObdLicDTO;
import com.wkhmedical.dto.UploadResult;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.EquipInfoService;
import com.wkhmedical.service.ObdLicService;
import com.wkhmedical.util.FileUtil;
import com.wkhmedical.util.RSAUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "设备接口")
@RequestMapping("/api")
public class DeviceController {

	@Autowired
	ObdLicService obdLicService;

	@Resource
	ConfigProperties configProps;

	@Autowired
	EquipInfoService equipInfoService;

	/**
	 * 获取设备授权信息.
	 *
	 * @param id 设备编号
	 * @param licInfo 加密请求信息
	 * @return 加密后授权信息
	 */
	@PostMapping("/device/{eid:[0-9a-zA-Z]+}/auth")
	public String getWare(@PathVariable String eid, @RequestBody @Valid String licStr) {
		log.info("============BEGIN==============");
		log.info("请求设备编号：" + eid);
		log.info("请求加密数据：" + licStr);
		ObdLicDTO rtnDTO = obdLicService.getObdLic(eid, licStr);
		String rtnJsonStr = JSON.toJSONString(rtnDTO);
		log.info("返回数据：" + rtnJsonStr);
		// 加密返回数据
		String rtnRsaStr = null;
		try {
			rtnRsaStr = RSAUtil.privateEncrypt(rtnJsonStr, RSAUtil.getPrivateKey(configProps.getRsaPrivate()));
		}
		catch (Exception e) {
		}
		log.info("返回加密数据：" + rtnRsaStr);
		log.info("============END==============");
		return rtnRsaStr;
	}

	/**
	 * 获取授权已使用/总量
	 * 
	 * @return
	 */
	@GetMapping("/lic/count.sum")
	public Long[] getLicCountSum() {
		return obdLicService.getLicCountArr(null);
	}

	@ApiOperation(value = "上传设备Excel")
	@PostMapping("/device/upload")
	public UploadResult uploadExcel(MultipartHttpServletRequest multiRequest, HttpServletRequest request, @CurrentUser TUserDetails user) {
		UploadResult rtnRes = new UploadResult();
		try {
			Map<String, MultipartFile> files = multiRequest.getFileMap();
			MultipartFile file = files.get("file");
			//
			rtnRes = FileUtil.checkFileFormat(rtnRes, file, "xls,xlsx");
			if (!rtnRes.getIsSuc()) {
				return rtnRes;
			}
			//
			rtnRes = FileUtil.checkFileSize(rtnRes, file, 100000);
			if (!rtnRes.getIsSuc()) {
				return rtnRes;
			}
			// 执行上传
			rtnRes = FileUtil.uploadFile(rtnRes, file);
		}
		catch (Exception e) {
			log.error("上传设备Excel报错");
			e.printStackTrace();
			rtnRes.setIsSuc(false);
			rtnRes.setErrMsg(BizConstant.ERR_UNKNOWN);
		}
		return rtnRes;
	}

	@ApiOperation(value = "解析和检验设备Excel")
	@PostMapping("/device/excel.analysis")
	public EquipExcelDTO excelAnalysis(@ApiParam(value = "excel文件路径", required = true) @RequestParam String excelPath,
			@ApiParam(value = "区县ID", required = true) @RequestParam Integer areaId) {
		System.out.println(areaId + excelPath);
		return equipInfoService.getExcelList(excelPath, areaId);
	}

}
