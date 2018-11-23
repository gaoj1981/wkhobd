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
import com.wkhmedical.dto.ValiAdd;
import com.wkhmedical.dto.ValiEdit;
import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.po.WareRecord;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.WareRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "维修保养接口")
@RequestMapping("/api/warerecord")
public class WareRecordController {

	@Autowired
	WareRecordService wareRecordService;

	@ApiOperation(value = "获取维修保养基本信息")
	@GetMapping("/get")
	public WareRecord getWareRecord(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		WareRecordBody paramBody = new WareRecordBody();
		paramBody.setId(id);
		return wareRecordService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取维修保养分页列表（APP用）")
	@PostMapping("/get.list")
	public List<WareRecordDTO> getWareRecordData(@RequestBody @Valid Paging<WareRecordBody> paramBody) {
		return wareRecordService.getList(paramBody);
	}

	@ApiOperation(value = "获取维修保养分页对象")
	@PostMapping("/get.page")
	public Page<WareRecordDTO> getWareRecordPage(@RequestBody @Valid Paging<WareRecordBody> paramBody) {
		return wareRecordService.getPgList(paramBody);
	}

	@ApiOperation(value = "添加维修保养（APP用）")
	@PostMapping("/add")
	public void wareRecordAdd(@RequestBody @Validated({ ValiAdd.class }) WareRecordBody paramBody, @CurrentUser TUserDetails user) {
		wareRecordService.addInfo(paramBody);
	}

	@ApiOperation(value = "修改维修保养")
	@PostMapping("/edit")
	public void wareRecordEdit(@RequestBody @Validated({ ValiEdit.class }) WareRecordBody paramBody) {
		wareRecordService.updateInfo(paramBody);
	}

	@ApiOperation(value = "删除维修保养")
	@DeleteMapping("/delete")
	public boolean wareRecordDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		wareRecordService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "逻辑删维修保养")
	@DeleteMapping("/del")
	public boolean wareRecordDel(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		wareRecordService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}

	/**
	 * 获取维修保养总数
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取维修保养总数")
	@GetMapping("/count.sum")
	public Long getCountSum() {
		return wareRecordService.getCountSum();
	}

}
