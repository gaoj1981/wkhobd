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
import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurBodyAdd;
import com.wkhmedical.dto.CarInsurBodyEdit;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurParam;
import com.wkhmedical.po.CarInsur;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.CarInsurService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "车辆保险接口")
@RequestMapping("/api/carinsur")
public class CarInsurController {

	@Autowired
	CarInsurService carInsurService;

	@ApiOperation(value = "获取车辆保险基本信息")
	@GetMapping("/get")
	public CarInsur getCarInsur(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		CarInsurParam paramBody = new CarInsurParam();
		paramBody.setId(id);
		return carInsurService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取车辆保险固定信息")
	@GetMapping("/get.info")
	public CarInsurDTO getCarInsurInfo(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		CarInsurBody paramBody = new CarInsurBody();
		paramBody.setId(id);
		return carInsurService.getExInfo(paramBody);
	}

	@ApiOperation(value = "获取车辆保险分页列表（APP用）")
	@PostMapping("/get.list")
	public List<CarInsurDTO> getCarInsurData(@RequestBody @Valid Paging<CarInsurBody> paramBody) {
		return carInsurService.getList(paramBody);
	}

	@ApiOperation(value = "获取车辆保险分页对象")
	@PostMapping("/get.page")
	public Page<CarInsurDTO> getCarInsurPage(@RequestBody @Valid Paging<CarInsurBody> paramBody) {
		return carInsurService.getPgList(paramBody);
	}

	@ApiOperation(value = "添加车辆保险（APP用）")
	@PostMapping("/add")
	public void carInsurAdd(@RequestBody @Valid CarInsurBodyAdd paramBody, @CurrentUser TUserDetails user) {
		carInsurService.addInfo(paramBody);
	}

	@ApiOperation(value = "修改车辆保险")
	@PostMapping("/edit")
	public void carInsurEdit(@RequestBody @Valid CarInsurBodyEdit paramBody) {
		carInsurService.updateInfo(paramBody);
	}

	@ApiOperation(value = "删除车辆保险")
	@DeleteMapping("/delete")
	public boolean carInsurDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		carInsurService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "逻辑删车辆保险")
	@DeleteMapping("/del")
	public boolean carInsurDel(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		carInsurService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}
}
