package com.wkhmedical.web.api;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.aliyun.OssService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "三方接口")
@RequestMapping("/api/third")
public class ThirdController {

	@Autowired
	OssService ossService;

	@ApiOperation(value = "获取上传文件的存储空间")
	@GetMapping("oss_policy")
	public Map<String, String> getOssPolicy() throws UnsupportedEncodingException {
		return ossService.postPolicy("poster");
	}

}
