package com.wkhmedical.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.boot.security.CurrentUser;
import com.wkhmedical.dto.CarInfoBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.CarInfoService;
import com.wkhmedical.service.ObdCarService;

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
	@Autowired
	ObdCarService obdCarService;

	@ApiOperation(value = "获取车辆OBD实时信息")
	@PostMapping("/get.obd")
	public ObdCarDTO getCarObdInfo(@ApiParam(name = "eid", value = "车辆ID", required = true) @RequestParam String eid) {
		log.info("car's obd");
		return obdCarService.getObdCar(eid);
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
	public List<CarInfoDTO> getCarData(@RequestBody @Valid CarInfoPage paramBody) {
		return carInfoService.getCarInfoList(paramBody);
	}

	@ApiOperation(value = "添加车辆")
	@PostMapping("/add")
	public void carInfoAdd(@RequestBody @Valid CarInfoBody paramBody, @CurrentUser TUserDetails user) {
		carInfoService.addCarInfo(paramBody);
	}

	@ApiOperation(value = "修改车辆")
	@PostMapping("/edit")
	public void carInfoEdit(@RequestBody @Valid CarInfoBody paramBody) {
		carInfoService.updateCarInfo(paramBody);
	}

	@ApiOperation(value = "删除车辆")
	@DeleteMapping("/delete")
	public boolean carInfoDel(@ApiParam(value = "id主KEY", required = true) @RequestParam Long id) {
		carInfoService.deleteCarInfo(id);
		return true;
	}
}
