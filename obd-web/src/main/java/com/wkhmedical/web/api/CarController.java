package com.wkhmedical.web.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.service.CarInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "车辆接口")
@RequestMapping("/api/car")
public class CarController {

	@Autowired
	CarInfoService carInfoService;

	@ApiOperation(value = "获取车辆OBD实时信息")
	@PostMapping("/get.obd")
	public void getCarObdInfo(@ApiParam(name = "eid", value = "车辆ID", required = true) @RequestParam String eid) {
		log.info("car's obd");
	}

	@ApiOperation(value = "获取车辆基本信息")
	@PostMapping("/get")
	public CarInfoDTO getCarInfo(@ApiParam(name = "eid", value = "车辆ID", required = true) @RequestParam String eid) {
		CarInfoParam paramBody = new CarInfoParam();
		paramBody.setEid(eid);
		return carInfoService.getCarInfo(paramBody);
	}

	@ApiOperation(value = "获取车辆分页列表")
	@PostMapping("/get.list")
	public void getCarData(@RequestBody @Valid CarInfoPageParam paramBody) {
		carInfoService.getCarInfoList(paramBody);
	}

}
