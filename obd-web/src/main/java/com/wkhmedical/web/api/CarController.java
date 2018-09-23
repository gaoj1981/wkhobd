package com.wkhmedical.web.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wkhmedical.dto.CarInfoListBody;
import com.wkhmedical.service.CarInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "车辆接口")
@RequestMapping("/api/car")
public class CarController {

	@Autowired
	CarInfoService carInfoService;

	@ApiOperation(value = "获取车辆OBD实时信息")
	@PostMapping("/get.list")
	public void getCarData(@RequestBody @Valid CarInfoListBody paramBody) {
		log.info("car's obd");
		carInfoService.getCarInfoList(paramBody);
	}

}
