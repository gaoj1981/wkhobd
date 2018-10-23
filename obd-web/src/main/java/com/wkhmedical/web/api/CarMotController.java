package com.wkhmedical.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.boot.security.CurrentUser;
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.dto.ValiAdd;
import com.wkhmedical.dto.ValiEdit;
import com.wkhmedical.po.CarMot;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.CarMotService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "车辆年检接口")
@RequestMapping("/api/carmot")
public class CarMotController {

	@Autowired
	CarMotService carMotService;

	@ApiOperation(value = "获取车辆年检基本信息")
	@GetMapping("/get")
	public CarMot getCarMot(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		CarMotBody paramBody = new CarMotBody();
		paramBody.setId(id);
		return carMotService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取车辆年检分页列表（APP用）")
	@PostMapping("/get.list")
	public List<CarMotDTO> getCarMotData(@RequestBody @Valid Paging<CarMotBody> paramBody) {
		return carMotService.getList(paramBody);
	}

	@ApiOperation(value = "获取车辆年检分页对象")
	@PostMapping("/get.page")
	public Page<CarMotDTO> getCarMotPage(@RequestBody @Valid Paging<CarMotBody> paramBody) {
		return carMotService.getPgList(paramBody);
	}

	@ApiOperation(value = "添加车辆年检（APP用）")
	@PostMapping("/add")
	public void carMotAdd(@RequestBody @Validated({ ValiAdd.class }) CarMotBody paramBody,
			@CurrentUser TUserDetails user) {
		carMotService.addInfo(paramBody);
	}

	@ApiOperation(value = "修改车辆年检")
	@PostMapping("/edit")
	public void carMotEdit(@RequestBody @Validated({ ValiEdit.class }) CarMotBody paramBody) {
		carMotService.updateInfo(paramBody);
	}

	@ApiOperation(value = "删除车辆年检")
	@DeleteMapping("/delete")
	public boolean carMotDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		carMotService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "逻辑删车辆年检")
	@DeleteMapping("/del")
	public boolean carMotDel(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		carMotService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}
}
