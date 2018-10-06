/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.web.api;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.wkhmedical.config.ConfigProperties;
import com.wkhmedical.dto.ObdLicDTO;
import com.wkhmedical.service.ObdLicService;
import com.wkhmedical.util.RSAUtil;

import io.swagger.annotations.Api;
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
}
