package com.wkhmedical.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.boot.security.CurrentUser;
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.CarInfoAddBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoEditBody;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.po.CarInfo;
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

	@ApiOperation(value = "获取车辆基本信息")
	@GetMapping("/get")
	public CarInfo getBaseInfo(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		CarInfoParam paramBody = new CarInfoParam();
		paramBody.setId(id);
		return carInfoService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取车辆OBD实时信息（APP用）")
	@PostMapping("/get.obd")
	public ObdCarDTO getCarObdInfo(@ApiParam(name = "eid", value = "车辆ID", required = true) @RequestParam String eid) {
		ObdCarDTO rtnDTO = obdCarService.getObdCar(eid);
		return rtnDTO;
	}

	@ApiOperation(value = "获取车辆固定信息（APP用）")
	@GetMapping("/get.info")
	public CarInfoDTO getCarInfo(@ApiParam(name = "eid", value = "车辆ID", required = true) @RequestParam String eid) {
		return carInfoService.getCarInfo(eid);
	}

	@ApiOperation(value = "获取车辆分页列表（APP用）")
	@PostMapping("/get.list")
	public List<CarInfoDTO> getCarData(@RequestBody @Valid Paging<CarInfoPageParam> paramBody) {
		return carInfoService.getCarInfoList(paramBody);
	}

	@ApiOperation(value = "获取车辆分页对象")
	@PostMapping("/get.page")
	public Page<CarInfo> getCarPage(@RequestBody @Valid Paging<CarInfoPageParam> paramBody) {
		return carInfoService.getCarInfoPage(paramBody);
	}

	@ApiOperation(value = "添加车辆")
	@PostMapping("/add")
	public void carInfoAdd(@RequestBody @Valid CarInfoAddBody paramBody, @CurrentUser TUserDetails user) {
		carInfoService.addCarInfo(paramBody);
	}

	@ApiOperation(value = "修改车辆")
	@PostMapping("/edit")
	public void carInfoEdit(@RequestBody @Valid CarInfoEditBody paramBody) {
		carInfoService.updateCarInfo(paramBody);
	}

	@ApiOperation(value = "删除车辆")
	@DeleteMapping("/delete")
	public boolean carInfoDelete(@ApiParam(value = "车辆ID", required = true) @RequestParam String eid) {
		carInfoService.deleteCarInfo(eid);
		return true;
	}

	@ApiOperation(value = "逻辑删车辆")
	@DeleteMapping("/del")
	public boolean carInfoDel(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		carInfoService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}

	/**
	 * 获取车辆总数
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取车辆总数")
	@GetMapping("/count.sum")
	public Long getCountSum() {
		return carInfoService.getCountSum();
	}

	@ApiOperation(value = "获取OBD最近开机列表")
	@PostMapping("/get.obd.acc")
	public List<ObdCarDTO> getObdAccList(@ApiParam(value = "车辆ID", required = true) @RequestParam String eid) {
		return obdCarService.getObdCarList(eid);
	}

}
